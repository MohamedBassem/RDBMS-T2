package eg.edu.guc.dbms.factories;

import eg.edu.guc.dbms.components.BufferManager;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;

public class BufferManagerFactory {

	static BufferManager bufferManager;
	
	
	public static BufferManager getInstance(BTreeFactory btree, Properties properties) {
		if (bufferManager == null) {
			bufferManager = new BufferManager(
					properties.getMinimumEmptyBufferSlots(),
					properties.getMaximumUsedBufferSlots(), false, btree);
			bufferManager.init();
		}
		return bufferManager;
	}
	
}
