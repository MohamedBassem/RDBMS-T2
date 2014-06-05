package eg.edu.guc.dbms.sql;

import java.util.HashMap;

public class Insert extends PhysicalPlanTree {

	private String tableName;
	private HashMap<String, String> colValues;
	
	public Insert(String tableName) {
		// TODO Auto-generated constructor stub
		this.tableName = tableName;
		colValues = new HashMap<String, String>();
	}

	@Override
	public Operation getOperation() {
		return Operation.INSERT;
	}

	public HashMap<String, String> getColValues() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setColValue(String column, String value) {
		colValues.put(column, value);
	}

	public String getTableName() {
		return tableName;
	}


}
