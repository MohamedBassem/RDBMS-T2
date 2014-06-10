package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import eg.edu.guc.dbms.components.BufferManager;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.helpers.Page;
import eg.edu.guc.dbms.helpers.Tuple;
import eg.edu.guc.dbms.interfaces.Command;
import eg.edu.guc.dbms.transactions.Transaction;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.btrees.BTreeAdopter;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;


public class SelectCommand implements Command {
	BTreeFactory btfactory;
	CSVReader reader;
	String tableName;
	HashMap<String,String> htblColNameValue;
	String strOperator;
	Properties properties;
	BufferManager bufferManager;
	long transactionId;
	
	// The final arraylist of objects
	ArrayList< HashMap<String, String> > results;
	
	// The arraylist of results pointer
	ArrayList< String > resultPointers;
	
	// The partial results before merging using OR or AND
	ArrayList< ArrayList<String> > partialRecords;
	
	
	
	public SelectCommand(BTreeFactory btfactory,CSVReader reader,Properties properties,BufferManager bufferManager, String tableName,
			HashMap<String, String> htblColNameValue, String strOperator, long transactionId) {
		this.btfactory = btfactory;
		this.reader = reader;
		this.tableName = tableName;
		this.htblColNameValue = htblColNameValue;
		this.strOperator = strOperator;
		this.properties = properties;
		this.bufferManager = bufferManager;
		this.transactionId = transactionId;
		this.results = new ArrayList<HashMap<String, String>>();
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
//		System.out.println(properties.getData());
		if(properties.getData().get(tableName) == null){
			throw new DBEngineException("This table doesn't exist");
		}else if(htblColNameValue == null && strOperator == null){
			selectAll();
		}else{
			validate();
			normalSelect();
			mergeResults();
			convertPointers();
		}
//		System.out.println("3abod" + results);
		
	}

	private void validate() throws DBEngineException {
		
		if(strOperator != null && !strOperator.equals("AND") && !strOperator.equals("OR")){
			throw new DBEngineException("Unknown Opertator");
		}
		
		HashMap<String, HashMap<String, String>> table = properties.getData().get(tableName);
		
		Set<String> keys = this.htblColNameValue.keySet();
		
		for(String key: keys){
			if(table.get(key) == null){
				throw new DBEngineException("Wrong Column Name");
			}
		}
		
	}

	private void normalSelect() throws DBEngineException {
		Set<String> keys = this.htblColNameValue.keySet();
		
		this.partialRecords = new ArrayList< ArrayList<String> >();
		
		for(String key: keys){
			
			if(properties.isIndexed(this.tableName, key)){
				
				BTreeAdopter tree = null;
				try {
					tree = btfactory.getBtree(this.tableName, key);
					
				} catch (DBEngineException e) {}
				try {
					partialRecords.add((ArrayList<String>) tree.find(htblColNameValue.get(key)));
				} catch (IOException e) {}
			}else{
				
				ArrayList<String> partialRecord = new ArrayList<String>();
				int tablePages = bufferManager.getLastPageIndex(this.tableName);
				for(int i=0;i<=tablePages;i++){
					Page res = bufferManager.read(transactionId, tableName, i, false);
					for(int j=0;j<res.size();j++){
						if(res.get(j) != null && res.get(j).get(key).equals(htblColNameValue.get(key))){
							String pointer = this.tableName + " " + i + " " + j;
							partialRecord.add(pointer);
						}
					}
				}
				partialRecords.add(partialRecord);
			}
		}
		
	}
	
	private void mergeResults() throws DBEngineException {
		this.resultPointers = new ArrayList<String>();
		if(partialRecords.size() == 0){
			// DO NOTHING
		}else{
			this.resultPointers = partialRecords.get(0);
			System.out.println(partialRecords);
			for(int i=1;i<partialRecords.size();i++){
				if(strOperator.equals("AND")){
					this.resultPointers = intersect(this.resultPointers,partialRecords.get(i));
				}else{
					this.resultPointers = union(this.resultPointers,partialRecords.get(i));
				}
			}
		}
		
	}

	private void selectAll() throws DBEngineException {
		
		this.resultPointers = new ArrayList<String>();
//		this.results = new ArrayList< HashMap<String, String> >();
		
		int tablePages = bufferManager.getLastPageIndex(this.tableName);
		
		for(int i=0;i<=tablePages;i++){
			Page res = bufferManager.read(transactionId, tableName, i, false);
			for(int j=0;j<res.size();j++){
				if(res.get(j) == null){ // Deleted Record
					continue;
				}else{
					String pointer = this.tableName + " " + i + " " + j;
					resultPointers.add(pointer);
					results.add(res.get(j));
				}
				
			}
		}
		
	}

	private void convertPointers() throws DBEngineException {
//		this.results = new ArrayList< HashMap<String, String> >();
		for (String result : this.resultPointers ) {
			String[] row = result.split(" ");
			Page page = bufferManager.read(transactionId, row[0] , Integer.parseInt(row[1]) , false );
			Tuple record = page.get(Integer.parseInt(row[2]));
			this.results.add(record);
		}
	}
	
	public ArrayList<String> getResultPointers(){
		return this.resultPointers;
	}
	

	@Override
	public List<HashMap<String, String>> getResult() {
		return this.results;
	}

}
