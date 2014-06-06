package eg.edu.guc.dbms.components.sqlparser;

import eg.edu.guc.dbms.sql.Delete;
import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import gudusoft.gsqlparser.stmt.TDeleteSqlStatement;

public class DeleteStatementParser {
	public static PhysicalPlanTree parse(TDeleteSqlStatement statement) {
		String tableName = statement.getTargetTable().toString();
		Delete result = new Delete(tableName);

		System.out.println("Delete from: " + tableName);
		if (statement.getWhereClause() != null) {
			String whereClause = statement.getWhereClause().getCondition().toString();
			System.out.printf("\nWhere clause: \n\t%s\n", whereClause);
			result.setWhereClause(whereClause);
		}
		return result;
	}
}
