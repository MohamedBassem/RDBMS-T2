package eg.edu.guc.dbms.interfaces;

import java.util.Hashtable;

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
            Hashtable<String,String> htblColValues);
    public void recordDelete(String strTransID, String tableName, int pageNumber,
            String strKeyValue,
            Hashtable<String,String> htblColValues);
    public void recordCommit( String strTransID );

	
}
