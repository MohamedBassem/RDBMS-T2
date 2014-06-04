package eg.edu.guc.dbms.sql;

import java.util.Hashtable;
import java.util.List;

public class Insert extends PhysicalPlanTree {

	@Override
	public Operation getOperation() {
		return Operation.INSERT;
	}

	public Hashtable<String, String> getColValues() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}


}
