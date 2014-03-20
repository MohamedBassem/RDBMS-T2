package team2.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import javax.swing.text.TabableView;

import sun.org.mozilla.javascript.xml.XMLLib.Factory;
import team2.exceptions.DBEngineException;
import team2.interfaces.Command;
import team2.util.CSVReader;
import team2.util.Properties;
import team2.util.btrees.BTreeAdopter;
import team2.util.btrees.BTreeFactory;

public class InsertCommand implements Command {
	BTreeFactory btFactory;
	CSVReader reader;
	String tableName;
	Properties properties;
	Hashtable<String,String> htblColNameValue;
	
	
public InsertCommand(BTreeFactory btFactory, CSVReader reader, String tableName, 
		Properties properties, Hashtable<String,String> htblColNameValue) {
	this.btFactory = btFactory;
	this.reader = reader;
	this.tableName = tableName;
	this.properties = properties;
	this.htblColNameValue = htblColNameValue;
}
	@Override
	public void execute() throws DBEngineException, IOException {
		if(properties.getData().get(tableName) == null)
			throw new DBEngineException("Table name is wrong or it doesn't exist.");
		
			BTreeAdopter tree;
			Set<String> columns = htblColNameValue.keySet();
			for (String column : columns) {
				if(properties.getData().get(tableName).get(column) == null)
					throw new DBEngineException("Column name is wrong or it doesn't exist.");
				if(properties.isPrimaryKey(tableName, column)) {
					if(htblColNameValue.get(column) == null)
						throw new DBEngineException("The primary key can't be null.");
					tree = btFactory.getBtree(tableName, column);
							if(tree.find(htblColNameValue.get(column)) != null)
								throw new DBEngineException("Th primary key you're trying to insert is not unique.");
						
					
				}
				if(properties.isForeignKey(tableName, column)) {
					if(htblColNameValue.get(column) == null)
						throw new DBEngineException("The primary key can't be null.");
					String reference = properties.getReferenceColumn(tableName, column);
					String[] references = reference.split(".");
					BTreeAdopter refTree = btFactory.getBtree(references[0], references[1]);
					if(refTree.find(htblColNameValue.get(column)) == null)
						throw new DBEngineException("The foreign key you're trying to " +
								"insert doesn't reference anything."); 	
				}
			}
			
			int lastPage = reader.getLastPageIndex(tableName);
			int lastRow = reader.getLastRow(tableName, lastPage);
			
			if(lastRow == properties.getMaximumPageSize()) {
				lastPage++;
				
					reader.createTablePage(tableName, lastPage);		
			}	
			int row = reader.appendToTable(tableName, lastPage, htblColNameValue);
			ArrayList<String> indexedColumns = properties.getIndexedColumns(tableName);
			
			for (String column : indexedColumns) {
				String pointer = tableName + " " + lastPage + " " + row;
					tree = btFactory.getBtree(tableName, column);
					tree.insert(htblColNameValue.get(column), pointer);			
			}
		
}
}