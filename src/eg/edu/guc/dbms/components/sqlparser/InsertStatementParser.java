package eg.edu.guc.dbms.components.sqlparser;

import eg.edu.guc.dbms.sql.Insert;
import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import gudusoft.gsqlparser.nodes.TMultiTarget;
import gudusoft.gsqlparser.stmt.TInsertSqlStatement;

public class InsertStatementParser {
	public static PhysicalPlanTree parse(TInsertSqlStatement statement) {
		String tableName = statement.getTargetTable().toString();
		PhysicalPlanTree result = new Insert(tableName);
		System.out.println("Table name:" + tableName);
		
		System.out.println("Insert value type:" + statement.getValueType());
		
		if (statement.getColumnList() != null) {
			System.out.println("columns:");
			for (int i = 0; i < statement.getColumnList().size(); i++) {
				System.out
						.println("\t"
								+ statement.getColumnList().getObjectName(i)
										.toString());
			}
		}

		if (statement.getValues() != null) {
			System.out.println("values:");
			for (int i = 0; i < statement.getValues().size(); i++) {
				TMultiTarget mt = statement.getValues().getMultiTarget(i);
				for (int j = 0; j < mt.getColumnList().size(); j++) {
					System.out.println("\t"
							+ mt.getColumnList().getResultColumn(j).toString());
				}
			}
		}
		
		return result;
	}
}
