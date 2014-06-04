package eg.edu.guc.dbms.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.helpers.BufferSlot;
import eg.edu.guc.dbms.helpers.Page;
import eg.edu.guc.dbms.utils.DatabaseIO;

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
	
	/**
	 * The queue containing monitors for threads waiting for free slots to wake them up
	 */
	Queue<Object> waitingForFreeSlots;
	
	
	DatabaseIO databaseIO;
	
	public BufferManager(int minimumSlots,int maximumSlots){
		this.minimumSlots = minimumSlots;
		this.maximumSlots = maximumSlots;
		databaseIO = new DatabaseIO();
	}
	
	public void init(){
		unUsedSlots = new LinkedList<BufferSlot>();
		usedSlots = new HashMap<String,BufferSlot>();
		waitingForFreeSlots = new LinkedList<Object>();
		
		for(int i=0;i<maximumSlots;i++){
			unUsedSlots.add(new BufferSlot(i));
		}
		
		initializeFlusher();
		
	}
	
	
	public Page read(String tableName,int pageNumber,boolean bModify){
		String pageName = encodePageName(tableName, pageNumber);
		Page page = null;
		BufferSlot slot = null;
		if(usedSlots.containsKey(pageName)){
			slot = usedSlots.get(pageName);
			slot.acquire();
			page = slot.getPage();
			if(!bModify){
				slot.release();
			}
		}else{
			slot = getEmptySlot();
			usedSlots.put(pageName, slot);
			slot.acquire();
			try {
				page = databaseIO.loadPage(tableName, pageNumber);
			} catch (DBEngineException e) {
				e.printStackTrace();
			}
			slot.setPage(pageName, page);
		}
		if(!bModify){
			slot.release();
		}
		
		return page;
	}

	public void write(String tableName,int pageNumber,Page page){
		String pageName = encodePageName(tableName, pageNumber);
		BufferSlot slot = usedSlots.get(pageName);
		slot.setPage(pageName, page);
		slot.release();
	}
	
	public void createTable(String tableName,String[] columns){
		
	}
	
	public synchronized void LRU(){
		
	}
	
	private BufferSlot getEmptySlot() {
		if(unUsedSlots.size() == 0){
			Object mon = new Object();
			waitingForFreeSlots.add(mon);
			synchronized (mon) {
			    try {
					mon.wait(); // Wait until notified by another thread that there is an empty slot
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		BufferSlot slot = unUsedSlots.poll();
		
		if(unUsedSlots.size() < minimumSlots || usedSlots.size() > maximumSlots ){
			runFlusher();
		}
		
		return slot;
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
	
	private void runFlusher(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				LRU();
			}
		}).start();
	}
	
	private String encodePageName(String tableName, int pageNumber) {
		return String.format("%s_%s", tableName, pageNumber);
	}
	
	public int getLastPageIndex(String tableName){
		return databaseIO.getLastPageIndex(tableName);
	}
	

}
