package eg.edu.guc.dbms.components;

import eg.edu.guc.dbms.components.sqlparser.CreateIndexStatementParser;
import eg.edu.guc.dbms.components.sqlparser.CreateTableStatementParser;
import eg.edu.guc.dbms.components.sqlparser.DeleteStatementParser;
import eg.edu.guc.dbms.components.sqlparser.InsertStatementParser;
import eg.edu.guc.dbms.components.sqlparser.SelectStatementParser;
import eg.edu.guc.dbms.components.sqlparser.UpdateStatementParser;
import eg.edu.guc.dbms.interfaces.SQLParser;
import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TCustomSqlStatement;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.stmt.TCreateIndexSqlStatement;
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
					return CreateIndexStatementParser.parse((TCreateIndexSqlStatement) statement);
				default:
					System.out.println("Unkown SQL statement: " + statement.sqlstatementtype.toString());
			}
		}
		return null;
	}
	
	public static void main(String[] args) {
		SQLParserImpl test = getInstance();
		String tests[] = {	"SELECT * FROM pokemon WHERE name = 'Pikachu'",
							"INSERT INTO kitchen (woman) VALUES ('SAK')",
							"DELETE FROM kitchen WHERE woman = 'Alaa Maher'",
							"CREATE TABLE Gamdeen (name STRING PRIMARY KEY, gamadan INT)",
							"INSERT INTO Gamdeen (name, gamadan) VALUES ('Rami', 99999)",
							"UPDATE Gamdeen SET name = 'Rami Khalil' WHERE name = 'Rami'",
							"CREATE INDEX index_gamda ON Gamdeen (name)",
							"SELECT FROM WHERE",
							"Enta 3amel eih?"};
		for(int i = 0; i < tests.length; i++) {
			System.out.println(tests[i]);
			if(test.parseSQLStatement(tests[i]))
				test.getParseTree();
			else
				System.out.println("ERROR: " + test.getErrorMessage());
			System.out.println("------\n");
		}
	}
}
