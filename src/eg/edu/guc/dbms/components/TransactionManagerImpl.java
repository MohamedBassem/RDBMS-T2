package eg.edu.guc.dbms.components;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Semaphore;


import eg.edu.guc.dbms.commands.CreateIndex;
import eg.edu.guc.dbms.commands.CreateTableCommand;
import eg.edu.guc.dbms.commands.DeleteCommand;
import eg.edu.guc.dbms.commands.InsertCommand;
import eg.edu.guc.dbms.commands.IntermediateSelectCommand;
import eg.edu.guc.dbms.commands.ProductCommand;
import eg.edu.guc.dbms.commands.ProjectCommand;
import eg.edu.guc.dbms.commands.SelectCommand;
import eg.edu.guc.dbms.commands.UpdateCommand;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.factories.BufferManagerFactory;
import eg.edu.guc.dbms.factories.TransactionManagerFactory;
import eg.edu.guc.dbms.interfaces.Command;
import eg.edu.guc.dbms.interfaces.LogManager;
import eg.edu.guc.dbms.interfaces.SQLParser;
import eg.edu.guc.dbms.interfaces.TransactionCallbackInterface;
import eg.edu.guc.dbms.interfaces.TransactionManager;
import eg.edu.guc.dbms.sql.Create;
import eg.edu.guc.dbms.sql.Delete;
import eg.edu.guc.dbms.sql.Index;
import eg.edu.guc.dbms.sql.Insert;
import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import eg.edu.guc.dbms.sql.PhysicalPlanTree.Operation;
import eg.edu.guc.dbms.sql.Product;
import eg.edu.guc.dbms.sql.Project;
import eg.edu.guc.dbms.sql.Scan;
import eg.edu.guc.dbms.sql.Select;
import eg.edu.guc.dbms.sql.Update;
import eg.edu.guc.dbms.transactions.Transaction;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;

public class TransactionManagerImpl implements TransactionManager {

	private static final int THREAD_POOL_SIZE = 5;
	
	private BufferManager bufferManager;
	private LogManager logManager;
	private BTreeFactory bTreeFactory;
	private CSVReader reader;
	private Properties properties;
	
	private static TransactionCallbackInterface transactionCallBack;
	
	
	public TransactionManagerImpl(BufferManager bufferManager, LogManager logManager, BTreeFactory btree, Properties properties, CSVReader reader2) {
		//this.bufferManager 	= bufferManager;
		this.logManager 	= logManager;
		this.reader 		= reader2;
		this.properties = properties;
		this.bTreeFactory = btree;
		this.bufferManager = BufferManagerFactory.getInstance(btree, properties);

	}
	
	public static void setCallBack(TransactionCallbackInterface callBack) {
		transactionCallBack = callBack;
	}
	
	@Override
	public void executeTrasaction(PhysicalPlanTree tree) {
		ArrayList<Command> steps = new ArrayList<Command>();
		Transaction transaction = new Transaction(bufferManager, logManager, steps);
		treeToSteps(tree, steps, transaction);
		transaction.execute(transactionCallBack);
	}
	
	public static void runConcurrently(String sqlFile){
		TransactionManagerImpl tr = (TransactionManagerImpl) TransactionManagerFactory.getInstance();
		SQLParser parser = SQLParserImpl.getInstance();
		String[] sqlStatments = sqlFile.trim().split(";");
		for(String sqlStatment : sqlStatments){
			
			if(!parser.parseSQLStatement(sqlStatment)){
				System.out.println("ERROR" + parser.getErrorMessage());
				continue;
			}

			tr.executeTrasaction(parser.getParseTree());

						
		}
	}

	public static void main(String[] args) throws IOException {
		if(args.length == 1){
			BufferedReader reader = new BufferedReader(new FileReader(new File(args[0])));
			String res = "";
			while(true){
				String line = reader.readLine();
				if(line == null) break;
				res += line;
			}
			reader.close();
			runConcurrently(res);
		}else{
			
		}	
		
	}
	
	
	
	private void treeToSteps(PhysicalPlanTree tree, ArrayList<Command> steps,Transaction transaction) {
		if (tree == null) {
			return;
		}
		for (PhysicalPlanTree node : tree.getChildren()) {
			treeToSteps(node, steps, transaction);
		}
		Command step = null;
		if (tree.getOperation() == Operation.CREATE_TABLE) {
			Create node = (Create) tree;
			step = new CreateTableCommand(node.getTableName(), node.getTableTypes(), node.getTableColRefs(), node.getKeyColName(), 
					this.reader,this.bTreeFactory,properties, bufferManager, transaction);
		} else if (tree.getOperation() == Operation.INDEX) {
			Index node = (Index) tree;
			step = new CreateIndex(node.getTableName(), node.getColumnName(), properties, reader, bTreeFactory, bufferManager, transaction.getId());
		} else if (tree.getOperation() == Operation.INSERT) {
			Insert node = (Insert) tree;
			step = new InsertCommand(bTreeFactory, reader, bufferManager, node.getTableName(), properties, node.getColValues(), transaction.getId());
		} else if (tree.getOperation() == Operation.SCAN) {
			Scan node = (Scan) tree;
			step = new SelectCommand(bTreeFactory, reader, properties, bufferManager, node.getTableName(), null, null, transaction.getId());
		} else if (tree.getOperation() == Operation.UPDATE) {
			Update node = (Update) tree;
			step = new UpdateCommand(bTreeFactory, reader, properties, node.getTableName(), node.getColWhereValues(), node.getOperator(), node.getColValues(), bufferManager, transaction.getId());
		} else if (tree.getOperation() == Operation.PROJECT) {
			Project node = (Project) tree;
			step = new ProjectCommand(steps.get(steps.size() - 1).getResult(), node.getProjectionColumns());
		} else if (tree.getOperation() == Operation.PRODUCT) {
			Product node = (Product) tree;
			List<HashMap<String, String>> relation1 = steps.get(steps.size() - 1).getResult();
			List<HashMap<String, String>> relation2 = steps.get(steps.size() - 2).getResult();
			step = new ProductCommand(relation1, relation2);
		} else if (tree.getOperation() == Operation.SELECT) {
			Select node = (Select) tree;
			List<HashMap<String, String>> previousResult = steps.get(steps.size() - 1).getResult();
			step = new IntermediateSelectCommand(previousResult, node.getColValues(), node.getOperator(),properties);
		} else if (tree.getOperation() == Operation.DELETE) {
			Delete node = (Delete) tree;
			step = new DeleteCommand(node.getTableName(), node.getColWhereValues(), node.getOperator(), reader, properties, bTreeFactory, bufferManager, transaction.getId());
		}
		steps.add(step);
	}

	

}
