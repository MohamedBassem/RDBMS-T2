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
import java.util.Map;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.helpers.Page;
import eg.edu.guc.dbms.helpers.Tuple;

public class DatabaseIO {
	
	private final String numberOfPagesFile = "data/pages.ser";
	private final String columnOrderFilePath = "data/columns.csv";
	
	private Map<String, Integer> numberOfPages;
	
	private Map<String, ArrayList<String>> columnsOrder;
	
	public DatabaseIO(){
		numberOfPages	= loadPagesTable();
		loadColumnsOrder();
	}
	
	public synchronized Page loadPage(String tableName, int pageNumber)
			throws DBEngineException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(encodePageName(tableName, pageNumber)));
			String[] columns = decodeRow(reader.readLine());
			Page list = new Page();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.equals("")) {
					list.add(null);
					continue;
				}
				String[] row = decodeRow(line);
				Tuple table = new Tuple();
				for (int i = 0; i < row.length; i++) {
					table.put(columns[i], row[i]);
				}
				list.add(table);
			}
			reader.close();
			return list;
		} catch (IOException e) {
			throw new DBEngineException("Bad file");
		}
	}
	
	public synchronized void writePage(String tableName,int pageNumber,Page page) throws DBEngineException{
		try {
			if (numberOfPages.containsKey(tableName) && numberOfPages.get(tableName) <= pageNumber  ) {
				numberOfPages.put(tableName, numberOfPages.get(tableName) + 1);
				savePagesTable();
			}
			ArrayList<String> columns = columnsOrder.get(tableName);
			PrintWriter writer = new PrintWriter( new BufferedWriter(new FileWriter(encodePageName(tableName, pageNumber))));
			writer.println(encodeColumnsRow(columns));
			for(Tuple tuple : page){
				if(tuple == null){
					writer.println();
				}else{
					writer.println(encodeRow(tuple, columns));
				}
			}
			writer.close();
		} catch (IOException e) {
			throw new DBEngineException("Bad file");
		}
	}
	
	public synchronized Page createTablePage(String tableName, String[] columns )
			throws DBEngineException {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(encodePageName(tableName, 0)));
			
			numberOfPages.put(tableName, 1);
			
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
			return new Page();
		} catch (IOException e) {
			throw new DBEngineException("There was a problem while accessing the file");
		}
	}
	
	private String encodePageName(String tableName, int pageNumber) {
		return String.format("data/%s_%s", tableName, pageNumber);
	}
	
	private void savePagesTable() throws IOException {
		saveObject(numberOfPages, numberOfPagesFile);
	}
	
	private void saveColumnsOrder() {
		try {
			saveObject(columnsOrder, columnOrderFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String[] decodeRow(String row) {
		return row.split(",");
	}
	
	private void saveObject(Object obj, String filePath) throws IOException {
		FileOutputStream outputStream;
		outputStream = new FileOutputStream(filePath);
		ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
		objectStream.writeObject(obj);
		objectStream.close();
	}
	
	// TODO
//	public int getLastPageIndex(String tableName) {
//		return numberOfPages.get(tableName) - 1;
//	}
	
	@SuppressWarnings("unchecked")
	private Map<String, Integer> loadPagesTable() {
		Map<String, Integer> map;
		try {
			map = (Map<String, Integer>) loadObject(numberOfPagesFile);
		} catch (Exception ex) {
			map = new HashMap<String, Integer>();
		}
	    return map;
	}
	
	@SuppressWarnings("unchecked")
	private void loadColumnsOrder() {
		try {
			columnsOrder = (Map<String, ArrayList<String>>) loadObject(columnOrderFilePath);
		} catch (IOException e) {
			columnsOrder = new HashMap<String, ArrayList<String>>();
		} catch (ClassNotFoundException e) {
			columnsOrder = new HashMap<String, ArrayList<String>>();
		}
	}
	
	private Object loadObject(String filePath) throws IOException, ClassNotFoundException {
		Object obj;
		FileInputStream inputStream = new FileInputStream(filePath);
		ObjectInputStream objectStream = new ObjectInputStream(inputStream);
		obj = objectStream.readObject();
		objectStream.close();
		return obj;
	}
	
	private String encodeRow(Hashtable<String, String> data, ArrayList<String> columns) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < columns.size(); i++) {
			buffer.append(data.get(columns.get(i)));
			buffer.append(",");
		}
		return buffer.substring(0, buffer.length() - 1);
	}
	
	private String encodeColumnsRow(ArrayList<String> list){
		String[] columns = new String[list.size()];
		for(int i=0;i<list.size();i++){
			columns[i] = list.get(i);
		}
		return encodeColumnsRow(columns);
	}
	
	private String encodeColumnsRow(String[] list) {
		StringBuffer buffer = new StringBuffer("");
		for (String s : list) {
			buffer.append(s);
			buffer.append(",");
		}
		return buffer.substring(0, buffer.length() - 1);
	}
	
}
