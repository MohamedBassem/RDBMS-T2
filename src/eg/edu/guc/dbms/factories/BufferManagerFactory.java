package eg.edu.guc.dbms.factories;

import eg.edu.guc.dbms.components.BufferManager;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;

public class BufferManagerFactory {

	public static BufferManager getInstance(BTreeFactory btree) {
		return new BufferManager(5, 10, true, btree);
	}
	
}
