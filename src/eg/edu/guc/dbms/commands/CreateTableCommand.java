package eg.edu.guc.dbms.commands;



import java.util.Hashtable;
import java.util.Set;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.Command;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.Utils;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;


public class CreateTableCommand implements Command {
	CSVReader reader;
	String strTableName;
	Hashtable<String,String> htblColNameType;
	Hashtable<String,String>htblColNameRefs;
	String strKeyColName;
	Properties properties;
	BTreeFactory btreeFactory;
	
	public CreateTableCommand(String strTableName,
							Hashtable<String,String> htblColNameType,
							Hashtable<String,String>htblColNameRefs,
							String strKeyColName, CSVReader reader,BTreeFactory btreeFactory,Properties properties){
		this.strTableName=strTableName; 
		this.htblColNameType=htblColNameType;
		this.htblColNameRefs= htblColNameRefs; 
		this.strKeyColName=strKeyColName;
		this.reader=reader; 	
		this.btreeFactory = btreeFactory;
		this.properties = properties;
	}
	public void execute() throws DBEngineException{
		validate();
		Set<String> columnName = htblColNameType.keySet();
		String [] columnNames = Utils.setToArray(columnName);   	
		for(int i =0; i<columnNames.length; i++){
			
			Hashtable<String, String> metaData = new Hashtable<String, String>();
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
			metaData.put("References", htblColNameRefs.get(columnNames[i]));
			reader.appendToMetaDataFile(metaData);
		}
		reader.createTablePage(strTableName,0,Utils.setToArray(properties.getData().get(this.strTableName).keySet()));	
	
	}
	private void validate() throws DBEngineException{
		
		if( properties.getData().get(this.strTableName) != null  ){
			throw new DBEngineException("The table already exists.");
		}
		
		if( htblColNameType.get(strKeyColName) == null ){
			throw new DBEngineException("This key doesn't name a column");
		}
		
		for(String column : htblColNameRefs.keySet()){
			if( !htblColNameRefs.get(column).equals("null") ){
				String[] s = htblColNameRefs.get(column).split("\\.");
				if( properties.getData().get(s[0]) == null || 
						properties.getData().get(s[0]).get(s[1]) == null){
					throw new DBEngineException("The referenced column doesn't exist.");
				}
			}
		}
		
	}

}


