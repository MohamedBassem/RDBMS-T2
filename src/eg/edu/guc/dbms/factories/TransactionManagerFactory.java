package eg.edu.guc.dbms.factories;

import eg.edu.guc.dbms.components.TransactionManagerImpl;
import eg.edu.guc.dbms.interfaces.TransactionManager;



public class TransactionManagerFactory {

	public static TransactionManager getInstance() {
		return new TransactionManagerImpl(BufferManagerFactory.getInstance(), LogManagerFactory.getInstance());
	}
	
}
