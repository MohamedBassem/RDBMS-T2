package eg.edu.guc.dbms.sql;

import java.util.HashMap;

public class Select extends PhysicalPlanTree{

	public Select(String whereClause) {
		// TODO Auto-generated constructor stub
		// @Farghal: Implement
	}

	@Override
	public Operation getOperation() {
		return Operation.SELECT;
	}

	public HashMap<String, String> getColValues() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getOperator() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}





}
