package eg.edu.guc.dbms.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import eg.edu.guc.dbms.components.BufferManager;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.Command;
import eg.edu.guc.dbms.transactions.Transaction;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.Utils;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;

public class CreateTableCommand implements Command {
	CSVReader reader;
	String strTableName;
	HashMap<String,String> htblColNameType;
	HashMap<String,String>htblColNameRefs;
	String strKeyColName;
	Properties properties;
	BTreeFactory btreeFactory;
	BufferManager bufferManager;
	Transaction transaction;
	
	public CreateTableCommand(String strTableName,
			HashMap<String,String> htblColNameType,
			HashMap<String,String>htblColNameRefs,
			String strKeyColName, CSVReader reader,BTreeFactory btreeFactory,
			Properties properties, BufferManager bufferManager){
this.strTableName=strTableName; 
this.htblColNameType=htblColNameType;
this.htblColNameRefs= htblColNameRefs; 
this.strKeyColName=strKeyColName;
this.reader=reader; 	
this.btreeFactory = btreeFactory;
this.properties = properties;
this.bufferManager = bufferManager;
}
	public CreateTableCommand(String strTableName,
							HashMap<String,String> htblColNameType,
							HashMap<String,String>htblColNameRefs,
							String strKeyColName, CSVReader reader,BTreeFactory btreeFactory,
							Properties properties, BufferManager bufferManager, Transaction transaction){
		this(strTableName,htblColNameType,htblColNameRefs,strKeyColName,reader,btreeFactory,properties,bufferManager);
		this.transaction = transaction;
		}
	public void execute() throws DBEngineException{
		validate();
		Set<String> columnName = htblColNameType.keySet();
		String [] columnNames = Utils.setToArray(columnName);   	
		for(int i =0; i<columnNames.length; i++){
			
			HashMap<String, String> metaData = new HashMap<String, String>();
			metaData.put("Table Name", strTableName);
			metaData.put("Column Name", columnNames[i]);
			metaData.put("Column Type", htblColNameType.get(columnNames[i]));
			if(columnNames[i].equals(strKeyColName)){
				metaData.put("Key", "True");
				metaData.put("Indexed", "True");
				this.btreeFactory.createTree(this.strTableName, this.strKeyColName);
			}
			else{
				metaData.put("Key", "False");
				metaData.put("Indexed", "False");
			}
			System.out.println(metaData);
			System.out.println(htblColNameRefs.get(columnNames[i]));
			metaData.put("References", htblColNameRefs.get(columnNames[i]));
			reader.appendToMetaDataFile(metaData);
		}
		bufferManager.createTable(transaction.getId(),strTableName,Utils.setToArray(properties.getData().get(this.strTableName).keySet()));	
	
	}
	private void validate() throws DBEngineException{
		
		if(properties.getData().get(this.strTableName) != null  ){
			throw new DBEngineException("The table already exists.");
		}
		
		if( htblColNameType.get(strKeyColName) == null ){
			throw new DBEngineException("This key doesn't name a column");
		}
		
		for(String column : htblColNameRefs.keySet()){
			if( !htblColNameRefs.get(column).equals("null") ){
				String[] s = htblColNameRefs.get(column).split("\\.");
				if(properties.getData().get(s[0]) == null || 
						properties.getData().get(s[0]).get(s[1]) == null){
					throw new DBEngineException("The referenced column doesn't exist.");
				}
			}
		}
		
	}
	@Override
	public List<HashMap<String, String>> getResult() {
		// TODO Auto-generated method stub
		return null;
	}

}


