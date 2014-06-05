package eg.edu.guc.dbms.components.sqlparser;

import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import gudusoft.gsqlparser.stmt.TDeleteSqlStatement;

public class DeleteStatementParser {
	public static PhysicalPlanTree parse(TDeleteSqlStatement statement) {
		String tableName = statement.getTargetTable().toString();
		return null;
	}
}
