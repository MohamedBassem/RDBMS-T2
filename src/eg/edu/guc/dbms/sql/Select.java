package eg.edu.guc.dbms.sql;

import java.util.List;

public class Select extends PhysicalPlanTree{

	@Override
	public Operation getOperation() {
		return Operation.SELECT;
	}

	@Override
	public List<PhysicalPlanTree> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}

}
