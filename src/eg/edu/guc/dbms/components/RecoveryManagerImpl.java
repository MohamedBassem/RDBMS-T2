package eg.edu.guc.dbms.components;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Hashtable;

import sun.org.mozilla.javascript.ast.ForLoop;
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
	
	public void doAction(RandomAccessFile raf) {
		String line = raf.readLine();
		while (line != null) {
			if (!line.contains("<START ") && !line.contains("<COMMIT ")) {
				String[] splittedLine = line.split(",");
				Hashtable<String, String> htblColNameValue = new Hashtable<String, String>();
				for (int i = 3; i < splittedLine.length; i++) {
					htblColNameValue.put(splittedLine[i].
							substring(0,splittedLine[i].indexOf("=")), splittedLine[i].
							substring(splittedLine[i].indexOf("=")));
				}
				switch (splittedLine[1]) {
				case "I":
					if (transactions.get(splittedLine[0]))
						engine.insertIntoTable(splittedLine[2],
								htblColNameValue);
					else
						engine.deleteFromTable(splittedLine[2],
								htblColNameValue, "AND");

					break;

				case "D":
					htblColNameValue = new Hashtable<String, String>();
					if (transactions.get(splittedLine[0]))
						engine.deleteFromTable(splittedLine[2],
								htblColNameValue, "AND");

					else
						engine.insertIntoTable(splittedLine[2],
								htblColNameValue);

					break;

				case "U":
					if (transactions.get(splittedLine[0]))
					
					engine.updateTable(splittedLine[2], htblColNameValue, "AND", colValue);
					break;

				default:
					break;
				}

			}
		}
		
	}
}
