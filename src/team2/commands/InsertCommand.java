package team2.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import team2.exceptions.DBEngineException;
import team2.interfaces.Command;
import team2.util.CSVReader;
import team2.util.Properties;
import team2.util.btrees.BTreeAdopter;
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
		int lastPage = reader.getLastPageIndex(tableName);
		int lastRow = reader.getLastRow(tableName, lastPage);
		
		if(lastRow == properties.getMaximumPageSize()) {
			lastPage++;
			try {
				reader.createTablePage(tableName, lastPage);
			} catch (DBEngineException e) {
				e.printStackTrace();
			}
		}		
		
		try {
			int row = reader.appendToTable(tableName, lastPage, htblColNameValue);
			ArrayList<String> indexedColumns = properties.getIndexedColumns(tableName);
			
			for (String column : indexedColumns) {
				String pointer = tableName + " " + lastPage + " " + row;
				BTreeAdopter tree;
				try {
					tree = btfactory.getBtree(tableName, column);
					tree.insert(htblColNameValue.get(column), pointer, false);
				} catch (DBEngineException | IOException e) {
					e.printStackTrace();
				}
			}
		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}

}
