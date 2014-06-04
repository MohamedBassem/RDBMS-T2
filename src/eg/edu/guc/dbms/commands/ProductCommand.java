package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.Command;

public class ProductCommand implements Command {

	public ProductCommand(List<Hashtable<String, String>> relation1, List<Hashtable<String, String>> relation2) {
		
	}
	
	@Override
	public void execute() throws DBEngineException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Hashtable<String, String>> getResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
