package team2.interfaces;

import java.util.ArrayList;
import java.util.Hashtable;

import team2.exceptions.DBEngineException;
import team2.util.Properties;

public interface CSVReaderInterface {
	
	public ArrayList<Hashtable<String, String>> loadPage(String tableName,int pageNumber) throws DBEngineException;
	
	public Hashtable<String,String> loadRow(String tableName,int pageNumber,int rowNumber) throws DBEngineException;
	
	public void createTablePage(String tableName,int newPageNumber, String[] columns) throws DBEngineException;
	
	public void appendToMetaDataFile(Hashtable<String,String> data) throws DBEngineException;
	
	/**
	 * @return Integer , the row at which it was added 
	 */
	public int appendToTable(String tableName,int pageNumber,Hashtable<String,String> data) throws DBEngineException;
	
	public int appentToTable(String tableName, Hashtable<String,String> data) throws DBEngineException;
	
	public void deleteRow(String tableName,int pageNumber,int rowNumber) throws DBEngineException; // Mark Row As Deleted
	
	public ArrayList<Hashtable<String, String>> loadMetaDataFile() throws DBEngineException;
	
	public void listenToMetaDataFileUpdates(MetaDataListener properties);
	
	public int getLastPageIndex(String tableName);
	
	public int getLastRow(String tableName, int pageNumber);
	
}
