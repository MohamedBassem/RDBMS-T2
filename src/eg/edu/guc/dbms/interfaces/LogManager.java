package eg.edu.guc.dbms.interfaces;

import java.util.HashMap;

public interface LogManager {

	public void init( );
	public void flushLog();
    public void recordStart(String strTransID);
    public void recordUpdate(String strTransID, String tableName, int pageNummber,
    		String strKeyValue,
    		String strColName,
    		Object objOld,
    		Object objNew);
    public void recordInsert(String strTransID, String tableName, int pageNumber,
            HashMap<String,String> htblColValues);
    public void recordDelete(String strTransID, String tableName, int pageNumber,
            String strKeyValue,
            HashMap<String,String> htblColValues);
    public void recordCommit( String strTransID );

	
}
