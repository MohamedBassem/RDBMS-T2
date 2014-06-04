package eg.edu.guc.dbms.transactions;

import java.util.List;

import eg.edu.guc.dbms.components.BufferManager;
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
		
	}
	
}
