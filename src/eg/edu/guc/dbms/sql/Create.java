package eg.edu.guc.dbms.sql;

import java.util.HashMap;

public class Create extends PhysicalPlanTree {

	private HashMap<String, String> columnReference;
	private HashMap<String, String> types;
	private String keyColName;
	
	public Create(String tableName) {
		super(tableName);
	}

	@Override
	public Operation getOperation() {
		return Operation.CREATE_TABLE;
	}

	public HashMap<String, String> getTableColRefs() {
		return columnReference;
	}

	public HashMap<String, String> getTableTypes() {
		return this.types;
	}

	public String getKeyColName() {
		return keyColName;
	}
	
	public void setTableColRefs(HashMap<String, String> colRefs) {
		this.columnReference = colRefs;
	}
	
	public void setTableTypes(HashMap<String, String> types) {
		this.types = types;
	}
	
	public void setKeyColName(String keyColName) {
		this.keyColName = keyColName;
	}

}
