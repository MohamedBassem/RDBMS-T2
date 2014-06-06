package eg.edu.guc.dbms.components;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;


import eg.edu.guc.dbms.engine.RecoveryMode;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.LogManager;
import eg.edu.guc.dbms.interfaces.RecoveryManager;

public class RecoveryManagerImpl implements RecoveryManager {

	RandomAccessFile raf;
	RecoveryMode recoveryMode;
	Hashtable<String, Boolean> transactions;

	public static void main(String[] args) throws IOException,
			DBEngineException {
		RecoveryManagerImpl x = new RecoveryManagerImpl();
		x.recover();
	}

	public void recover() throws IOException, DBEngineException {
		raf = new RandomAccessFile("data/log/logFile.log", "r");
		transactions = new Hashtable<String, Boolean>();
		recoveryMode = new RecoveryMode(new LogManager() {

			@Override
			public void recordUpdate(String strTransID, String tableName,
					int pageNummber, String strKeyValue, String strColName,
					Object objOld, Object objNew) throws IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public void recordStart(String strTransID) throws IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public void recordInsert(String strTransID, String tableName,
					int pageNumber, HashMap<String, String> htblColValues)
					throws IOException {
				System.out.println("Inserting");
			}

			@Override
			public void recordDelete(String strTransID, String tableName,
					int pageNumber, String strKeyValue,
					HashMap<String, String> htblColValues) throws IOException {
				System.out.println("deleting");
			}

			@Override
			public void recordCommit(String strTransID) throws IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public void init() throws IOException {
				// TODO Auto-generated method stub

			}

			@Override
			public void flushLog() throws IOException {
				// TODO Auto-generated method stub

			}
		});

		String line = raf.readLine();
		while (line != null) {
			if (line.contains("<START ")) {
				transactions.put(line.substring(7, line.length() - 1), false);
			}
			if (line.contains("<COMMIT ")) {
				transactions.put(line.substring(8, line.length() - 1), true);
			}
			line = raf.readLine();
		}
		raf.seek(0);
		doAction(raf);

	}

	public void doAction(RandomAccessFile raf) throws IOException,
			DBEngineException {
		String line = raf.readLine();
		recoveryMode.init();
		while (line != null) {
			if (!line.contains("<START ") && !line.contains("<COMMIT ")) {
				String[] splittedLine = line.split(",");
				switch (splittedLine[1]) {
				case "I":
					HashMap<String, String> htblColNameValue = new HashMap<String, String>();
					splittedLine[splittedLine.length - 1] = splittedLine[splittedLine.length - 1]
							.substring(0, splittedLine[splittedLine.length - 1]
									.length() - 2);
					for (int i = 4; i < splittedLine.length; i++) {
						if (splittedLine[i].contains("{")
								|| splittedLine[i].contains(" ")) {
							htblColNameValue.put(splittedLine[i].substring(1,
									splittedLine[i].indexOf("=")),
									splittedLine[i].substring(splittedLine[i]
											.indexOf("=") + 1));
						} else {
							htblColNameValue.put(splittedLine[i].substring(0,
									splittedLine[i].indexOf("=")),
									splittedLine[i].substring(splittedLine[i]
											.indexOf("=") + 1));
						}
					}
					if (transactions.get(splittedLine[0].substring(1))) {
						recoveryMode.insertIntoTable(splittedLine[2],
								htblColNameValue);
					} else {
						recoveryMode.deleteFromTable(splittedLine[2],
								htblColNameValue, "AND");
					}
					break;

				case "D":
					htblColNameValue = new HashMap<String, String>();
					splittedLine[splittedLine.length - 1] = splittedLine[splittedLine.length - 1]
							.substring(0, splittedLine[splittedLine.length - 1].length() - 2);
					for (int i = 5; i < splittedLine.length; i++) {
						if (splittedLine[i].contains("{")
								|| splittedLine[i].contains(" ")) {
							htblColNameValue.put(splittedLine[i].substring(1,
									splittedLine[i].indexOf("=")),
									splittedLine[i].substring(splittedLine[i]
											.indexOf("=") + 1));
						} else {
							htblColNameValue.put(splittedLine[i].substring(0,
									splittedLine[i].indexOf("=")),
									splittedLine[i].substring(splittedLine[i]
											.indexOf("=") + 1));
						}
					}
					if (transactions.get(splittedLine[0].substring(1))) {
						recoveryMode.deleteFromTable(splittedLine[2],
								htblColNameValue, "AND");
					} else {
						recoveryMode.insertIntoTable(splittedLine[2],
								htblColNameValue);
					}
					break;

				case "U":
					htblColNameValue = new HashMap<String, String>();
					HashMap<String, String> colValue = new HashMap<String, String>();
					colValue.put(splittedLine[5], splittedLine[4]);
					if (transactions.get(splittedLine[0])) {
						htblColNameValue.put(splittedLine[5], splittedLine[7]);
						recoveryMode.updateTable(splittedLine[2],
								htblColNameValue, "AND", colValue);
					} else {
						htblColNameValue.put(splittedLine[5], splittedLine[6]);
						recoveryMode.updateTable(splittedLine[2],
								htblColNameValue, "AND", colValue);
					}
					break;

				default:
					break;
				}

			}
			line = raf.readLine();
		}

	}
}
