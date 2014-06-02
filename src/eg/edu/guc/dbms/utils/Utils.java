package eg.edu.guc.dbms.utils;

import java.util.Set;

public class Utils {
	public static String[] setToArray(Set<String> set){
		String[] ret = new String[set.size()];
		int i=0;
		for(String str : set){
			ret[i++] = str;
		}
		return ret; 
	}
}
