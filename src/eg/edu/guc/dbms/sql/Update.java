package eg.edu.guc.dbms.sql;

import java.util.List;

public class Update extends PhysicalPlanTree {

	@Override
	public Operation getOperation() {
		return Operation.UPDATE;
	}

	@Override
	public List<PhysicalPlanTree> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

}
