package eg.edu.guc.dbms.interfaces;

import java.util.HashMap;
import java.util.List;

public interface TransactionCallbackInterface {
	public void onPostExecute(List< HashMap<String,String> > results);
}
