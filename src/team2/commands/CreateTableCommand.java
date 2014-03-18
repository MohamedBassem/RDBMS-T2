package team2.commands;



import java.io.IOException;
import java.util.Hashtable;
import java.util.Set;

import team2.exceptions.DBEngineException;
import team2.interfaces.Command;
import team2.util.CSVReader;
import team2.util.btrees.BTreeAdopter;
import team2.util.btrees.BTreeFactory;

public class CreateTableCommand implements Command {
	CSVReader reader;
	String strTableName;
	Hashtable<String,String> htblColNameType;
	Hashtable<String,String>htblColNameRefs;
	String strKeyColName; 
	
	public CreateTableCommand(String strTableName,
							Hashtable<String,String> htblColNameType,
							Hashtable<String,String>htblColNameRefs,
							String strKeyColName, CSVReader reader){
	this.strTableName=strTableName; 
	this.htblColNameType=htblColNameType;
	this.htblColNameRefs= htblColNameRefs; 
	this.strKeyColName=strKeyColName;
	this.reader=reader; 	
	}
	public void execute() throws DBEngineException, IOException{
	Set<String> columnName = htblColNameType.keySet(); 
	String [] columnNames = (String[]) columnName.toArray();   	
	for(int i =0; i<columnNames.length; i++){
		Hashtable<String, String> metaData = new Hashtable<String, String>();
		metaData.put("Table Name", strTableName);
		metaData.put("Column Name", columnNames[i]);
		metaData.put("Column Type", htblColNameType.get(columnNames[i]));
			if(columnNames[i].equals(strKeyColName)){
				metaData.put("Key", "True");
				metaData.put("Indexed", "True");
			//BTreeFactory factory = new BTreeFactory("Tree");
			//factory.createBtree();
			
			}
			else{
				metaData.put("Key", "False");
				metaData.put("Indexed", "False");
			}
			metaData.put("References", htblColNameRefs.get(columnNames[i]));
			reader.appendToMetaDataFile(metaData);
		}
		reader.createTablePage(strTableName,0);	

	}

	}


