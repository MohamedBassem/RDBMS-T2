package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.Command;

public class IntermediateSelectCommand implements Command {

	public IntermediateSelectCommand(List<HashMap<String, String>> relation,
			HashMap<String, String> htblColNameValue, String strOperator) {
		
	}
	
	@Override
	public void execute() throws DBEngineException, IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<HashMap<String, String>> getResult() {
		// TODO Auto-generated method stub
		return null;
	}

}
