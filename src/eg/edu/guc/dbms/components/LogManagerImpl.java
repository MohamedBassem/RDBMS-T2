package eg.edu.guc.dbms.components;

import java.util.HashMap;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import eg.edu.guc.dbms.interfaces.LogManager;

public class LogManagerImpl implements LogManager {

	private FileWriter logFile;
	private BufferedWriter bfr;
	private static LogManagerImpl instance = new LogManagerImpl();

	public static void main(String[] args) throws IOException {
		LogManagerImpl x = LogManagerImpl.getInstance();
		HashMap<String, String> h = new HashMap<String, String>();
		h.put("aaa", "bbbbb");
		h.put("cccc", "dddd");
		x.init();
		x.recordStart("T1");
//		x.recordInsert("T1", "Student", 4, h);
//		x.recordUpdate("T1", "Student", 1, "sd", "ID", 2, 4);
		x.recordDelete("T1", "Student", 2, "abc", h);
		x.recordCommit("T1");
		x.flushLog();
	}

	private LogManagerImpl() {
		try {
			this.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static LogManagerImpl getInstance() {
		return instance;
	}

	@Override
	public void init() throws IOException {
		logFile = new FileWriter("data/log/logFile.log", true);
		bfr = new BufferedWriter(logFile);
	}

	@Override
	public void flushLog() throws IOException {
		bfr.flush();

	}

	@Override
	public void recordStart(String strTransID) throws IOException {
		bfr.write("<START " + strTransID + ">\n");

	}

	@Override
	public void recordUpdate(String strTransID, String tableName,
			int pageNumber, String strKeyValue, String strColName,
			Object objOld, Object objNew) throws IOException {
		// <T1,U,Student4,pageNumber,colName,x.y>
		// Transcation1 : Update colName from table Student page 4 from x to y
		String result = "<" + strTransID + ",U," + tableName + "," + pageNumber
				+ "," + strKeyValue + "," + strColName + ","
				+ objOld.toString() + "," + objNew.toString() + ">\n";
		bfr.write(result);

	}

	@Override
	public void recordInsert(String strTransID, String tableName,
			int pageNumber, HashMap<String, String> htblColValues)
			throws IOException {
		// <T1,I,Student4,pageNumber,htblColValues.objectId>
		// Transcation1 : Insert x into Student page 4 with these attributes
		String result = "<" + strTransID + ",I," + tableName + "," + pageNumber + ","
				+ htblColValues.toString() + ">\n";
		bfr.write(result);
	}

	@Override
	public void recordDelete(String strTransID, String tableName,
			int pageNumber, String strKeyValue,
			HashMap<String, String> htblColValues) throws IOException {
		// <T1,D,Student4,pageNumber,htblColValues.objectId>
		// Transcation1 : Insert x into Student page 4 with these attributes
		String result = "<" + strTransID + ",D," + tableName + "," + pageNumber + ","
				+ strKeyValue + "," + htblColValues.toString() + ">\n";
		bfr.write(result);

	}

	@Override
	public void recordCommit(String strTransID) throws IOException {
		bfr.write("<COMMIT " + strTransID + ">\n");

	}

}
