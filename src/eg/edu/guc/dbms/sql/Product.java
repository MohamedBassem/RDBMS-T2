package eg.edu.guc.dbms.sql;

public class Product extends PhysicalPlanTree{

	@Override
	public Operation getOperation() {
		return Operation.PRODUCT;
	}

}
