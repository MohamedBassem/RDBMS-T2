package team2.util;

import java.util.Hashtable;

import team2.exceptions.DBEngineException;
import team2.interfaces.CSVReaderInterface;
import team2.interfaces.MetaDataListener;

public class CSVReader implements CSVReaderInterface{

	@Override
	public Hashtable<String, String>[] loadPage(String tableName, int pageNumber)
			throws DBEngineException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Hashtable<String, String> loadRow(String tableName, int pageNumber,
			int rowNumber) throws DBEngineException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void createTablePage(String tableName, int newPageNumber)
			throws DBEngineException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void appendToMetaDataFile(Hashtable<String, String> data)
			throws DBEngineException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int appendToTable(String tableName, int pageNumber,
			Hashtable<String, String> data) throws DBEngineException {
		// TODO Auto-generated method stub
		return -1;
	}

	@Override
	public void deleteRow(String tableName, int pageNumber, int rowNumber)
			throws DBEngineException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Hashtable<String, String> loadMetaDataFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void listenToMetaDataFileUpdates(MetaDataListener properties) {
		// TODO Auto-generated method stub
	}

}
