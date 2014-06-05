package eg.edu.guc.dbms.components;

import eg.edu.guc.dbms.interfaces.SQLParser;
import eg.edu.guc.dbms.sql.*;
import gudusoft.gsqlparser.*;
import gudusoft.gsqlparser.nodes.*;

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
		PhysicalPlanTree result = new Project();
		
		for (int i = 0; i < statement.getResultColumnList().size(); i++) {
			TResultColumn resultColumn = statement.getResultColumnList().getResultColumn(i);
			( (Project) result).addProjectionColumn(resultColumn.getExpr().toString());
			System.out.printf("\tColumn: %s, Alias: %s\n",
					resultColumn.getExpr().toString(), 
					(resultColumn.getAliasClause() == null) ? "" : resultColumn.getAliasClause().toString()
				);
		}
		
		PhysicalPlanTree next = null, current = result;
		
		if (statement.getWhereClause() != null) {
			System.out.printf("\nwhere clause: \n\t%s\n", statement.getWhereClause().getCondition().toString());
			next = new Select(statement.getWhereClause().getCondition().toString());
			current.addChild(next);
			current = next;
		}
		
		for (int i = 0; i < statement.joins.size(); i++) {
			TJoin join = statement.joins.getJoin(i);
			System.out.printf("\ntable: \n\t%s, alias: %s\n", join.getTable().toString(),
					(join.getTable().getAliasClause() != null) ? join
							.getTable().getAliasClause().toString() : "");
			
			if(i > 0) { // Product Tree
				next = new Product();
				next.addChild(current.popLastChild());
				next.addChild(new Scan(join.getTable().toString()));
				current.addChild(next);
				current = next;
			} else { // Single table
				next = new Scan(join.getTable().toString());
				current.addChild(next);
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		SQLParserImpl test = getInstance();
		System.out.println("Init.");
		test.parseSQLStatement("SELECT * FROM lulz, pikachu WHERE id = 1");
		test.getParseTree();
	}
}
