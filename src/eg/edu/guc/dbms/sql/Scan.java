package eg.edu.guc.dbms.sql;

import java.util.List;

public class Scan extends PhysicalPlanTree {

	@Override
	public Operation getOperation() {
		return Operation.SCAN;
	}

	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}



}
