package eg.edu.guc.dbms.factories;

import eg.edu.guc.dbms.components.LogManagerImpl;
import eg.edu.guc.dbms.interfaces.LogManager;

public class LogManagerFactory {

	public static LogManager getInstance() {
		return new LogManagerImpl();
	}
}
