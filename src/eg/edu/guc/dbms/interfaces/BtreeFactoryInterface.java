package eg.edu.guc.dbms.interfaces;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.utils.btrees.BTreeAdopter;

public interface BtreeFactoryInterface {
	public BTreeAdopter getBtree(String tableName,String coloumnName) throws DBEngineException; 
}