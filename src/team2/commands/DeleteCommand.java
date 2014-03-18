package team2.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import team2.exceptions.DBEngineException;
import team2.interfaces.Command;
import team2.util.CSVReader;
import team2.util.btrees.BTreeAdopter;
import team2.util.btrees.BTreeFactory;
import team2.util.Properties;

public class DeleteCommand implements Command {
	String strTableName; 
	Hashtable<String, String> htblColNameValue;
	String strOperator;  
	CSVReader reader; 
	BTreeFactory btfactory;
	Properties properties;
	SelectCommand select = new SelectCommand(this.btfactory,this.reader, this.properties, this.strTableName,
			this.htblColNameValue, this.strOperator);
	public DeleteCommand(String strTableName,Hashtable<String,String> htblColNameValue,
								String strOperator, CSVReader reader){
	this.strTableName=strTableName; 
	this.htblColNameValue=htblColNameValue; 
	this.strOperator=strOperator;
	this.reader=reader; 
	}
	public void execute() throws DBEngineException, IOException {
		select.execute();
		this.deleteFromTable();
		this.deleteFromTree(); 
	}
	
	public void deleteFromTable() throws DBEngineException{
		ArrayList<String> pointers = select.getResultPointers(); 
		for(int i =0; i<pointers.size(); i++){
		String [] x = ((String) pointers.get(i)).split(" ");
		int pageNumber = Integer.parseInt(x[1]);
		int rowNumber= Integer.parseInt(x[2]);
		reader.deleteRow(this.strTableName,pageNumber,rowNumber); 
		}
	}
	public void deleteFromTree() throws DBEngineException, IOException{
		ArrayList<String> indexedColumns = properties.getIndexedColumns(strTableName); 
		for(int i=0; i<indexedColumns.size(); i++){
			BTreeAdopter adoptor= btfactory.getBtree(strTableName, indexedColumns.get(i));
			String key = htblColNameValue.get(indexedColumns.get(i));
			ArrayList<String> pointers = adoptor.find(key);
			for(int j=0; j<pointers.size(); j++){
				boolean x = adoptor.delete(key, pointers.get(i));
			}
		}
	}

}
