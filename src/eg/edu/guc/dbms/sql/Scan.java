package eg.edu.guc.dbms.sql;


public class Scan extends PhysicalPlanTree {
	
	String tableName;

	public Scan(String tableName) {
		super();
		this.tableName = tableName;
	}

	public Operation getOperation() {
		return Operation.SCAN;
	}

	public String getTableName() {
		return tableName;
	}



}
