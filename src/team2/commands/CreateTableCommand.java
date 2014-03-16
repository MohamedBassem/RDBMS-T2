package team2.commands;



import java.util.Hashtable;
import java.util.Set;
import team2.exceptions.DBEngineException;
import team2.interfaces.Command;
import team2.util.CSVReader;

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
	public void execute() throws DBEngineException{
	Hashtable<String, String> metaData = new Hashtable<String, String>();
	Set<String> columnName = htblColNameType.keySet(); 
	String [] columnNames = (String[]) columnName.toArray();   	
	for(int i =0; i<columnNames.length; i++){
		String ColumnDetails=columnNames[i]+ ", " + htblColNameType.get(columnNames[i]) + ", ";
			if(columnNames[i].equals(strKeyColName)){
				ColumnDetails+="True, True, ";	
			//Btree newBtree = new Btree(); 
			//newBtree.create fix me!!
			}
			else{
				ColumnDetails+="False, False, ";
			}
		ColumnDetails+=htblColNameRefs.get(columnNames[i]);  	
		metaData.put(strTableName, ColumnDetails);	
		reader.appendToMetaDataFile(metaData);
		}
		reader.createTablePage(strTableName,0);	

	}

	}


