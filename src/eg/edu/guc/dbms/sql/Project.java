package eg.edu.guc.dbms.sql;

import java.util.List;

public class Project extends PhysicalPlanTree {

	@Override
	public Operation getOperation() {
		return Operation.PROJECT;
	}

	public List<String> getProjectionColumn() {
		// TODO Auto-generated method stub
		return null;
	}
}
