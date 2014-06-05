package eg.edu.guc.dbms.components;

import eg.edu.guc.dbms.interfaces.SQLParser;

public class SQLParserImpl implements SQLParser {
	private static SQLParserImpl instance = new SQLParserImpl();
	
	private SQLParserImpl() {
		
	}
	
	public static SQLParserImpl getInstance() {
		return instance;
	}

	@Override
	public boolean parseSQLStatement(String statement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getParseTree() {
		// TODO Auto-generated method stub
		return null;
	}
}
