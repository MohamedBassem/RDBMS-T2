package eg.edu.guc.dbms.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.interfaces.Command;

public class ProductCommand implements Command {
	private List<Hashtable<String, String>> relation1;
	private List<Hashtable<String, String>> relation2;
	private List<Hashtable<String, String>> result;

	public ProductCommand(List<Hashtable<String, String>> relation1,
			List<Hashtable<String, String>> relation2) {
		this.relation1 = relation1;
		this.relation2 = relation2;
		result = new ArrayList<Hashtable<String, String>>();
	}

	@Override
	public void execute() throws DBEngineException, IOException {
		for (int i = 0; i < relation1.size(); i++) {
			Hashtable<String, String> record = new Hashtable<String, String>();
			Set<String> keys = relation1.get(i).keySet();
			for (String key : keys) {
				record.put(key, relation1.get(i).get(key));
			}
			for (int k = 0; k < relation2.size(); k++) {
				Set<String> keys2 = relation2.get(k).keySet();
				for (String key : keys2) {
					record.put(key, relation2.get(k).get(key));
				}
			}
			result.add(record);
		}
	}

	@Override
	public List<Hashtable<String, String>> getResult() {
		return result;
	}

}
