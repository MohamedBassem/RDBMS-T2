package eg.edu.guc.dbms.sql;

import java.util.List;

public abstract class PhysicalPlanTree {

	public enum Operation {
		PROJECT,
		SELECT,
		PRODUCT,
		SCAN,
		INSERT,
		CREATE_TABLE,
		UPDATE;
	}
	
	public abstract Operation getOperation();
	public abstract List<PhysicalPlanTree> getChildren();
	
}
