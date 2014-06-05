package eg.edu.guc.dbms.transactions;

import java.io.IOException;
import java.util.List;

import eg.edu.guc.dbms.components.BufferManager;
import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.Command;
import eg.edu.guc.dbms.interfaces.LogManager;

public class Transaction extends Thread {

	private BufferManager bufferManager;
	private LogManager logManager;
	private List<Command> steps;
	
	public Transaction(BufferManager bufManager, LogManager logManager,
            List<Command> vSteps) {
		this.bufferManager	= bufManager;
		this.logManager		= logManager;
		this.steps			= vSteps;
	}
	
	public void execute() {
		start();
	}

	@Override
	public void run() {
		for (Command step : steps) {
			try {
				step.execute();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			} catch (DBEngineException e) {
				e.printStackTrace();
				break;
			}
		}
	}
	
}
