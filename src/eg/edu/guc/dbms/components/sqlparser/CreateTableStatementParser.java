package eg.edu.guc.dbms.components.sqlparser;

import eg.edu.guc.dbms.sql.Create;
import eg.edu.guc.dbms.sql.PhysicalPlanTree;
import gudusoft.gsqlparser.nodes.TColumnDefinition;
import gudusoft.gsqlparser.nodes.TConstraint;
import gudusoft.gsqlparser.stmt.TCreateTableSqlStatement;

public class CreateTableStatementParser {
	
	public static PhysicalPlanTree parse(TCreateTableSqlStatement statement) {
		String tableName = statement.getTargetTable().toString();
		Create result = new Create(tableName);
//		System.out.println("Table Name:" + tableName);
//        System.out.println("Columns:");
        TColumnDefinition column;
        for(int i=0;i<statement.getColumnList().size();i++){
            column = statement.getColumnList().getColumn(i);
            String columnName = column.getColumnName().toString();
            String columnType = column.getDatatype().toString();
            result.setColValue(columnName, columnType);
            
//            System.out.println("\t" + columnName + ": " + columnType);
            if (column.getDefaultExpression() != null){
//                System.out.println("\tDefault:"+column.getDefaultExpression().toString());
            }
            if (column.isNull()){
//                System.out.println("\tNull: yes");
            }
            if (column.getConstraints() != null){
//                System.out.println("\tInline constraints:");
                for(int j=0;j<column.getConstraints().size();j++){
                	String constraint = column.getConstraints().getConstraint(j).toString();
                	if(constraint.equals("PRIMARY KEY"))
                		result.setKeyColName(columnName);
                    printConstraint(column.getConstraints().getConstraint(j),false);
                }
            }
//            System.out.println("");
        }

        if(statement.getTableConstraints().size() > 0){
//            System.out.println("\tOutline Constraints:");
            for(int i=0;i<statement.getTableConstraints().size();i++){
            	//TODO Add regex for PRIMARY KEY(col1, col2, ...) constraint ?
              printConstraint(statement.getTableConstraints().getConstraint(i), true);
//              System.out.println("");
            }
        }
		return result;
	}
	
	private static void printConstraint(TConstraint constraint, Boolean outline){

        if (constraint.getConstraintName() != null){
            System.out.println("\t\tconstraint name:"+constraint.getConstraintName().toString());
        }

        switch(constraint.getConstraint_type()){
            case notnull:
                System.out.println("\t\tnot null");
                break;
            case primary_key:
                System.out.println("\t\tprimary key");
                if (outline){
                    String lcstr = "";
                    if (constraint.getColumnList() != null){
                        for(int k=0;k<constraint.getColumnList().size();k++){
                            if (k !=0 ){lcstr = lcstr+",";}
                            lcstr = lcstr+constraint.getColumnList().getObjectName(k).toString();
                        }
                        System.out.println("\t\tprimary key columns:"+lcstr);
                    }
                }
                break;
            case unique:
                System.out.println("\t\tunique key");
                if(outline){
                    String lcstr="";
                    if (constraint.getColumnList() != null){
                        for(int k=0;k<constraint.getColumnList().size();k++){
                            if (k !=0 ){lcstr = lcstr+",";}
                            lcstr = lcstr+constraint.getColumnList().getObjectName(k).toString();
                        }
                    }
                    System.out.println("\t\tcolumns:"+lcstr);
                }
                break;
            case check:
                System.out.println("\t\tcheck:"+constraint.getCheckCondition().toString());
                break;
            case foreign_key:
            case reference:
                System.out.println("\t\tforeign key");
                if(outline){
                    String lcstr="";
                    if (constraint.getColumnList() != null){
                        for(int k=0;k<constraint.getColumnList().size();k++){
                            if (k !=0 ){lcstr = lcstr+",";}
                            lcstr = lcstr+constraint.getColumnList().getObjectName(k).toString();
                        }
                    }
                    System.out.println("\t\tcolumns:"+lcstr);
                }
                System.out.println("\t\treferenced table:"+constraint.getReferencedObject().toString());
                if (constraint.getReferencedColumnList() != null){
                    String lcstr="";
                    for(int k=0;k<constraint.getReferencedColumnList().size();k++){
                        if (k !=0 ){lcstr = lcstr+",";}
                        lcstr = lcstr+constraint.getReferencedColumnList().getObjectName(k).toString();
                    }
                    System.out.println("\t\treferenced columns:"+lcstr);
                }
                break;
            default:
                break;
        }
    }
}
