package eg.edu.guc.dbms.components.sqlparser;

import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import eg.edu.guc.dbms.sql.Update;
import gudusoft.gsqlparser.nodes.TExpression;
import gudusoft.gsqlparser.nodes.TResultColumn;
import gudusoft.gsqlparser.stmt.TUpdateSqlStatement;

public class UpdateStatementParser {
	public static PhysicalPlanTree parse(TUpdateSqlStatement statement) {
		String tableName = statement.getTargetTable().toString();
		Update result = new Update(tableName);
		System.out.println("Set clause:");
		for (int i = 0; i < statement.getResultColumnList().size(); i++) {
			TResultColumn resultColumn = statement.getResultColumnList().getResultColumn(i);
			TExpression expression = resultColumn.getExpr();
			String column = expression.getLeftOperand().toString();
			String value = expression.getRightOperand().toString();
			result.setColValue(column, value);
			System.out.println("\t" + column + " = " + value);
		}
		if (statement.getWhereClause() != null) {
			String whereClause = statement.getWhereClause().getCondition().toString();
			System.out.println("Where clause:\n\t" + whereClause);
			result.setWhereClause(whereClause);
		}
		return result;
	}
}
