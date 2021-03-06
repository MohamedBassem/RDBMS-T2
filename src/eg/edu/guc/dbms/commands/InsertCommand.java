package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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

public class InsertCommand implements Command {
	BTreeFactory btFactory;
	CSVReader reader;
	String tableName;
	Properties properties;
	HashMap<String, String> htblColNameValue;
	BufferManager bufferManager;
	long transactionId;
	LogManager logManager;

	public InsertCommand(BTreeFactory btFactory, CSVReader reader,
			BufferManager bufferManager, String tableName,
			Properties properties, HashMap<String, String> htblColNameValue,
			long transactionId) {
		this.btFactory = btFactory;
		this.reader = reader;
		this.tableName = tableName;
		this.properties = properties;
		this.htblColNameValue = htblColNameValue;
		this.bufferManager = bufferManager;
		this.logManager = LogManagerFactory.getInstance();
		this.transactionId = transactionId;
	}

	public InsertCommand(BTreeFactory btFactory, CSVReader reader,
			BufferManager bufferManager, String tableName,
			Properties properties, HashMap<String, String> htblColNameValue,
			LogManager logManager, long transactionId) {
		this(btFactory, reader, bufferManager, tableName, properties,
				htblColNameValue, transactionId);
		this.logManager = logManager;
		this.transactionId = transactionId;
	}

	@Override
	public void execute() throws DBEngineException {
		if (properties.getData().get(tableName) == null)
			throw new DBEngineException(
					"Table name is wrong or it doesn't exist.");

		BTreeAdopter tree;
		Set<String> columns = htblColNameValue.keySet();
		for (String column : columns) {
			if (properties.getData().get(tableName).get(column) == null)
				throw new DBEngineException(
						"Column name is wrong or it doesn't exist.");
			if (properties.isPrimaryKey(tableName, column)) {
				if (htblColNameValue.get(column) == null)
					throw new DBEngineException(
							"The primary key can't be null.");
				tree = btFactory.getBtree(tableName, column);
				try {
					if (tree.find(htblColNameValue.get(column)) != null)
						throw new DBEngineException(
								"Th primary key you're trying to insert is not unique.");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (properties.isForeignKey(tableName, column)) {
				if (htblColNameValue.get(column) != null) {
					String reference = properties.getReferenceColumn(tableName,
							column);
					String[] references = reference.split("\\.");
					BTreeAdopter refTree = btFactory.getBtree(references[0],
							references[1]);
					try {
						if (refTree.find(htblColNameValue.get(column)) == null)
							throw new DBEngineException(
									"The foreign key you're trying to "
											+ "insert doesn't reference anything.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		int lastPage = bufferManager.getLastPageIndex(tableName);
		Page page = bufferManager.read(transactionId, tableName, lastPage,
				false);
		if (page.size() + 1 == properties.getMaximumPageSize()) {
			lastPage++;
		}
		page = bufferManager.read(transactionId, tableName, lastPage, true);
		page.add(htblColNameValue);
		int insertRow = page.size() - 1;
		bufferManager.write(transactionId, tableName, lastPage, page);
		try {
			logManager.recordInsert(transactionId + "", tableName, lastPage,
					htblColNameValue);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ArrayList<String> indexedColumns = properties
				.getIndexedColumns(tableName);

		for (String column : indexedColumns) {
			String pointer = tableName + " " + lastPage + " " + insertRow;
			tree = btFactory.getBtree(tableName, column);
			try {
				tree.insert(htblColNameValue.get(column), pointer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public List<HashMap<String, String>> getResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
