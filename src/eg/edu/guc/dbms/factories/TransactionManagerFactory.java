package eg.edu.guc.dbms.factories;

import eg.edu.guc.dbms.components.TransactionManagerImpl;
import eg.edu.guc.dbms.interfaces.TransactionManager;
import eg.edu.guc.dbms.utils.CSVReader;
import eg.edu.guc.dbms.utils.Properties;
import eg.edu.guc.dbms.utils.btrees.BTreeFactory;



public class TransactionManagerFactory {

	public static TransactionManager getInstance() {
		CSVReader reader = new CSVReader();
		Properties properties = new Properties(reader);
		BTreeFactory btree =  new BTreeFactory(properties.getBTreeN());
		return new TransactionManagerImpl(BufferManagerFactory.getInstance(btree, properties), LogManagerFactory.getInstance(), btree, properties, reader);
	}
	
}
