package eg.edu.guc.dbms.sql;


public class Select extends PhysicalPlanTree{

	@Override
	public Operation getOperation() {
		return Operation.SELECT;
	}

}
