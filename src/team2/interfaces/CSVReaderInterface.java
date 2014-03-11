package team2.interfaces;

import java.util.Hashtable;

import team2.exceptions.DBEngineException;
import team2.util.Properties;

public interface CSVReaderInterface {
	
	public Hashtable<String,String>[] loadPage(String tableName,int pageNumber) throws DBEngineException;
	
	public Hashtable<String,String> loadRow(String tableName,int pageNumber,int rowNumber) throws DBEngineException;
	
	public void createTablePage(String tableName,int newPageNumber) throws DBEngineException;
	
	public void appendToMetaDataFile(Hashtable<String,String> data) throws DBEngineException;
	
	/**
	 * @return Integer , the row at which it was added 
	 */
	public int appendToTable(String tableName,int pageNumber,Hashtable<String,String> data) throws DBEngineException;
	
	public void deleteRow(String tableName,int pageNumber,int rowNumber) throws DBEngineException; // Mark Row As Deleted
	
	public Hashtable<String,String> loadMetaDataFile();
	
	public void listenToMetaDataFileUpdates(MetaDataListener properties);
	
}
