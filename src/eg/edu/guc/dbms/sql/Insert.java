package eg.edu.guc.dbms.sql;


public class Insert extends PhysicalPlanTree {

	public Insert(String tableName) {
		super(tableName);
	}

	public Operation getOperation() {
		return Operation.INSERT;
	}

}
