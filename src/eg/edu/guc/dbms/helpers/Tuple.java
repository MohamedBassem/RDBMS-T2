package eg.edu.guc.dbms.helpers;

import java.util.HashMap;
import java.util.Map;

public class Tuple extends HashMap<String,String> {
	
	public Tuple(){
		super();
	}
	
	public Tuple(Map<? extends String,? extends String> t){
		super(t);
	}
}
