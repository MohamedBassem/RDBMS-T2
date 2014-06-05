package eg.edu.guc.dbms.components;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Hashtable;

import javax.swing.plaf.SliderUI;

import eg.edu.guc.dbms.engine.DBApp;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.RecoveryManager;

public class RecoveryManagerImpl implements RecoveryManager {

	RandomAccessFile raf;
	DBApp engine;
	Hashtable<String, Boolean> transactions;

	public void recover() throws IOException, DBEngineException {
		raf = new RandomAccessFile("data/log/logFile.log", "r");
		transactions = new Hashtable<String, Boolean>();
		String line = raf.readLine();
		while (line != null) {
			if (line.contains("<START ")) {
				transactions.put(line.substring(7, line.length() - 1), false);
				line = raf.readLine();
			}
			if (line.contains("<COMMIT ")) {
				transactions.put(line.substring(8, line.length() - 1), true);
			}
		}
		raf.seek(0);
		doAction(raf);

	}

	public void doAction(RandomAccessFile raf) throws IOException,
			DBEngineException {
		String line = raf.readLine();
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
						htblColNameValue.put(splittedLine[i].substring(0,
								splittedLine[i].indexOf("=")), splittedLine[i]
								.substring(splittedLine[i].indexOf("=")));
					}
					if (transactions.get(splittedLine[0]))
						engine.insertIntoTable(splittedLine[2],
								htblColNameValue);
					else
						engine.deleteFromTable(splittedLine[2],
								htblColNameValue, "AND");

					break;

				case "D":
					htblColNameValue = new HashMap<String, String>();
					splittedLine[splittedLine.length - 1] = splittedLine[splittedLine.length - 1]
							.substring(0, splittedLine[splittedLine.length - 1]
									.length() - 2);
					for (int i = 5; i < splittedLine.length; i++) {
						htblColNameValue.put(splittedLine[i].substring(0,
								splittedLine[i].indexOf("=")), splittedLine[i]
								.substring(splittedLine[i].indexOf("=")));
					}
					if (transactions.get(splittedLine[0]))
						engine.deleteFromTable(splittedLine[2],
								htblColNameValue, "AND");

					else
						engine.insertIntoTable(splittedLine[2],
								htblColNameValue);

					break;

				case "U":
					htblColNameValue = new HashMap<String, String>();
					HashMap<String, String> colValue = new HashMap<String, String>();
					colValue.put(splittedLine[5], splittedLine[4]);
					if (transactions.get(splittedLine[0])) {
						htblColNameValue.put(splittedLine[5], splittedLine[7]);
						engine.updateTable(splittedLine[2], htblColNameValue,
								"AND", colValue);
					} else {
						htblColNameValue.put(splittedLine[5], splittedLine[6]);
						engine.updateTable(splittedLine[2], htblColNameValue,
								"AND", colValue);
					}
					break;

				default:
					break;
				}

			}
		}

	}
}
