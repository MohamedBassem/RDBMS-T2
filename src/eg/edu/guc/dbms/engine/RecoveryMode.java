package eg.edu.guc.dbms.engine;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import eg.edu.guc.dbms.commands.CreateIndex;
import eg.edu.guc.dbms.commands.CreateTableCommand;
import eg.edu.guc.dbms.commands.DeleteCommand;
import eg.edu.guc.dbms.commands.InsertCommand;
import eg.edu.guc.dbms.commands.SelectCommand;
import eg.edu.guc.dbms.commands.UpdateCommand;
import eg.edu.guc.dbms.components.BufferManager;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.factories.LogManagerFactory;
import eg.edu.guc.dbms.interfaces.LogManager;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;

public class RecoveryMode {

	BTreeFactory bTreeFactory;
	CSVReader reader;
	Properties properties;
	BufferManager bufferManager;
	private LogManager logManager;
	long transactionId;

	public RecoveryMode() {
		this.init();
		this.logManager = LogManagerFactory.getInstance();
	}

	public RecoveryMode(LogManager logManager) {
		this.init();
		this.logManager = logManager;
	}

	public void init() {
		this.reader = new CSVReader();
		this.properties = new Properties(reader);
		this.bTreeFactory = new BTreeFactory(properties.getBTreeN());
		this.bufferManager = new BufferManager(
				properties.getMinimumEmptyBufferSlots(),
				properties.getMaximumUsedBufferSlots(), false);
		this.bufferManager.init();
	}

	public void createTable(String strTableName,
			HashMap<String, String> htblColNameType,
			HashMap<String, String> htblColNameRefs, String strKeyColName)
			throws DBEngineException {
		CreateTableCommand newTable = new CreateTableCommand(strTableName,
				htblColNameType, htblColNameRefs, strKeyColName, this.reader,
				this.bTreeFactory, properties, bufferManager);
		newTable.execute();

	}

	public void createIndex(String strTableName, String strColName)
			throws DBEngineException {
		CreateIndex createIndex = new CreateIndex(strTableName, strColName,
				this.properties, reader, bTreeFactory, bufferManager);
		createIndex.execute();
	}

	public void insertIntoTable(String strTableName,
			HashMap<String, String> htblColNameValue) throws DBEngineException {
		InsertCommand insertCommand = new InsertCommand(this.bTreeFactory,
				reader, bufferManager, strTableName, properties,
				htblColNameValue, logManager, transactionId);
		insertCommand.execute();
		bufferManager.runFlusher();

	}

	public void deleteFromTable(String strTableName,
			HashMap<String, String> htblColNameValue, String strOperator)
			throws DBEngineException {
		DeleteCommand delete = new DeleteCommand(strTableName,
				htblColNameValue, strOperator, reader, properties,
				bTreeFactory, bufferManager, transactionId, logManager);
		delete.execute();
		bufferManager.runFlusher();

	}

	public Iterator<HashMap<String, String>> selectFromTable(String strTable,
			HashMap<String, String> htblColNameValue, String strOperator)
			throws DBEngineException {
		SelectCommand selectCommand = new SelectCommand(this.bTreeFactory,
				this.reader, properties, bufferManager, strTable,
				htblColNameValue, strOperator, transactionId);
		selectCommand.execute();
		Iterator<HashMap<String, String>> results = selectCommand.getResults()
				.iterator();
		if (results.hasNext() == false) {
			return null;
		} else {
			return results;
		}
	}

	/**
	 * 
	 * @param strTableName
	 * @param htblColNameValue
	 * @param strOperator
	 * @param colValue
	 *            contains the condition on which the value is updated upon
	 * @throws IOException
	 * @throws DBEngineException
	 */
	public void updateTable(String strTableName,
			HashMap<String, String> htblColNameValue, String strOperator,
			HashMap<String, String> colValue) throws IOException,
			DBEngineException {
		UpdateCommand updateCommand = new UpdateCommand(this.bTreeFactory,
				this.reader, this.properties, strTableName, htblColNameValue,
				strOperator, colValue, bufferManager, transactionId);
		updateCommand.execute();
	}

	public void saveAll() throws DBEngineException {
		this.bTreeFactory.saveAll();
	}
}
