package team2.engine;

import java.util.Hashtable;
import java.util.Iterator;

import team2.commands.CreateIndex;
import team2.commands.CreateTableCommand;
import team2.commands.DeleteCommand;
import team2.commands.InsertCommand;
import team2.commands.SelectCommand;
import team2.exceptions.DBAppException;
import team2.exceptions.DBEngineException;
import team2.util.CSVReader;
import team2.util.Properties;
import team2.util.btrees.BTreeFactory;


public class Engine {
	
	BTreeFactory bTreeFactory;
	CSVReader reader;
	Properties properties;
	
	public void init(){
		this.bTreeFactory = new BTreeFactory();
		this.reader = new CSVReader();
		this.properties = new Properties(reader);
	}
	
	public void createTable(String strTableName,

							Hashtable<String,String> htblColNameType,
							Hashtable<String,String>htblColNameRefs,
							String strKeyColName) 
									throws DBEngineException {
	CreateTableCommand newTable = new CreateTableCommand(strTableName, htblColNameType, htblColNameRefs, strKeyColName, this.reader);
	newTable.execute(); 
	
	}

	
	public void createIndex(String strTableName, String strColName) throws DBAppException, DBEngineException {
		CreateIndex newIndex = new CreateIndex(strTableName, strColName, properties, reader, bTreeFactory);
		newIndex.execute();
	}
	
	public void insertIntoTable(String strTableName,
								Hashtable<String,String> htblColNameValue)
										throws DBAppException, DBEngineException {
		InsertCommand insert = new InsertCommand(bTreeFactory, reader, strTableName, properties, htblColNameValue);
		insert.execute();
	}
	
	public void deleteFromTable(String strTableName,
								Hashtable<String,String> htblColNameValue,
								String strOperator)
										throws DBEngineException {
		DeleteCommand delete = new DeleteCommand(strTableName, htblColNameValue, strOperator, reader);
		delete.execute(); 
	
	}

	
	public Iterator< Hashtable<String, String >> selectFromTable(String strTable,
									Hashtable<String,String> htblColNameValue,
									String strOperator)
											throws DBEngineException {
		SelectCommand selectCommand = new SelectCommand(this.bTreeFactory, this.reader,properties, strTable, htblColNameValue, strOperator);
		selectCommand.execute();
		return selectCommand.getResults().iterator();
				
	}
	
	public void saveAll() throws DBEngineException {
		// TODO
	}
}
