package eg.edu.guc.dbms.interfaces;

import eg.edu.guc.dbms.sql.PhysicalPlanTree;

public interface TransactionManager {

	public void executeTrasaction(PhysicalPlanTree tree);
	
	
}
