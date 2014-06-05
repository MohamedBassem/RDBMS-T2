package eg.edu.guc.dbms.components;

import java.util.HashMap;

import eg.edu.guc.dbms.interfaces.LogManager;

public class LogManagerImpl implements LogManager{

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void flushLog() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordStart(String strTransID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordUpdate(String strTransID, String tableName,
			int pageNummber, String strKeyValue, String strColName,
			Object objOld, Object objNew) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordInsert(String strTransID, String tableName,
			int pageNumber, HashMap<String, String> htblColValues) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordDelete(String strTransID, String tableName,
			int pageNumber, String strKeyValue,
			HashMap<String, String> htblColValues) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void recordCommit(String strTransID) {
		// TODO Auto-generated method stub
		
	}


}
