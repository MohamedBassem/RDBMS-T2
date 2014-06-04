package eg.edu.guc.dbms.sql;

import java.util.List;

public class Scan extends PhysicalPlanTree {

	@Override
	public Operation getOperation() {
		return Operation.SCAN;
	}

	@Override
	public List<PhysicalPlanTree> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

}
