package eg.edu.guc.dbms.engine;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;

import eg.edu.guc.dbms.commands.CreateIndex;
import eg.edu.guc.dbms.commands.CreateTableCommand;
import eg.edu.guc.dbms.commands.DeleteCommand;
import eg.edu.guc.dbms.commands.InsertCommand;
import eg.edu.guc.dbms.commands.SelectCommand;
import eg.edu.guc.dbms.commands.UpdateCommand;
import eg.edu.guc.dbms.components.BufferManager;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;

public class DBApp {

	BTreeFactory bTreeFactory;
	CSVReader reader;
	Properties properties;
	BufferManager bufferManager;
	
	public DBApp(){
		this.init();
	}

	public void init() {
		this.reader = new CSVReader();
		this.properties = new Properties(reader);
		this.bTreeFactory = new BTreeFactory(properties.getBTreeN());
		// TODO
		this.bufferManager = new BufferManager(0, 100);
	}

	public void createTable(String strTableName,
							Hashtable<String,String> htblColNameType,
							Hashtable<String,String>htblColNameRefs,
							String strKeyColName) 
									throws DBEngineException {
		CreateTableCommand newTable = new CreateTableCommand(strTableName, htblColNameType, htblColNameRefs, strKeyColName, this.reader,this.bTreeFactory,properties,bufferManager);
		newTable.execute(); 
	
	}

	public void createIndex(String strTableName, String strColName) throws DBEngineException {
		CreateIndex createIndex = new CreateIndex(strTableName, strColName, this.properties, reader, bTreeFactory,bufferManager);
		createIndex.execute();
	}

	public void insertIntoTable(String strTableName,
								Hashtable<String,String> htblColNameValue)
										throws DBEngineException {
		InsertCommand insertCommand = new InsertCommand(this.bTreeFactory, reader,bufferManager, strTableName, properties, htblColNameValue);
		insertCommand.execute();

	}

	public void deleteFromTable(String strTableName,
								Hashtable<String,String> htblColNameValue,
								String strOperator)
										throws DBEngineException {
		DeleteCommand delete = new DeleteCommand(strTableName, htblColNameValue, strOperator, reader,properties,bTreeFactory,bufferManager);
		delete.execute(); 
	
	}

	
	public Iterator< Hashtable<String, String >> selectFromTable(String strTable,
									Hashtable<String,String> htblColNameValue,
									String strOperator)
											throws DBEngineException {
		SelectCommand selectCommand = new SelectCommand(this.bTreeFactory, this.reader,properties,bufferManager, strTable, htblColNameValue, strOperator);
		selectCommand.execute();
		Iterator<Hashtable<String, String>> results = selectCommand
				.getResults().iterator();
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
	 * @param colValue contains the condition on which the value is updated upon
	 * @throws IOException
	 * @throws DBEngineException
	 */
	public void updateTable(String strTableName,
			Hashtable<String, String> htblColNameValue, String strOperator,
			Hashtable<String, String> colValue) throws IOException, DBEngineException {
		UpdateCommand updateCommand = new UpdateCommand(this.bTreeFactory,
				this.reader, this.properties, strTableName, htblColNameValue,
				strOperator, colValue);
		updateCommand.execute();
	}

	public void saveAll() throws DBEngineException {
		this.bTreeFactory.saveAll();
	}
}
