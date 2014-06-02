package eg.edu.guc.dbms.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.CSVReaderInterface;
import eg.edu.guc.dbms.interfaces.MetaDataListener;


public class CSVReader implements CSVReaderInterface{

	private List<MetaDataListener> metadataObservers;
	private Map<String, Integer> numberOfPages;
	private Map<String, Integer> numberOfRows;
	private final String numberOfPagesFile = "data/pages.ser";
	private final String numberOfRowsFile = "data/rows.ser";
	private final String columnOrderFilePath = "data/columns.csv";
	private final String metadataFile = "data/meta.csv";
	private final String tmpFilePath = "data/tmp";
	private final String[] metadataColumnOrder_ = {"Table Name", "Column Name", "Column Type", "Key", "Indexed", "References"};
	private final ArrayList<String> metadataColumnOrder;
	private Map<String, ArrayList<String>> columnsOrder;
	
	public CSVReader() {
		numberOfPages	= loadPagesTable();
		numberOfRows	= loadRowsTable();
		loadColumnsOrder();
		metadataColumnOrder = new ArrayList<String>();
		for(String str : metadataColumnOrder_){
			metadataColumnOrder.add(str);
		}
		
		metadataObservers = new ArrayList<MetaDataListener>();
		
//		System.out.println(numberOfRows);
//		System.out.println(numberOfPages);
//		System.out.println(columnsOrder);
	}
	
	@Override
	public synchronized ArrayList<Hashtable<String, String>> loadPage(String tableName, int pageNumber)
			throws DBEngineException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(encodePageName(tableName, pageNumber)));
			String[] columns = decodeRow(reader.readLine());
			ArrayList<Hashtable<String, String>> list = new ArrayList<Hashtable<String,String>>();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.equals("")) {
					list.add(null);
					continue;
				}
				String[] row = decodeRow(line);
				Hashtable<String, String> table = new Hashtable<String, String>();
				for (int i = 0; i < row.length; i++) {
					table.put(columns[i], row[i]);
				}
				list.add(table);
			}
			return list;
		} catch (IOException e) {
			throw new DBEngineException("Bad file");
		}
	}
	
	@Override
	public synchronized Hashtable<String, String> loadRow(String tableName, int pageNumber,
			int rowNumber) throws DBEngineException {
		Hashtable<String, String> result = new Hashtable<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(encodePageName(tableName, pageNumber)));
			String[] columns = decodeRow(reader.readLine());
			String line = null;
			for (int i = 0; i <= rowNumber; i++) {
				line = reader.readLine();
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
	
	public synchronized void editRow(int row, String data, String filePath) throws IOException {
		File file = new File(filePath);
		File tmpFile = new File(tmpFilePath);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		PrintWriter writer = new PrintWriter(new FileWriter(tmpFile));
		String line = null;
		int index = -1;
		while ((line = reader.readLine()) != null) {
			if (index == row) {
				line = data;
			}
			//line will be equal to null if data = null (delete row)
			if (line != null) {
				writer.println(line);
			}
			index++;
		}
		writer.flush();
		file.delete();
		tmpFile.renameTo(file);
	}

	/*
	 * The format of the file is tablename_pagenumber
	 */ 
	
	@Override
	public synchronized void createTablePage(String tableName, int newPageNumber, String[] columns )
			throws DBEngineException {
		if ((new File(encodePageName(tableName,  newPageNumber)).exists())) {
			throw new DBEngineException("Page already exists");
		}
		try {
			
			PrintWriter writer = new PrintWriter(new FileWriter(encodePageName(tableName, newPageNumber)));
			if (numberOfPages.containsKey(tableName)) {
				numberOfPages.put(tableName, numberOfPages.get(tableName) + 1);
			}else{
				numberOfPages.put(tableName, 1);
			}
			numberOfRows.put(encodePageName(tableName, newPageNumber), 0);
			ArrayList<String> list = new ArrayList<String>();
			for (String s : columns) {
				list.add(s);
			}
			writer.println(encodeColumnsRow(columns));
			writer.flush();
			writer.close();
			columnsOrder.put(tableName, list);
			saveColumnsOrder();
			savePagesTable();
			saveRowsTable();
			
		} catch (IOException e) {
			throw new DBEngineException("There was a problem while accessing the file");
		}
	}

	private String encodeColumnsRow(String[] list) {
		StringBuffer buffer = new StringBuffer("");
		for (String s : list) {
			buffer.append(s);
			buffer.append(",");
		}
		return buffer.substring(0, buffer.length() - 1);
	}
	
	private void saveRowsTable() throws IOException {
		saveObject(numberOfRows, numberOfRowsFile);
	}

	
	@Override
	public synchronized void appendToMetaDataFile(Hashtable<String, String> data)
			throws DBEngineException {
		try {
			PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(metadataFile, true)));			
			writer.println(encodeRow(data, metadataColumnOrder));
			writer.flush();
			writer.close();
			notifyMetadataObservers();
		} catch (IOException e) {
			throw new DBEngineException("There was a problem while accessing the file");
		}
		
	}
	
	public synchronized void saveMetaDataFile(ArrayList<Hashtable<String, String>> data) throws DBEngineException {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(metadataFile));
			writer.println(encodeColumnsRow(metadataColumnOrder_));
			for (Hashtable<String, String> row : data) {
				writer.println(encodeRow(row, metadataColumnOrder));
			}
			writer.flush();
			notifyMetadataObservers();
		} catch (IOException e) {
			throw new DBEngineException("There was a problem while accessing the file");
		}
	}

	@Override
	public synchronized int appendToTable(String tableName, int pageNumber,
			Hashtable<String, String> data) throws DBEngineException {
		int lastRow = -1;
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(encodePageName(tableName, pageNumber), true));
			writer.println(encodeRow(data,columnsOrder.get(tableName)));
			writer.flush();
			numberOfRows.put(encodePageName(tableName, pageNumber), numberOfRows.get(encodePageName(tableName, pageNumber)) + 1);
			lastRow = getLastRow(tableName, pageNumber);
			saveRowsTable();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DBEngineException("There was a problem writing into the file");
		}
		return lastRow;
	}

	@Override
	public synchronized void deleteRow(String tableName, int pageNumber, int rowNumber)
			throws DBEngineException {
		try {
			editRow(rowNumber, "", encodePageName(tableName, pageNumber));
		} catch (IOException e) {
			throw new DBEngineException("There was a problem accesing the file");
		}
		
	}

	@Override
	public synchronized int appentToTable(String tableName, Hashtable<String, String> data) throws DBEngineException {
		int lastPage = getLastPageIndex(tableName);
		return appendToTable(tableName, lastPage, data);
	}

	public synchronized ArrayList<Hashtable<String, String>> loadMetaDataFile() throws DBEngineException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(metadataFile));
			String[] columns = decodeRow(reader.readLine());
			ArrayList<Hashtable<String, String>> list = new ArrayList<Hashtable<String,String>>();
			String line = null;
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
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DBEngineException();
		}
	}

	@Override
	public void listenToMetaDataFileUpdates(MetaDataListener properties) {
		metadataObservers.add(properties);
	}	

	@Override
	public int getLastPageIndex(String tableName) {
		return numberOfPages.get(tableName) - 1;
	}

	@Override
	public int getLastRow(String tableName, int pageNumber) {
		return numberOfRows.get(encodePageName(tableName, pageNumber)) - 1;
	}
	
	private String encodeRow(Hashtable<String, String> data, ArrayList<String> columns) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < columns.size(); i++) {
			buffer.append(data.get(columns.get(i)));
			buffer.append(",");
		}
		return buffer.substring(0, buffer.length() - 1);
	}
	
	private String[] decodeRow(String row) {
		return row.split(",");
	}
	
	private String encodePageName(String tableName, int pageNumber) {
		return String.format("data/%s_%s", tableName, pageNumber);
	}
	
	private String[] decodePageName(String fileName) {
		return fileName.split("data/")[1].split("_");
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
	
	private Map<String, Integer> loadRowsTable() {
		Map<String, Integer> map;
		try {
			map = (Map<String, Integer>) loadObject(numberOfRowsFile);
		} catch (Exception ex) {
			map = new HashMap<String, Integer>();
		}
	    return map;
	}

	private void notifyMetadataObservers() {
		ArrayList<Hashtable<String, String>> metadataFile = null;
		try {
			metadataFile = loadMetaDataFile();
		} catch (DBEngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (MetaDataListener l : metadataObservers) {
			l.refresh(metadataFile);
		}
	}
	
	private void saveColumnsOrder() {
		try {
			saveObject(columnsOrder, columnOrderFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadColumnsOrder() {
		try {
			columnsOrder = (Map<String, ArrayList<String>>) loadObject(columnOrderFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			columnsOrder = new HashMap<String, ArrayList<String>>();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			columnsOrder = new HashMap<String, ArrayList<String>>();
		}
	}
	
	
}
