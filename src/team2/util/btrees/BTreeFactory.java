package team2.util.btrees;

import java.io.IOException;
import java.util.ArrayList;

import jdbm.RecordManager;
import jdbm.RecordManagerFactory;
import jdbm.btree.BTree;
import jdbm.helper.StringComparator;
import team2.exceptions.DBEngineException;
import team2.interfaces.BtreeFactoryInterface;

public class BTreeFactory implements BtreeFactoryInterface {
	RecordManager recman;
	int BPlusTreeN;
	
	ArrayList<BTreeAdopter> inMemoryTrees;
	
	public BTreeFactory(int BPlusTreeN){
		inMemoryTrees = new ArrayList<BTreeAdopter>();
		this.BPlusTreeN = BPlusTreeN;
		try {
			recman = RecordManagerFactory.createRecordManager("BtreeDatabase");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public BTreeAdopter getBtree(String tableName, String columnName)
			throws DBEngineException {
		
		try {
			long recid = this.recman.getNamedObject(tableName+"."+columnName);
			BTree tree = BTree.load( recman, recid );
			BTreeAdopter ret = new BTreeAdopter(tree,this);
			inMemoryTrees.add(ret);
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public BTreeAdopter createTree(String tableName, String columnName) {
		 BTree tree;
		try {
			tree = BTree.createInstance( recman, new StringComparator(),null,null,this.BPlusTreeN );
			recman.setNamedObject( tableName+"."+columnName , tree.getRecid() );
			BTreeAdopter ret = new BTreeAdopter(tree,this);
			inMemoryTrees.add(ret);
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void saveAll(){
		try {
			recman.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
