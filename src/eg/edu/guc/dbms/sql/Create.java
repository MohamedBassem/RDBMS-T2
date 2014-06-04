package eg.edu.guc.dbms.sql;

import java.util.List;

public class Craete extends PhysicalPlanTree {

	@Override
	public Operation getOperation() {
		return Operation.CREATE_TABLE;
	}

	@Override
	public List<PhysicalPlanTree> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

}
