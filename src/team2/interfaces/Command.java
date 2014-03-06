package team2.interfaces;

import team2.exceptions.DBEngineException;

public interface Command {
	
	public void execute() throws DBEngineException;
	
}
