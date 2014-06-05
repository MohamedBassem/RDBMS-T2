package eg.edu.guc.dbms.sql;

import java.util.Hashtable;
import java.util.List;

public class Create extends PhysicalPlanTree {

	private String name;
	private Hashtable<String, String> columnReference;
	private Hashtable<String, String> types;
	private String keyColName;

	@Override
	public Operation getOperation() {
		return Operation.CREATE_TABLE;
	}

	public String getTableName() {
		return name;
	}

	public Hashtable<String, String> getTableColRefs() {
		return columnReference;
	}

	public Hashtable<String, String> getTableTypes() {
		return this.types;
	}

	public String getKeyColName() {
		return keyColName;
	}
	
	public void setTableName(String name) {
		this.name = name;
	}
	
	public void setTableColRefs(Hashtable<String, String> colRefs) {
		this.columnReference = colRefs;
	}
	
	public void setTableTypes(Hashtable<String, String> types) {
		this.types = types;
	}
	
	public void setKeyColName(String keyColName) {
		this.keyColName = keyColName;
	}

}
