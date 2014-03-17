package team2.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

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
		try {
		if(properties.getData().get(tableName) == null)
			throw new DBEngineException("Table name is wrong or it doesn't exist.");
		
		int lastPage = reader.getLastPageIndex(tableName);
		int lastRow = reader.getLastRow(tableName, lastPage);
		
		if(lastRow == properties.getMaximumPageSize()) {
			lastPage++;
			
				reader.createTablePage(tableName, lastPage);		
		}	
		
			Set<String> columns = htblColNameValue.keySet();
			for (String column : columns) {
				if(properties.getData().get(tableName).get(column) == null)
					throw new DBEngineException("Column name is wrong or it doesn't exist.");
			}
			
			int row = reader.appendToTable(tableName, lastPage, htblColNameValue);
			ArrayList<String> indexedColumns = properties.getIndexedColumns(tableName);
			
			for (String column : indexedColumns) {
				String pointer = tableName + " " + lastPage + " " + row;
				BTreeAdopter tree;
				
					tree = btfactory.getBtree(tableName, column);
					if(properties.isPrimaryKey(tableName, column)) {
						if(htblColNameValue.get(column) == null)
							throw new DBEngineException("The primary key can't be null.");
						if(tree.find(htblColNameValue.get(column)) != null)
							throw new DBEngineException("Th primary key you're trying to insert is not unique.");
					}
					
					tree.insert(htblColNameValue.get(column), pointer);
			}
		} catch (DBEngineException e) {
			e.printStackTrace();		
	} catch (IOException e) {
		e.printStackTrace();
	}
	}
}



