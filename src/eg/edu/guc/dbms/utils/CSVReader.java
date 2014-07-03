package eg.edu.guc.dbms.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.MetaDataListener;


public class CSVReader{

	private List<MetaDataListener> metadataObservers;
	private final String metadataFile = "data/meta.csv";
	private final String tmpFilePath = "data/tmp";
	private final String[] metadataColumnOrder_ = {"Table Name", "Column Name", "Column Type", "Key", "Indexed", "References"};
	private final ArrayList<String> metadataColumnOrder;
	
	public CSVReader() {
		metadataColumnOrder = new ArrayList<String>();
		for(String str : metadataColumnOrder_){
			metadataColumnOrder.add(str);
		}
		
		metadataObservers = new ArrayList<MetaDataListener>();
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
		reader.close();
		writer.close();
	}

	private String encodeColumnsRow(String[] list) {
		StringBuffer buffer = new StringBuffer("");
		for (String s : list) {
			buffer.append(s);
			buffer.append(",");
		}
		return buffer.substring(0, buffer.length() - 1);
	}
	
	public synchronized void appendToMetaDataFile(HashMap<String, String> data)
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
	
	public synchronized void saveMetaDataFile(ArrayList<HashMap<String, String>> data) throws DBEngineException {
		try {
			PrintWriter writer = new PrintWriter(new FileWriter(metadataFile));
			writer.println(encodeColumnsRow(metadataColumnOrder_));
			for (HashMap<String, String> row : data) {
				writer.println(encodeRow(row, metadataColumnOrder));
			}
			writer.flush();
			writer.close();
			notifyMetadataObservers();
		} catch (IOException e) {
			throw new DBEngineException("There was a problem while accessing the file");
		}
	}

	public synchronized ArrayList<HashMap<String, String>> loadMetaDataFile() throws DBEngineException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(metadataFile));
			String[] columns = decodeRow(reader.readLine());
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.equals("")) {
					continue;
				}
				String[] row = decodeRow(line);
				HashMap<String, String> table = new HashMap<String, String>();
				for (int i = 0; i < row.length; i++) {
					table.put(columns[i], row[i]);
				}
				list.add(table);
			}
			reader.close();
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			throw new DBEngineException();
		}
	}

	public void listenToMetaDataFileUpdates(MetaDataListener properties) {
		metadataObservers.add(properties);
	}	
	
	private String encodeRow(HashMap<String, String> data, ArrayList<String> columns) {
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

	private void notifyMetadataObservers() {
		ArrayList<HashMap<String, String>> metadataFile = null;
		try {
			metadataFile = loadMetaDataFile();
		} catch (DBEngineException e) {
			e.printStackTrace();
		}
		for (MetaDataListener l : metadataObservers) {
			l.refresh(metadataFile);
		}
	}
	
	
}
