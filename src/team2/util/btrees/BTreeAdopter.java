package team2.util.btrees;

import java.io.IOException;
import java.util.ArrayList;

import jdbm.btree.BTree;

public class BTreeAdopter {
	BTree tree;
	
	public BTreeAdopter(BTree tree) {
		this.tree = tree;
	}

	 public Object insert( Object key, Object value, boolean replace)throws IOException{
		ArrayList<String> values = new ArrayList<String>();
		values.add((String)value);
		Object returned = tree.insert(key, values, false);
		if(returned==null)
			return values;
		else {
			((ArrayList<String>) returned).add((String) value);
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
		return true; 
	}

public ArrayList<String> find(Object key) throws IOException {	
	return (ArrayList<String>) tree.find(key);
}

}
