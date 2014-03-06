package team2.interfaces;

import team2.exceptions.DBEngineException;
import team2.util.btrees.BTree;

public interface BtreeFactoryInterface {
	public BTree getBtree(String tableName,String coloumnName) throws DBEngineException; 
}