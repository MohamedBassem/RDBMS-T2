package team2.util;

import java.util.Hashtable;

import team2.interfaces.MetaDataListener;

public class Properties implements MetaDataListener {
	CSVReader reader;
	
	public Properties(CSVReader reader){
		this.reader = reader;
		init();
	}
	
	private void init(){
		
	}

	@Override
	public void refresh(Hashtable<String, String>[] data) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
