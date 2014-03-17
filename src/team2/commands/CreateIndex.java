package team2.commands;

import java.util.ArrayList;
import java.util.Hashtable;

import team2.exceptions.DBEngineException;
import team2.interfaces.Command;
import team2.util.CSVReader;
import team2.util.Properties;
import team2.util.btrees.BTreeAdopter;
import team2.util.btrees.BTreeFactory;

public class CreateIndex implements Command {
	String tableName;
	String columnName;
	Properties properties;
	CSVReader reader;
	BTreeFactory factory;
	
	public CreateIndex(String tableName, String columnName,
			Properties properties, CSVReader reader, BTreeFactory factory) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.properties = properties;
		this.reader = reader;
		this.factory = factory;
	}

	@Override
	public void execute() throws DBEngineException {
		Hashtable<String, Hashtable<String, Hashtable<String,String>>> data 
		= properties.getData();
		Hashtable<String, Hashtable<String, String>> table = data.get(tableName);
		
		if(table == null) {
			System.out.println("Table name is wrong or it doesn't exist.");
			return;
		}
		
		Hashtable<String, String> column = table.get(columnName);
		if(column == null) {
			System.out.println("Column name is wrong or it doesn't exist.");
			return;
		}
		column.put("Indexed", "True");
		properties.setData(data);
		BTreeAdopter tree = factory.getBtree(tableName, columnName);
		
		if(tree != null) {
			System.out.println("This column is already indexed.");
			return;
		}
		
		tree = factory.createTree(tableName, columnName);
		
		SelectCommand select = new SelectCommand(factory, reader, properties,
				tableName, null, null);
		select.execute();
		ArrayList<Hashtable<String, String>> rows = select.getResults();
		ArrayList<String> pointers = select.getResultPointers();	
		
		for(int i = 0; i < rows.size(); i++) {
			String value = rows.get(i).get(columnName);
			String pointer = pointers.get(i);
			tree.insert(value, pointer, false);
		}
		}

}
