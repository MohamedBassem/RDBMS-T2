package eg.edu.guc.dbms.components;

import eg.edu.guc.dbms.interfaces.SQLParser;
import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import eg.edu.guc.dbms.sql.Project;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.nodes.TJoin;
import gudusoft.gsqlparser.nodes.TJoinItem;
import gudusoft.gsqlparser.nodes.TResultColumn;

public class SQLParserImpl implements SQLParser {
	private static SQLParserImpl instance = new SQLParserImpl();
	private TGSqlParser sqlparser;
	private int status;
	
	private SQLParserImpl() {
		sqlparser = new TGSqlParser(EDbVendor.dbvoracle);
		status = 0;
	}
	
	public static SQLParserImpl getInstance() {
		return instance;
	}

	public boolean parseSQLStatement(String statement) {
		sqlparser.sqltext = statement; 
		status = sqlparser.parse(); 
		return status == 0;
	}

	public String getErrorMessage() {
		if(status == 0)
			return null;
		return sqlparser.getErrormessage();
	}

	public PhysicalPlanTree getParseTree() {
		if(status == 0) {
			TCustomSqlStatement statement = sqlparser.sqlstatements.get(0);
			switch (statement.sqlstatementtype) {
				case sstselect:
					return parseSelectStatement(statement);
				case sstinsert:
				case sstupdate:
				case sstdelete:
				case sstcreatetable:
				case sstcreateindex:
				default:
					System.out.println("Unkown SQL statement: " + statement.sqlstatementtype.toString());
			}
		}
		return null;
	}
	
	private PhysicalPlanTree parseSelectStatement(TCustomSqlStatement statement) {
		Project project = new Project();
		for (int i = 0; i < statement.getResultColumnList().size(); i++) {
			TResultColumn resultColumn = statement.getResultColumnList().getResultColumn(i);
			project.addProjectionColumn(resultColumn.getExpr().toString());
			System.out.printf("\tColumn: %s, Alias: %s\n",
					resultColumn.getExpr().toString(), 
					(resultColumn.getAliasClause() == null) ? "" : resultColumn.getAliasClause().toString()
				);
		}
		
		for (int i = 0; i < statement.joins.size(); i++) {
			TJoin join = statement.joins.getJoin(i);
			System.out.printf("\ntable: \n\t%s, alias: %s\n", join.getTable()
					.toString(),
					(join.getTable().getAliasClause() != null) ? join
							.getTable().getAliasClause().toString() : "");
			for (int j = 0; j < join.getJoinItems().size(); j++) {
				TJoinItem joinItem = join.getJoinItems().getJoinItem(j);
				System.out.printf("Join type: %s\n", joinItem.getJoinType()
						.toString());
				System.out
						.printf("table: %s, alias: %s\n", joinItem.getTable()
								.toString(), (joinItem.getTable()
								.getAliasClause() != null) ? joinItem
								.getTable().getAliasClause().toString() : "");
				if (joinItem.getOnCondition() != null) {
					System.out.printf("On: %s\n", joinItem.getOnCondition()
							.toString());
				} else if (joinItem.getUsingColumns() != null) {
					System.out.printf("using: %s\n", joinItem.getUsingColumns()
							.toString());
				}
			}
		}
		
		if (statement.getWhereClause() != null){ 
			System.out.printf("\nwhere clause: \n\t%s\n", statement.getWhereClause().getCondition().toString()); 
		}

		return null;
	}
	
	public static void main(String[] args) {
		SQLParserImpl test = getInstance();
		System.out.println("Init.");
		test.parseSQLStatement("SELECT * FROM lulz, pikachu WHERE id = 1");
		test.getParseTree();
	}
}
