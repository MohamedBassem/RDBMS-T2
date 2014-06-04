package eg.edu.guc.dbms.helpers;

import java.util.Hashtable;
import java.util.Map;

public class Tuple extends Hashtable<String,String> {
	
	public Tuple(){
		super();
	}
	
	public Tuple(Map<? extends String,? extends String> t){
		super(t);
	}
}
