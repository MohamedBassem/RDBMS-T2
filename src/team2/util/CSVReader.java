package team2.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import team2.exceptions.DBEngineException;
import team2.interfaces.CSVReaderInterface;
import team2.interfaces.MetaDataListener;

public class CSVReader implements CSVReaderInterface{

	private Map<String, Integer> numberOfPages;
	private final String numberOfPagesFile = "data/app/pages.ser";
	
	public CSVReader() {
		numberOfPages = loadPagesTable();
		
	}
	
	@Override
	public Hashtable<String, String>[] loadPage(String tableName, int pageNumber)
			throws DBEngineException {
		Hashtable<String, String>[] result = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(encodePageName(tableName, "" + pageNumber)));
			String[] columns = decodeRow(reader.readLine());
			ArrayList<Hashtable<String, String>> list = new ArrayList<Hashtable<String,String>>();
			String line;
			while ((line = reader.readLine()) != null) {
				String[] row = decodeRow(line);
				Hashtable<String, String> table = new Hashtable<String, String>();
				for (int i = 0; i < row.length; i++) {
					table.put(columns[i], row[i]);
				}
				list.add(table);
				result = (Hashtable<String, String>[]) list.toArray();
			}
		} catch (IOException e) {
			throw new DBEngineException("Bad file");
		}
		return result;
		
	}
	
	private String[] decodeRow(String row) {
		return row.split(",");
	}
	
	@Override
	public Hashtable<String, String> loadRow(String tableName, int pageNumber,
			int rowNumber) throws DBEngineException {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws DBEngineException, IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream("data/app/pages.ser");
	    ObjectInputStream ois = new ObjectInputStream(fis);
	    Map<String, Integer> m = (Map<String, Integer>) ois.readObject();
	    ois.close();
	    System.out.println(m);
	}
	

		
	

	/*
	 * The format of the file is tablename_pagenumber
	 */
	@Override
	public void createTablePage(String tableName, int newPageNumber)
			throws DBEngineException {
		if ( (new File(encodePageName(tableName, "" + newPageNumber)).exists())) {
			throw new DBEngineException("Page already exists");
		}
		try {
			FileWriter writer = new FileWriter(encodePageName(tableName, "" + newPageNumber));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DBEngineException("There was a problem while accessing the file");
		}
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
	public int appentToTable(String tableName, Hashtable<String, String> data) {
		// TODO Auto-generated method stub
		return 0;
	}

	private String encodePageName(String tableName, String pageNumber) {
		return String.format("data/tables/%s_%s", tableName, pageNumber);
	}
	
	private String[] decodePageName(String fileName) {
		return fileName.split("data/tables/")[1].split("_");
	}
	
	private void saveObject(Object obj, String filePath) throws IOException {
		FileOutputStream outputStream;
		outputStream = new FileOutputStream(filePath);
		ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
		objectStream.writeObject(obj);
	}
	
	private Object loadObject(String filePath) throws IOException, ClassNotFoundException {
		Object obj;
		FileInputStream inputStream = new FileInputStream(filePath);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		obj = objectStream.readObject();
		objectStream.close();
		return obj;
	}
	
	private void savePagesTable() throws IOException {
		saveObject(numberOfPages, numberOfPagesFile);
	}
	
	private Map<String, Integer> loadPagesTable() {
		Map<String, Integer> map;
		try {
			map = (Map<String, Integer>) loadObject(numberOfPagesFile);
		} catch (Exception ex) {
			map = new HashMap<String, Integer>();
		}
	    return map;

	}
	
	public Hashtable<String, String>[] loadMetaDataFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void listenToMetaDataFileUpdates(MetaDataListener properties) {
		// TODO Auto-generated method stub

	}

}
