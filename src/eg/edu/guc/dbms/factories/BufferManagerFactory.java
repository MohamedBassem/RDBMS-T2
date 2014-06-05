package eg.edu.guc.dbms.factories;

import eg.edu.guc.dbms.components.BufferManager;

public class BufferManagerFactory {

	public static BufferManager getInstance() {
		return new BufferManager(5, 10);
	}
	
}
