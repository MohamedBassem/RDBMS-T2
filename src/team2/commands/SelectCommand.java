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

public class SelectCommand implements Command {
	BTreeFactory btfactory;
	CSVReader reader;
	String tableName;
	Hashtable<String,String> htblColNameValue;
	String strOperator;
	Properties properties;
	
	ArrayList< Hashtable<String, String> > results;
	
	
	public SelectCommand(BTreeFactory btfactory,CSVReader reader,Properties properties,String tableName,
			Hashtable<String, String> htblColNameValue, String strOperator) {
		this.btfactory = btfactory;
		this.reader = reader;
		this.tableName = tableName;
		this.htblColNameValue = htblColNameValue;
		this.strOperator = strOperator;
		this.properties = properties;
	}
	
	private ArrayList<String> intersect(ArrayList<String> resultsPointer,
			ArrayList<String> arrayList) {
		
		ArrayList<String> ret = new ArrayList<String>();
		
		for (String element : resultsPointer) {
			if(arrayList.contains(element)){
				ret.add(element);
			}
		}
		
		return ret;
	}

	private ArrayList<String> union(ArrayList<String> resultsPointer,
			ArrayList<String> arrayList) {
		
		ArrayList<String> ret = resultsPointer;
		
		for (String element : arrayList) {
			if(!ret.contains(element)){
				ret.add(element);
			}
		}
		
		return ret;
		
	}
	
	@Override
	public void execute() throws DBEngineException {
		
		Set<String> keys = this.htblColNameValue.keySet();
		
		ArrayList< ArrayList<String> > partialRecords = new ArrayList< ArrayList<String> >();
		
		for(String key: keys){
			if(properties.isIndexed(this.tableName, key)){
				BTreeAdopter tree = null;
				try {
					tree = btfactory.getBtree(this.tableName, key);
				} catch (DBEngineException e) {}
				
				try {
					partialRecords.add((ArrayList<String>) tree.find(htblColNameValue.get(key)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				ArrayList<String> partialRecord = new ArrayList<String>();
				int tablePages = reader.getLastPageIndex(this.tableName);
				for(int i=0;i<=tablePages;i++){
					Hashtable<String,String>[] res = reader.loadPage(tableName, i);
					for(int j=0;j<res.length;j++){
						if(res[j].get(key).equals(htblColNameValue.get(key))){
							String pointer = this.tableName + " " + i + " " + j;
							partialRecord.add(pointer);
						}
					}
				}
				partialRecords.add(partialRecord);
			}
		}
		
		ArrayList<String> resultsPointer = new ArrayList<String>();
		
		if(partialRecords.size() == 0){
			// DO NOTHING
		}else if( !strOperator.equals("AND") && !strOperator.equals("OR")){
			throw new DBEngineException();
		}else{
			resultsPointer = partialRecords.get(0);
			for(int i=1;i<partialRecords.size();i++){
				if(strOperator.equals("AND")){
					resultsPointer = intersect(resultsPointer,partialRecords.get(i));
				}else{
					resultsPointer = union(resultsPointer,partialRecords.get(i));
				}
			}
		}
		
		this.results = new ArrayList< Hashtable<String, String> >();
		
		for (String result : resultsPointer) {
			String[] row = result.split(" ");
			Hashtable<String, String> record = reader.loadRow(row[0] , Integer.parseInt(row[1]) , Integer.parseInt(row[2]) );
			this.results.add(record);
		}
		
	}
	
	public ArrayList<Hashtable<String, String> > getResults(){
		return this.results;
	}

}
