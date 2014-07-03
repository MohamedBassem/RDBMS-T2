package eg.edu.guc.dbms.sql;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public abstract class PhysicalPlanTree {
	
	String tableName, whereClause;
	private HashMap<String, String> colValues;

	public enum Operation {
		PROJECT,
		SELECT,
		PRODUCT,
		SCAN,
		INSERT,
		CREATE_TABLE,
		INDEX,
		UPDATE,
		DELETE;
	}
	
	List<PhysicalPlanTree> children;
	
	public PhysicalPlanTree() {
		children = new LinkedList<PhysicalPlanTree>();
		colValues = new HashMap<String, String>();
	}
	
	public PhysicalPlanTree(String tableName) {
		this();
		this.tableName = tableName; 
	}
	
	public abstract Operation getOperation();
	public List<PhysicalPlanTree> getChildren() {
		return children;
	}
	public PhysicalPlanTree addChild(PhysicalPlanTree tree) {
		children.add(tree);
		return this;
	}
	
	public PhysicalPlanTree popLastChild() {
		return children.remove(children.size()-1);
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public String getWhereClause() {
		return whereClause;
	}
	
	public void setWhereClause(String whereClause) {
		this.whereClause = whereClause;
	}

	public HashMap<String, String> getColValues() {
		return colValues;
	}
	
	public void setColValue(String column, String value) {
		colValues.put(column, value);
	}
	
	public String getOperator() {
		if(getWhereClause() != null && getWhereClause().contains("AND"))
			return "AND";
		else if(getWhereClause() != null && getWhereClause().contains("OR"))
			return "OR";
		return null;
	}
	
	public HashMap<String, String> getColWhereValues() {
		if (getWhereClause() == null) {
			return null;
		}
		String[] sets = new String[1];
		if (getOperator() == null) {
			sets[0] = getWhereClause();
		} else {
			sets = getWhereClause().split(getOperator());
		}
		
		for(int i = 0; i < sets.length; i++)
			sets[i] = sets[i].trim();
		HashMap<String, String> result = new HashMap<String, String>();
		for(int i = 0; i < sets.length; i++) {
			String[] where = sets[i].split("=");
			result.put(trimEnd(where[0]), trimEnd(where[1]));
		}
		return result.size() == 0? null: result;
	}

	
	private String trimEnd(String str) {
		int count = 0;
		for (int i = str.length() - 1; i >= 0; i--) {
			if (str.charAt(i) != ' ') {
				break;
			}
			count++;
		}
		return str.substring(0, str.length() - count);
	}

}
