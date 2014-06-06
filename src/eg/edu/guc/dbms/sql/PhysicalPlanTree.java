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
}
