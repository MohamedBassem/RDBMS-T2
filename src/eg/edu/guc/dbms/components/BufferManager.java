package eg.edu.guc.dbms.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import eg.edu.guc.dbms.helpers.BufferSlot;
import eg.edu.guc.dbms.helpers.Page;

public class BufferManager {
	
	public static final int FLUSH_PERIOD = 2000;
	
	/**
	 * The Queue of unused buffer slots
	 */
	Queue<BufferSlot> unUsedSlots;
	
	/**
	 * The hashMap containing the used slots 
	 */
	HashMap<String,BufferSlot> usedSlots;
	
	/**
	 * The minimum number of memory slots
	 */
	int minimumSlots;
	
	/**
	 * The maximum number of memory slots to be used
	 */
	int maximumSlots;
	
	public BufferManager(int minimumSlots,int maximumSlots){
		this.minimumSlots = minimumSlots;
		this.maximumSlots = maximumSlots;
	}
	
	public void init(){
		unUsedSlots = new LinkedList<BufferSlot>();
		usedSlots = new HashMap<String,BufferSlot>();
		
		for(int i=0;i<maximumSlots;i++){
			unUsedSlots.add(new BufferSlot(i));
		}
		
		initializeFlusher();
		
	}
	
	
	public synchronized void read(String tableName,int pageNumber){
		
	}
	
	public synchronized void write(String tableName,int pageNumber,Page page){
		
	}
	
	public synchronized void createTable(String tableName,String[] columns){
		
	}
	
	public synchronized void LRU(){
		
	}
	
	private void initializeFlusher() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true){
					LRU();
					try {
						Thread.sleep(FLUSH_PERIOD);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
	}

}
