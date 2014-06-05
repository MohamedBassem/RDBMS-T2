package eg.edu.guc.dbms.sql;


public class Scan extends PhysicalPlanTree {

	public Scan(String tableName) {
		// TODO Auto-generated constructor stub
		// @Farghal: Implement
	}

	@Override
	public Operation getOperation() {
		return Operation.SCAN;
	}

	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}



}
