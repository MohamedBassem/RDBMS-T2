package team2.engine;

import java.util.Hashtable;
import java.util.Iterator;

import team2.exceptions.DBAppException;
import team2.exceptions.DBEngineException;


public class Engine {
	
	public void init(){
		// TODO
	}
	
	public void createTable(String strTableName,
							Hashtable<String,String> htblColNameType,
							Hashtable<String,String>htblColNameRefs,
							String strKeyColName) 
									throws DBAppException {
		// TODO
	}
	
	public void createIndex(String strTableName, String strColName) throws DBAppException {
		// TODO
	}
	
	public void insertIntoTable(String strTableName,
								Hashtable<String,String> htblColNameValue)
										throws DBAppException {
		// TODO
	}
	
	public void deleteFromTable(String strTableName,
								Hashtable<String,String> htblColNameValue,
								String strOperator)
										throws DBEngineException {
		// TODO
	}
	
	public Iterator selectFromTable(String strTable,
									Hashtable<String,String> htblColNameValue,
									String strOperator)
											throws DBEngineException {
		// TODO 
		return null;
	}
	
	public void saveAll() throws DBEngineException {
		// TODO
	}
}
