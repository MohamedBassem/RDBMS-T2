package eg.edu.guc.dbms.sql;

import java.util.Hashtable;
import java.util.List;

public class Create extends PhysicalPlanTree {

	@Override
	public Operation getOperation() {
		return Operation.CREATE_TABLE;
	}

	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	public Hashtable<String, String> getTableColRefs() {
		// TODO Auto-generated method stub
		return null;
	}

	public Hashtable<String, String> getTableTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getKeyColName() {
		// TODO Auto-generated method stub
		return null;
	}

}
