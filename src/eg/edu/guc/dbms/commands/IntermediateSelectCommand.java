package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.Command;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.Utils;

public class IntermediateSelectCommand implements Command {

	List<HashMap<String, String>> relation;
	HashMap<String, String> htblColNameValue;
	String strOperator;
	List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
	Properties properties;

	public IntermediateSelectCommand(List<HashMap<String, String>> relation,
			HashMap<String, String> htblColNameValue, String strOperator,
			Properties properties) {
		this.relation = relation;
		this.htblColNameValue = htblColNameValue;
		this.strOperator = strOperator;
		this.properties = properties;
	}

	@Override
	public void execute() throws DBEngineException, IOException {
		this.validate();
		if (strOperator==null || strOperator.equals("AND")) {
			selectAndOperator();
		}
		if (strOperator==null ||strOperator.equals("OR")) {
			selectOrOperator();
		}
		
	}

	public void selectAndOperator() {
		Set<String> columnName = htblColNameValue.keySet();
		String[] columnNames = Utils.setToArray(columnName);
		for (int i = 0; i < relation.size(); i++) {
			boolean satisfied = true;
			for (int j = 0; j < columnNames.length; j++) {
				if (relation.get(i).get(columnNames[j]) != htblColNameValue
						.get(columnNames[j])) {
					satisfied = false;
				}
			}
			if (satisfied) {
				results.add(relation.get(i));
			}
		}
	}

	public void selectOrOperator() {
		Set<String> columnName = htblColNameValue.keySet();
		String[] columnNames = Utils.setToArray(columnName);
		for (int i = 0; i < relation.size(); i++) {
			boolean satisfiedOnce = false;
			for (int j = 0; j < columnNames.length; j++) {
				if (relation.get(i).get(columnNames[j]) == htblColNameValue
						.get(columnNames[j])) {
					satisfiedOnce = true;
					break;
				}
			}
			if (satisfiedOnce) {
				results.add(relation.get(i));
			}
		}
	}

	public void validate() throws DBEngineException {
		if (strOperator!=null && !strOperator.equals("AND") && !strOperator.equals("OR")) {
			throw new DBEngineException("Unknown Operator");
		}
	}

	@Override
	public List<HashMap<String, String>> getResult() {
		return results;
	}

}
