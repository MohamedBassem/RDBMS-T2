package eg.edu.guc.dbms.sql;

import java.util.HashMap;
import java.util.List;

public class Update extends PhysicalPlanTree {

	@Override
	public Operation getOperation() {
		return Operation.UPDATE;
	}

	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
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
