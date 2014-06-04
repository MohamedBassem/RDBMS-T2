package eg.edu.guc.dbms.components;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

public class RecoveryManager {
	FileReader logFile;
	BufferedReader br;

	public void recover() throws IOException {
		logFile = new FileReader("data/log/logFile.log");
		br = new BufferedReader(logFile);
		Hashtable<String, Boolean> transactions = new Hashtable<String, Boolean>();
		String line = br.readLine();
		while (line != null) {
			if (line.contains("<START ")) {
				transactions.put(line.substring(7, line.length() - 1), false);
			}
		}

	}
}
