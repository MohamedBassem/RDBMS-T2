package eg.edu.guc.dbms.sql;


public class Index extends PhysicalPlanTree {
	
	String columnName;
	
	public Index(String tableName, String columnName) {
		super(tableName);
		this.columnName = columnName;
	}

	@Override
	public Operation getOperation() {
		return Operation.INDEX;
	}

	public String getColumnName() {
		return columnName;
	}

}
