package eg.edu.guc.dbms.helpers;

import java.util.ArrayList;
import java.util.HashMap;

public class Page extends ArrayList<Tuple> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1912185703703568541L;
	
	public void add(HashMap<String, String> tuple){
		super.add(new Tuple(tuple));
	}
}
