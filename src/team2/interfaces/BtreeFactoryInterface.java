package team2.interfaces;

import team2.exceptions.DBEngineException;
import team2.util.btrees.BTreeAdopter;

public interface BtreeFactoryInterface {
	public BTreeAdopter getBtree(String tableName,String coloumnName) throws DBEngineException; 
}