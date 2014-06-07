package eg.edu.guc.dbms.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.edu.guc.dbms.commands.CreateIndex;
import eg.edu.guc.dbms.commands.CreateTableCommand;
import eg.edu.guc.dbms.commands.InsertCommand;
import eg.edu.guc.dbms.commands.IntermediateSelectCommand;
import eg.edu.guc.dbms.commands.ProductCommand;
import eg.edu.guc.dbms.commands.ProjectCommand;
import eg.edu.guc.dbms.commands.SelectCommand;
import eg.edu.guc.dbms.commands.UpdateCommand;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.factories.TransactionManagerFactory;
import eg.edu.guc.dbms.interfaces.Command;
import eg.edu.guc.dbms.interfaces.LogManager;
import eg.edu.guc.dbms.interfaces.SQLParser;
import eg.edu.guc.dbms.interfaces.TransactionManager;
import eg.edu.guc.dbms.sql.Create;
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

	private BufferManager bufferManager;
	private LogManager logManager;
	private BTreeFactory bTreeFactory;
	private CSVReader reader;
	private Properties properties;
	
	
	public TransactionManagerImpl(BufferManager bufferManager, LogManager logManager) {
		//this.bufferManager 	= bufferManager;
		this.logManager 	= logManager;
		this.reader = new CSVReader();
		this.properties = new Properties(reader);
		this.bTreeFactory = new BTreeFactory(properties.getBTreeN());
		System.out.println(properties.getBTreeN());
		this.bufferManager = new BufferManager(
				properties.getMinimumEmptyBufferSlots(),
				properties.getMaximumUsedBufferSlots(), false);
		this.bufferManager.init();
		
	}
	
	@Override
	public void executeTrasaction(PhysicalPlanTree tree) {
		ArrayList<Command> steps = new ArrayList<Command>();
		Transaction transaction = new Transaction(bufferManager, logManager, steps);
		treeToSteps(tree, steps, transaction);
		transaction.execute();
	}
	
	public static void main(String[] args) {
		TransactionManagerImpl tr = (TransactionManagerImpl) TransactionManagerFactory.getInstance();
		SQLParser parser = SQLParserImpl.getInstance();
		//parser.parseSQLStatement("CREATE TABLE Gamdeen (name STRING PRIMARY KEY, gamadan INT)");
		parser.parseSQLStatement("SELECT * FROM Gamdeen");
		PhysicalPlanTree tree = parser.getParseTree();
		tr.executeTrasaction(tree);
		//parser.parseSQLStatement("create table Users (name STRING PRIMARY KEY, age INT)");
		//tree = parser.getParseTree();
		//tr.executeTrasaction(tree);
		
		
	}
	
	public static PhysicalPlanTree createTable() {
//		Create t = new Create();
//		t.setKeyColName("id");
//		HashMap<String, String> types = new HashMap<String, String>();
//		types.put("name", "VARCHAR");
//		types.put("id", "INT");
//		t.setTableColRefs(new HashMap<String, String>());
//		t.setTableName("Users");
//		t.setTableTypes(types);
		return null;
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
			step = new UpdateCommand(bTreeFactory, reader, properties, node.getTableName(), node.getColSearchValue(), node.getOperator(), node.getColValue(), bufferManager, transaction.getId());
		} else if (tree.getOperation() == Operation.PROJECT) {
			Project node = (Project) tree;
			//TODO
			step = new ProjectCommand(null, node.getProjectionColumns());
		} else if (tree.getOperation() == Operation.PRODUCT) {
			Product node = (Product) tree;
			List<HashMap<String, String>> relation1 = steps.get(steps.size() - 1).getResult();
			List<HashMap<String, String>> relation2 = steps.get(steps.size() - 1).getResult();
			step = new ProductCommand(relation1, relation2);
		} else if (tree.getOperation() == Operation.SELECT) {
			Select node = (Select) tree;
			List<HashMap<String, String>> previousResult = steps.get(steps.size() - 1).getResult();
			step = new IntermediateSelectCommand(previousResult, node.getColValues(), node.getOperator());
		}	
		steps.add(step);
	}

	

}
