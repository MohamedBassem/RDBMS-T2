package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.edu.guc.dbms.components.BufferManager;
import eg.edu.guc.dbms.components.LogManagerImpl;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.helpers.Page;
import eg.edu.guc.dbms.interfaces.Command;
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
	String transaction;

	public DeleteCommand(String strTableName,
			HashMap<String, String> htblColNameValue, String strOperator,
			CSVReader reader, Properties properties, BTreeFactory btfactory,
			BufferManager bufferManager) {
		this.strTableName = strTableName;
		this.htblColNameValue = htblColNameValue;
		this.strOperator = strOperator;
		this.reader = reader;
		this.properties = properties;
		this.btfactory = btfactory;
		this.bufferManager = bufferManager;
		select = new SelectCommand(this.btfactory, this.reader,
				this.properties, this.bufferManager, this.strTableName,
				this.htblColNameValue, this.strOperator);
	}



	public void execute() throws DBEngineException {
		select.execute();
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
			Page page = bufferManager.read(strTableName, pageNumber, true);
			page.set(rowNumber, null);
			bufferManager.write(strTableName, pageNumber, page);
				LogManagerImpl logManager = LogManagerImpl.getInstance();
				try {
					logManager.recordDelete(transaction, strTableName,
							pageNumber,
							getKeyValue(strTableName, page, rowNumber),
							htblColNameValue);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public void deleteFromTree() throws DBEngineException, IOException {
		ArrayList<String> indexedColumns = properties
				.getIndexedColumns(strTableName);
		ArrayList<String> pointers = select.getResultPointers();
		ArrayList<HashMap<String, String>> results = select.getResults();
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
