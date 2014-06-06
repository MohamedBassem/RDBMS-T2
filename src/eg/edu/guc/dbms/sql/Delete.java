package eg.edu.guc.dbms.sql;


public class Delete extends PhysicalPlanTree {

	public Delete(String tableName) {
		super(tableName);
	}

	public Operation getOperation() {
		return Operation.DELETE;
	}

}
