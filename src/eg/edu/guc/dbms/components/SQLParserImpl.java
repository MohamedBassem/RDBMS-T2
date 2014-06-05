package eg.edu.guc.dbms.components;

import eg.edu.guc.dbms.components.sqlparser.*;
import eg.edu.guc.dbms.interfaces.SQLParser;
import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.stmt.TCreateTableSqlStatement;
import gudusoft.gsqlparser.stmt.TDeleteSqlStatement;
import gudusoft.gsqlparser.stmt.TInsertSqlStatement;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;
import gudusoft.gsqlparser.stmt.TUpdateSqlStatement;

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
					return SelectStatementParser.parse((TSelectSqlStatement) statement);
				case sstinsert:
					return InsertStatementParser.parse((TInsertSqlStatement) statement);
				case sstupdate:
					return UpdateStatementParser.parse((TUpdateSqlStatement) statement);
				case sstdelete:
					return DeleteStatementParser.parse((TDeleteSqlStatement) statement);
				case sstcreatetable:
					return CreateTableStatementParser.parse((TCreateTableSqlStatement) statement);
				case sstcreateindex:
				default:
					System.out.println("Unkown SQL statement: " + statement.sqlstatementtype.toString());
			}
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
