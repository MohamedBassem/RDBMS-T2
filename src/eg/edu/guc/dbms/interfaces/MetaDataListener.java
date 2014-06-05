package eg.edu.guc.dbms.interfaces;

import java.util.ArrayList;
import java.util.HashMap;

public interface MetaDataListener  {
	public void refresh(ArrayList<HashMap<String,String>> data);
}
