package team2.util.btrees;

import java.io.IOException;
import java.util.ArrayList;

public class BTree extends jdbm.btree.BTree {

	@Override
	 public synchronized Object insert( Object key, Object value, boolean replace)throws IOException{
		ArrayList<String> values = new ArrayList<String>();
		values.add((String)value);
		Object returned = super.insert(key, values, false);
		if(returned==null)
			return values;
		else {
			((ArrayList<String>) returned).add((String) value);
			return returned;
		}
	}


}
