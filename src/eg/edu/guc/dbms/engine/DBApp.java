package eg.edu.guc.dbms.engine;

import java.util.Hashtable;
import java.util.Iterator;

import eg.edu.guc.dbms.commands.CreateIndex;
import eg.edu.guc.dbms.commands.CreateTableCommand;
import eg.edu.guc.dbms.commands.DeleteCommand;
import eg.edu.guc.dbms.commands.InsertCommand;
import eg.edu.guc.dbms.commands.SelectCommand;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;



public class DBApp {
	
	BTreeFactory bTreeFactory;
	CSVReader reader;
	Properties properties;
	
	public DBApp(){
		this.init();
	}
	public void init(){
		this.reader = new CSVReader();
		this.properties = new Properties(reader);
		this.bTreeFactory = new BTreeFactory(properties.getBTreeN());
	}
	
	public void createTable(String strTableName,

							Hashtable<String,String> htblColNameType,
							Hashtable<String,String>htblColNameRefs,
							String strKeyColName) 
									throws DBEngineException {
		CreateTableCommand newTable = new CreateTableCommand(strTableName, htblColNameType, htblColNameRefs, strKeyColName, this.reader,this.bTreeFactory,properties);
		newTable.execute(); 
	
	}

	public void createIndex(String strTableName, String strColName) throws DBEngineException {
		CreateIndex createIndex = new CreateIndex(strTableName, strColName, this.properties, reader, bTreeFactory);
		createIndex.execute();
	}
	
	public void insertIntoTable(String strTableName,
								Hashtable<String,String> htblColNameValue)
										throws DBEngineException {
		InsertCommand insertCommand = new InsertCommand(this.bTreeFactory, reader, strTableName, properties, htblColNameValue);
		insertCommand.execute();
		
	}
	
	public void deleteFromTable(String strTableName,
								Hashtable<String,String> htblColNameValue,
								String strOperator)
										throws DBEngineException {
		DeleteCommand delete = new DeleteCommand(strTableName, htblColNameValue, strOperator, reader,properties,bTreeFactory);
		delete.execute(); 
	
	}

	
	public Iterator< Hashtable<String, String >> selectFromTable(String strTable,
									Hashtable<String,String> htblColNameValue,
									String strOperator)
											throws DBEngineException {
		SelectCommand selectCommand = new SelectCommand(this.bTreeFactory, this.reader,properties, strTable, htblColNameValue, strOperator);
		selectCommand.execute();
		Iterator< Hashtable<String, String >> results = selectCommand.getResults().iterator();
		if(results.hasNext() == false){
			return null;
		}else{
			return results;
		}
	}
	
	public void saveAll() throws DBEngineException {
		this.bTreeFactory.saveAll();
	}
}
