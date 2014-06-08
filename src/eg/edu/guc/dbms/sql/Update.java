package eg.edu.guc.dbms.sql;


public class Update extends PhysicalPlanTree {

	public Update(String tableName) {
		super(tableName);
	}

	@Override
	public Operation getOperation() {
		return Operation.UPDATE;
	}
}
