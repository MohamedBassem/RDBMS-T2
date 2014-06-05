package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.Command;

public class ProductCommand implements Command {
	private List<HashMap<String, String>> relation1;
	private List<HashMap<String, String>> relation2;
	private List<HashMap<String, String>> result;

	public ProductCommand(List<HashMap<String, String>> relation1,
			List<HashMap<String, String>> relation2) {
		this.relation1 = relation1;
		this.relation2 = relation2;
		result = new ArrayList<HashMap<String, String>>();
	}

	@Override
	public void execute() throws DBEngineException, IOException {
		for (int i = 0; i < relation1.size(); i++) {
			for (int count = 0; count < relation2.size(); count++) {
				HashMap<String, String> record = new HashMap<String, String>();

				Set<String> keys = relation1.get(i).keySet();
				for (String key : keys) {
					record.put(key, relation1.get(i).get(key));
				}
				Set<String> keys2 = relation2.get(count).keySet();
				for (String key : keys2) {
					record.put(key, relation2.get(count).get(key));
				}
				result.add(record);
			}
		}
	}

	@Override
	public List<HashMap<String, String>> getResult() {
		return result;
	}

}
