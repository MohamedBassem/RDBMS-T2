package eg.edu.guc.dbms.components.sqlparser;

import eg.edu.guc.dbms.sql.Index;
import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import gudusoft.gsqlparser.nodes.TOrderByItemList;
import gudusoft.gsqlparser.stmt.TCreateIndexSqlStatement;

public class CreateIndexStatementParser {
	public static PhysicalPlanTree parse(TCreateIndexSqlStatement statement) {
		String tableName = statement.getTargetTable().toString();
		String indexName = statement.getIndexName().toString();
		// TODO Support multi key index
		String columnName = statement.getColumnNameList().getOrderByItem(0).getSortKey().toString();
		Index result = new Index(tableName, columnName);

		System.out.println("Index name: " + indexName);
        System.out.println("Table name: " + tableName);
        TOrderByItemList list = statement.getColumnNameList();
        if (list != null){
            for(int i=0;i<list.size();i++){
                System.out.println("\tColumn name:" + list.getOrderByItem(i).getSortKey().toString());
            }
        }
        
		return result;
	}
}
