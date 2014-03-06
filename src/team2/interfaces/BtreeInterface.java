package team2.interfaces;

import java.util.ArrayList;

public interface BtreeInterface {
	
	public void insert(String key,String value);
	
	public ArrayList<String> get(String key);
	
	public String getTableName();
	
	public String getColumnName();
	
	public boolean isPrimary();
	
	public void setTableName(String tableName);
	
	public void setColumnName(String columnName);
	
	public void setIsPrimary(boolean isPrimary);
	
	public void delete(String key);
	
	public String serialize();

}
