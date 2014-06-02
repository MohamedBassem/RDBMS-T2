package eg.edu.guc.dbms.interfaces;

import java.io.IOException;

import eg.edu.guc.dbms.exceptions.DBEngineException;


public interface Command {
	
	public void execute() throws DBEngineException, IOException;
	
}
