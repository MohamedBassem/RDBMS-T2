package eg.edu.guc.dbms.sql;

import java.util.HashMap;

public class Update extends PhysicalPlanTree {

	public Update(String tableName) {
		super(tableName);
	}

	@Override
	public Operation getOperation() {
		return Operation.UPDATE;
	}

	public HashMap<String, String> getColSearchValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOperator() {
		// TODO Auto-generated method stub
		return null;
	}

	public HashMap<String, String> getColValue() {
		// TODO Auto-generated method stub
		return null;
	}


}
