package eg.edu.guc.dbms.interfaces;

import java.io.IOException;
import java.util.Hashtable;

import eg.edu.guc.dbms.exceptions.DBAppException;

public interface LogManager {

	public void init( ) throws IOException;
	public void flushLog() throws IOException;
    public void recordStart(String strTransID) throws IOException;
    public void recordUpdate(String strTransID, String tableName, int pageNummber,
    		String strKeyValue,
    		String strColName,
    		Object objOld,
    		Object objNew) throws IOException;
    public void recordInsert(String strTransID, String tableName, int pageNumber,
            Hashtable<String,String> htblColValues) throws IOException;
    public void recordDelete(String strTransID, String tableName, int pageNumber,
            String strKeyValue,
            Hashtable<String,String> htblColValues) throws IOException;
    public void recordCommit( String strTransID ) throws IOException;

	
}
