package team2.commands;

import java.util.ArrayList;
import java.util.Hashtable;

import team2.interfaces.Command;
import team2.util.CSVReader;
import team2.util.Properties;
import team2.util.btrees.BTree;
import team2.util.btrees.BTreeFactory;

public class InsertCommand implements Command {
	BTreeFactory btfactory;
	CSVReader reader;
	String tableName;
	Properties properties;
	Hashtable<String,String> htblColNameValue;
	
	
public InsertCommand(BTreeFactory btfactory, CSVReader reader, String tableName, 
		Properties properties, Hashtable<String,String> htblColNameValue) {
	this.btfactory = btfactory;
	this.reader = reader;
	this.tableName = tableName;
	this.properties = properties;
	this.htblColNameValue = htblColNameValue;
}
	@Override
	public void execute() {
		int lastPage = reader.lastPage(tableName);
		int lastRow = reader.getLastRow(tableName, lastPage);
		
		if(lastRow == properties.getMaximumPageSize()) {
			lastPage++;
			reader.createTablePage(tableName, lastPage);
		}
		
		int row = reader.appendToTable(tableName, lastPage, htblColNameValue);
		ArrayList<String> indexedColumns = properties.getIndexedColumns(tableName);
		
		for (String column : indexedColumns) {
			String pointer = tableName + " " + lastPage + " " + row;
			BTree tree = btfactory.getBtree(tableName, column);
			tree.insert(htblColNameValue.get(column), pointer, false);
			
		}
	}

}
