package eg.edu.guc.dbms.interfaces;

import java.util.ArrayList;
import java.util.Hashtable;

public interface MetaDataListener  {
	public void refresh(ArrayList<Hashtable<String,String>> data);
}
