package eg.edu.guc.dbms.components;

import eg.edu.guc.dbms.interfaces.SQLParser;
import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.stmt.TSelectSqlStatement;

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
		PhysicalPlanTree result = null;
		if(status == 0) {
			TCustomSqlStatement statement = sqlparser.sqlstatements.get(0);
			switch (statement.sqlstatementtype) {
			case sstselect:
				// analyzeSelectStmt((TSelectSqlStatement)stmt);
				break;
			case sstupdate:
				break;
			case sstcreatetable:
				break;
			case sstaltertable:
				break;
			case sstcreateview:
				break;
			default:
				System.out.println(statement.sqlstatementtype.toString());
			}
		}
		return result;
	}
}
