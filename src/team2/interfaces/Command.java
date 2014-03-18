package team2.interfaces;

import java.io.IOException;

import team2.exceptions.DBEngineException;

public interface Command {
	
	public void execute() throws DBEngineException, IOException;
	
}
