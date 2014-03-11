package team2.util;

import java.util.ArrayList;
import java.util.Hashtable;

import team2.interfaces.MetaDataListener;

public class Properties implements MetaDataListener {
	
	private CSVReader reader;
	
	private Hashtable<String, String>[] unparsedData;
	
	private Hashtable< String , Hashtable<String, Hashtable<String,String> >  > data; 
	
	
	public Properties(CSVReader reader){
		this.reader = reader;
		loadData();
		parseData();
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
		// TODO
		return null;
	}
	
	public ArrayList<String> getTableColumns(String tblName){
		// TODO
		return null;
	}
	
	public ArrayList<String> getTableNames(){
		// TODO
		return null;
	}
	
	public boolean isIndexed(String tblName, String colName){
		// TODO
		return false;
	}
	
	public boolean isPrimaryKey(String tblName,String colName){
		// TODO
		return false;
	}
	
	public String getTablePrimaryKey(String tblName){
		// TODO
		return "";
	}
	
	/**
	 * @return String the referncedCol and null if it doesn't reference anything
	 */
	public String getColumnRefernce(String tblName){
		// TODO
		return null;
	}
	
	public String getColumnType(String tblName,String colName){
		// TODO
		return null;
	}
	
	public int getMaximumPageSize(){
		// TODO
		return 0;
	}
	
	public int getBTreeN(){
		// TODO
		return 0;
	}
	
}
