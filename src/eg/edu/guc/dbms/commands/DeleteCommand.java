package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.edu.guc.dbms.components.BufferManager;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.factories.LogManagerFactory;
import eg.edu.guc.dbms.helpers.Page;
import eg.edu.guc.dbms.interfaces.Command;
import eg.edu.guc.dbms.interfaces.LogManager;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.btrees.BTreeAdopter;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;

public class DeleteCommand implements Command {
	String strTableName;
	HashMap<String, String> htblColNameValue;
	String strOperator;
	CSVReader reader;
	BTreeFactory btfactory;
	int pageId;
	Properties properties;
	BufferManager bufferManager;
	SelectCommand select;
	long transactionId;
	LogManager logManager;

	public DeleteCommand(String strTableName,
			HashMap<String, String> htblColNameValue, String strOperator,
			CSVReader reader, Properties properties, BTreeFactory btfactory,
			BufferManager bufferManager, long transactionId) {
		this.strTableName = strTableName;
		this.htblColNameValue = htblColNameValue;
		this.strOperator = strOperator;
		this.reader = reader;
		this.properties = properties;
		this.btfactory = btfactory;
		this.bufferManager = bufferManager;
		this.transactionId = transactionId;
		select = new SelectCommand(this.btfactory, this.reader,
				this.properties, this.bufferManager, this.strTableName,
				this.htblColNameValue, this.strOperator, transactionId);
		this.logManager = LogManagerFactory.getInstance();

	}

	public DeleteCommand(String strTableName,
			HashMap<String, String> htblColNameValue, String strOperator,
			CSVReader reader, Properties properties, BTreeFactory btfactory,
			BufferManager bufferManager, long transactionId,
			LogManager logManager) {
		this(strTableName, htblColNameValue, strOperator, reader, properties,
				btfactory, bufferManager, transactionId);
		this.logManager = logManager;
		select = new SelectCommand(this.btfactory, this.reader,
				this.properties, this.bufferManager, this.strTableName,
				this.htblColNameValue, this.strOperator, transactionId);
		

	}

	public void execute() throws DBEngineException {
		select.execute();
//		System.out.println("sss "+select.getResult());
		this.deleteFromTable();
		try {
			this.deleteFromTree();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getKeyValue(String tableName, Page page, int rowNumber) {
		return page.get(rowNumber)
				.get(properties.getTablePrimaryKey(tableName));
	}

	public void deleteFromTable() throws DBEngineException {
		ArrayList<String> pointers = select.getResultPointers();
		for (int i = 0; i < pointers.size(); i++) {
			String[] x = ((String) pointers.get(i)).split(" ");
			int pageNumber = Integer.parseInt(x[1]);
			pageId = pageNumber;
			int rowNumber = Integer.parseInt(x[2]);
			Page page = bufferManager.read(transactionId, strTableName,
					pageNumber, true);
			try {
				logManager.recordDelete(transactionId + "", strTableName,
						pageNumber, getKeyValue(strTableName, page, rowNumber),
						htblColNameValue);
			} catch (IOException e) {
				e.printStackTrace();
			}
			page.set(rowNumber, null);
			bufferManager.write(transactionId, strTableName, pageNumber, page);

		}
	}

	public void deleteFromTree() throws DBEngineException, IOException {
		ArrayList<String> indexedColumns = properties
				.getIndexedColumns(strTableName);
		ArrayList<String> pointers = select.getResultPointers();
		List<HashMap<String, String>> results = select.getResult();
		for (int i = 0; i < indexedColumns.size(); i++) {
			BTreeAdopter adoptor = btfactory.getBtree(strTableName,
					indexedColumns.get(i));
			for (int j = 0; j < pointers.size(); j++) {
				adoptor.delete(results.get(j).get(indexedColumns.get(i)),
						pointers.get(j));
			}
		}
	}

	@Override
	public List<HashMap<String, String>> getResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
