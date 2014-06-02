package eg.edu.guc.dbms.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.MetaDataListener;


public class Properties implements MetaDataListener {
	
	private CSVReader reader;
	
	private ArrayList<Hashtable<String, String>>unparsedData;
	
	private Hashtable< String , Hashtable<String, Hashtable<String,String> >  > data;

	private int maximumPageSize;
	 
	private int bTreeN;
	
	
	public Properties(CSVReader reader){
		this.reader = reader;
		reader.listenToMetaDataFileUpdates(this);
		loadData();
		loadPropertiesFile();
		parseData();
	}
	
	public Hashtable<String, Hashtable<String, Hashtable<String, String>>> getData() {
		return data;
	}
	
	//TODO save it
	public void setData(
			Hashtable<String, Hashtable<String, Hashtable<String, String>>> data) {
		this.data = data;
		ArrayList<Hashtable<String,String>> toBeSaved = new ArrayList<Hashtable<String,String>>();
		for(String tblName : this.data.keySet()){
			for(String columnName : this.data.get(tblName).keySet()){
				Hashtable<String,String> row = new Hashtable<String,String>();
				Hashtable<String,String> col = this.data.get(tblName).get(columnName);
				row.put("Table Name",tblName);
				row.put("Column Name",columnName);
				row.put("Column Type", col.get("Column Type"));
				row.put("Key", col.get("Key"));
				row.put("Indexed", col.get("Indexed"));
				row.put("References", col.get("References"));
				toBeSaved.add(row);
			}
		}
		try {
			reader.saveMetaDataFile(toBeSaved);
		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}

	private void loadPropertiesFile() {
		java.util.Properties properties = new java.util.Properties();
		try {
		  properties.load(new FileInputStream("config/DBApp.properties"));
		} catch (IOException e) {
		  System.out.println("Error Loading Properties File");
		}
		String maxNum = properties.getProperty("MaximumRowsCountinPage");
		String bn = properties.getProperty("BPlusTreeN");
		
		this.maximumPageSize = Integer.parseInt(maxNum);
		this.bTreeN = Integer.parseInt(bn);
	}

	private void loadData(){
		try {
			this.unparsedData = reader.loadMetaDataFile();
		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void refresh( ArrayList<Hashtable<String, String>> data) {
		this.unparsedData = data;
		parseData();
	}
	
	private void parseData(){
		this.data = new Hashtable< String , Hashtable<String,Hashtable<String,String> >  >();
		
		for(Hashtable<String, String> row : unparsedData){
			if( !data.containsKey(row.get("Table Name"))){
				data.put( row.get("Table Name") , new Hashtable<String,Hashtable<String,String>>());
			}
			data.get(row.get("Table Name")).put(row.get("Column Name"), new Hashtable<String,String>());
			data.get(row.get("Table Name")).get(row.get("Column Name")).put("Column Type", row.get("Column Type"));
			data.get(row.get("Table Name")).get(row.get("Column Name")).put("Key", row.get("Key"));
			data.get(row.get("Table Name")).get(row.get("Column Name")).put("Indexed", row.get("Indexed"));
			data.get(row.get("Table Name")).get(row.get("Column Name")).put("References", row.get("References"));
		}
	}
	
	public ArrayList<String> getIndexedColumns(String tblName){
		ArrayList<String> columns = this.getTableColumns(tblName);
		ArrayList<String> ret = new ArrayList<String>();
		
		for(String column : columns){
			if ( this.isIndexed(tblName, column) )
				ret.add(column);
		}
		return ret;
	}
	
	public ArrayList<String> getForeignKeys(String tblName){
		ArrayList<String> columns = this.getTableColumns(tblName);
		ArrayList<String> ret = new ArrayList<String>();
		
		for(String column : columns){
			if ( this.isForeignKey(tblName, column) )
				ret.add(column);
		}
		return ret;
	}
	
	public ArrayList<String> getTableColumns(String tblName){
		Set<String> columns = data.get(tblName).keySet();
		ArrayList<String> ret = new ArrayList<String>();
		for(String str : columns){
			ret.add(str);
		}
		return ret;
	}
	
	public ArrayList<String> getTableNames(){
		Set<String> tables = data.keySet();
		ArrayList<String> ret = new ArrayList<String>();
		for(String str : tables){
			ret.add(str);
		}
		return ret;
	}
	
	
	public boolean isIndexed(String tblName, String colName){
		return data.get(tblName).get(colName).get("Indexed").equals("True");
	}
	
	public boolean isForeignKey(String tblName, String colName){
		return !data.get(tblName).get(colName).get("References").equals("null");
	}
	
	public boolean isPrimaryKey(String tblName,String colName){
		return data.get(tblName).get(colName).get("Key").equals("True");
	}
	
	public String getReferenceColumn(String tblName,String colName){
		String x = data.get(tblName).get(colName).get("References");
		if(x.equals("null")){
			return null;
		}else{
			return x;
		}
	}
	
	public String getTablePrimaryKey(String tblName){
		ArrayList<String> columns = this.getTableColumns(tblName);
		for(String column : columns){
			if ( this.isPrimaryKey(tblName, column) )
				return column;
		}
		return "";
	}
	
	/**
	 * @return String the referencedCol and null if it doesn't reference anything
	 */
	public String getColumnReference(String tblName,String colName){
		return data.get(tblName).get(colName).get("References");
	}
	
	public String getColumnType(String tblName,String colName){
		return data.get(tblName).get(colName).get("Column Type");
	}
	
	public int getMaximumPageSize(){
		return maximumPageSize;
	}
	
	public int getBTreeN(){
		return bTreeN;
	}
	
}
