package eg.edu.guc.dbms.interfaces;

import eg.edu.guc.dbms.sql.PhysicalPlanTree;

public interface SQLParser {

	// True: No Errors
	// False: Error in SQL statement -> use getErrorMessage()
	public boolean parseSQLStatement(String statement);
	public String getErrorMessage();
	// Return type OBJECT to be replaced by designated parse tree data structure.
	public PhysicalPlanTree getParseTree();
}
