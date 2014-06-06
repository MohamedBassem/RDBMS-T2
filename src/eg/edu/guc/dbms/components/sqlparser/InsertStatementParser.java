package eg.edu.guc.dbms.components.sqlparser;

import eg.edu.guc.dbms.sql.Insert;
import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import gudusoft.gsqlparser.nodes.TMultiTarget;
import gudusoft.gsqlparser.stmt.TInsertSqlStatement;

public class InsertStatementParser {
	public static PhysicalPlanTree parse(TInsertSqlStatement statement) {
		String tableName = statement.getTargetTable().toString();
		Insert result = new Insert(tableName);
		System.out.println("Table name:" + tableName);
		
		System.out.println("Insert value type:" + statement.getValueType());
		
		System.out.println("Key/Value:");
		for (int i = 0; i < statement.getColumnList().size(); i++) {
			TMultiTarget mt = statement.getValues().getMultiTarget(i);
			String column = statement.getColumnList().getObjectName(i).toString();
			// TODO Support multi target?
			String value = mt.getColumnList().getResultColumn(0).toString();
			System.out.println("\t" + column + ": " + value);
			result.setColValue(column, value);
		}
		return result;
	}
}
