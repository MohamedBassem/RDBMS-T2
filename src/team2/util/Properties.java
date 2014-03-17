package team2.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

import team2.interfaces.MetaDataListener;

public class Properties implements MetaDataListener {
	
	private CSVReader reader;
	
	private Hashtable<String, String>[] unparsedData;
	
	private Hashtable< String , Hashtable<String, Hashtable<String,String> >  > data;
	
	private int maximumPageSize;
	
	private int bTreeN;
	
	
	public Properties(CSVReader reader){
		this.reader = reader;
		loadData();
		loadPropertiesFile();
		parseData();
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
		this.unparsedData = reader.loadMetaDataFile();
	}
	
	@Override
	public void refresh(Hashtable<String, String>[] data) {
		this.unparsedData = data;
		parseData();
	}
	
	private void parseData(){
		this.data = new Hashtable< String , Hashtable<String,Hashtable<String,String> >  >();
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
	 * @return String the referncedCol and null if it doesn't reference anything
	 */
	public String getColumnRefernce(String tblName,String colName){
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
