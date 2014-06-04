package eg.edu.guc.dbms.sql;

import java.util.List;

public class Project extends PhysicalPlanTree {

	@Override
	public Operation getOperation() {
		return Operation.PRODUCT;
	}

	@Override
	public List<PhysicalPlanTree> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

}
