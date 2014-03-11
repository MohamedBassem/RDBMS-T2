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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import team2.exceptions.DBEngineException;
import team2.interfaces.CSVReaderInterface;
import team2.interfaces.MetaDataListener;

public class CSVReader implements CSVReaderInterface{

	private Map<String, Integer> numberOfPages;
	private Map<String, Integer> numberOfRows;
	private final String numberOfPagesFile = "data/app/pages.ser";
	private final String numberOfRowsFile = "data/app/rows.ser";
	private final String metadataFile = "data/tables/meta.txt";
	
	public CSVReader() {
		numberOfPages	= loadPagesTable();
		numberOfRows	= loadRowsTable(); 
		
	}
	
	@Override
	public Hashtable<String, String>[] loadPage(String tableName, int pageNumber)
			throws DBEngineException {
		Hashtable<String, String>[] result = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(encodePageName(tableName, pageNumber)));
			String[] columns = decodeRow(reader.readLine());
			ArrayList<Hashtable<String, String>> list = new ArrayList<Hashtable<String,String>>();
			String line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				if (line.equals("")) {
					continue;
				}
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
	
	
	@Override
	public Hashtable<String, String> loadRow(String tableName, int pageNumber,
			int rowNumber) throws DBEngineException {
		Hashtable<String, String> result = new Hashtable<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(encodePageName(tableName, pageNumber)));
			String[] columns = decodeRow(reader.readLine());
			String line = reader.readLine();
			
			for (int i = 0; i <= rowNumber; i++) {
				line = reader.readLine();
				if (line.equals("")) {
					i--;
				}
			}
			String[] row = decodeRow(line);
			for (int i = 0; i < row.length; i++) {
				result.put(columns[i], row[i]);
			}
		} catch (IOException e) {
			throw new DBEngineException("Bad file");
		}
		return result;
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
		if ( (new File(encodePageName(tableName,  newPageNumber)).exists())) {
			throw new DBEngineException("Page already exists");
		}
		try {
			FileWriter writer = new FileWriter(encodePageName(tableName, newPageNumber));
			if (numberOfPages.containsKey(tableName)) {
				numberOfPages.put(tableName, numberOfPages.get(tableName) + 1);
			}
			numberOfRows.put(encodePageName(tableName, newPageNumber), 0);
			savePagesTable();
			saveRowsTable();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new DBEngineException("There was a problem while accessing the file");
		}
	}

	
	//TODO implement this
	@Override
	public void appendToMetaDataFile(Hashtable<String, String> data)
			throws DBEngineException {
		
		try {
			PrintWriter writer = new PrintWriter( new FileWriter(metadataFile), true);
			writer.println(encodeRow(data));
			
		} catch (IOException e) {
			throw new DBEngineException("There was a problem while accessing the file");
		}
		
	}

	@Override
	public int appendToTable(String tableName, int pageNumber,
			Hashtable<String, String> data) throws DBEngineException {
		int lastRow = -1;
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(encodePageName(tableName, pageNumber), true));
			writer.println(encodeRow(data));
			writer.flush();
			numberOfRows.put(encodePageName(tableName, pageNumber), numberOfRows.get(encodePageName(tableName, pageNumber) + 1));
			lastRow = getLastRow(tableName, pageNumber);
		} catch (IOException e) {
			throw new DBEngineException("There was a problem writing into the file");
		}
		return lastRow;
	}

	@Override
	public void deleteRow(String tableName, int pageNumber, int rowNumber)
			throws DBEngineException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int appentToTable(String tableName, Hashtable<String, String> data) throws DBEngineException {
		int lastPage = getLastPageIndex(tableName);
		return appendToTable(tableName, lastPage, data);
	}

	public Hashtable<String, String>[] loadMetaDataFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void listenToMetaDataFileUpdates(MetaDataListener properties) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLastPageIndex(String tableName) {
		return numberOfPages.get(tableName) - 1;
	}

	@Override
	public int getLastRow(String tableName, int pageNumber) {
		return numberOfRows.get(encodePageName(tableName, pageNumber)) - 1;
	}
	
	private String encodeRow(Hashtable<String, String> data) {
		StringBuffer buffer = new StringBuffer("");
		for (String key : data.keySet()) {
			buffer.append(data.get(key));
			buffer.append(",");
		}
		return buffer.substring(0, buffer.length() - 1).toString();
	}
	
	private String[] decodeRow(String row) {
		return row.split(",");
	}
	
	private String encodePageName(String tableName, int pageNumber) {
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
	
	private void saveRowsTable() throws IOException {
		saveObject(numberOfRows, numberOfRowsFile);
	}
	
	private Map<String, Integer> loadRowsTable() {
		Map<String, Integer> map;
		try {
			map = (Map<String, Integer>) loadObject(numberOfRowsFile);
		} catch (Exception ex) {
			map = new HashMap<String, Integer>();
		}
	    return map;
	}

}
