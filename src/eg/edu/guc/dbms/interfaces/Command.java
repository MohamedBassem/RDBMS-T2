package eg.edu.guc.dbms.interfaces;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import eg.edu.guc.dbms.exceptions.DBEngineException;


public interface Command {
	
	public void execute() throws DBEngineException, IOException;
	public List<Hashtable<String, String>> getResult();
}
