package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.Command;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.btrees.BTreeAdopter;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;


public class DeleteCommand implements Command {
	String strTableName; 
	Hashtable<String, String> htblColNameValue;
	String strOperator;  
	CSVReader reader; 
	BTreeFactory btfactory;
	Properties properties;
	SelectCommand select;
	public DeleteCommand(String strTableName,Hashtable<String,String> htblColNameValue,
								String strOperator, CSVReader reader,Properties properties,BTreeFactory btfactory){
		this.strTableName=strTableName; 
		this.htblColNameValue=htblColNameValue; 
		this.strOperator=strOperator;
		this.reader=reader;
		this.properties = properties;
		this.btfactory = btfactory;
		select = new SelectCommand(this.btfactory,this.reader, this.properties, this.strTableName,
				this.htblColNameValue, this.strOperator);
	}
	
	public void execute() throws DBEngineException {
		select.execute();
		this.deleteFromTable();
		try {
			this.deleteFromTree();
		} catch (IOException e) {
			e.printStackTrace();
		} 
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
		ArrayList<String> pointers = select.getResultPointers();
		ArrayList< Hashtable<String, String> > results = select.getResults();
		for(int i=0; i<indexedColumns.size(); i++){
			BTreeAdopter adoptor= btfactory.getBtree(strTableName, indexedColumns.get(i));			
			for(int j=0; j<pointers.size(); j++){
				adoptor.delete(results.get(j).get(indexedColumns.get(i)), pointers.get(j));
			}
		}
	}

}
