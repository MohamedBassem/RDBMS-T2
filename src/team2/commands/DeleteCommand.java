package team2.commands;

import java.util.ArrayList;
import java.util.Hashtable;

import team2.exceptions.DBEngineException;
import team2.interfaces.Command;
import team2.util.CSVReader;
import team2.util.btrees.BTreeFactory;

public class DeleteCommand implements Command {
	String strTableName; 
	Hashtable<String, String> htblColNameValue;
	String strOperator;  
	CSVReader reader; 
	BTreeFactory btfactory;
	SelectCommand select = new SelectCommand(this.btfactory,this.reader,this.strTableName,
			this.htblColNameValue, this.strOperator);
	public DeleteCommand(String strTableName,Hashtable<String,String> htblColNameValue,
								String strOperator, CSVReader reader){
	this.strTableName=strTableName; 
	this.htblColNameValue=htblColNameValue; 
	this.strOperator=strOperator;
	this.reader=reader; 
	}
	public void execute() throws DBEngineException {
		select.execute();
		ArrayList< Hashtable<String, String> > results = select.getResults();
		this.deleteFromTable();
		this.deleteFromTree(); 
	}
	
	public void deleteFromTable(){
		//loop through tables from hashmap and get pointer and delete.	
		//reader.deleteRow(this.strTableName,int pageNumber,/*select.getPointer()*/ 1); 
		}
	public void deleteFromTree(){
		
	}

}
