package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.Command;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;

public class UpdateCommand implements Command {

	public UpdateCommand(BTreeFactory btfactory,CSVReader reader,Properties properties,String tableName,
			HashMap<String, String> htblColNameValue, String strOperator, HashMap<String, String> colValue) {
		
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
