package eg.edu.guc.dbms.utils.btrees;

import java.io.IOException;
import java.util.ArrayList;

import jdbm.btree.BTree;

public class BTreeAdopter {
	private BTree tree;
	private BTreeFactory factory;
	
	public BTreeAdopter(BTree tree,BTreeFactory factory) {
		this.tree = tree;
		this.factory = factory;
	}

	 public Object insert( Object key, Object value)throws IOException{
		ArrayList<String> values = new ArrayList<String>();
		values.add((String)value);
		Object returned = tree.insert(key, values, false);
		if(returned==null){
			factory.saveAll();
			return values;
		}else {
			((ArrayList<String>) returned).add((String) value);
			factory.saveAll();
			return returned;
		}
	}
	
public boolean delete(Object key, String pointer) throws IOException {
		ArrayList <String> values = this.find(key);
		if((values==null) || (!values.contains(pointer))){
			return false;
		} 
		
		values.remove(pointer);
		
		if(values.isEmpty()) {
			tree.remove(key);
		}
		factory.saveAll();
		return true; 
	}

public ArrayList<String> find(Object key) throws IOException {	
	return (ArrayList<String>) tree.find(key);
}

}
