package eg.edu.guc.dbms.components;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import eg.edu.guc.dbms.exceptions.DBEngineException;
import eg.edu.guc.dbms.helpers.BufferSlot;
import eg.edu.guc.dbms.helpers.Page;
import eg.edu.guc.dbms.utils.DatabaseIO;
import eg.edu.guc.dbms.utils.LoggerFormater;


public class BufferManager {
	
	private final static Logger LOGGER = Logger.getLogger(BufferManager.class.getName()); 
	
	public static final int FLUSH_PERIOD = 1000*60*2;
	
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
	
	private Semaphore mutex;
	
	boolean runBackGroundFlusher;
	
	
	DatabaseIO databaseIO;
	
	public BufferManager(int minimumSlots,int maximumSlots,boolean runBackGroundFlusher){
		this.minimumSlots = minimumSlots;
		this.maximumSlots = maximumSlots;
		this.runBackGroundFlusher = runBackGroundFlusher;
		databaseIO = new DatabaseIO();
		
		ConsoleHandler handler = new ConsoleHandler();
		handler.setLevel(Level.ALL);
		handler.setFormatter(new LoggerFormater());
        LOGGER.setUseParentHandlers(false);
        LOGGER.addHandler(handler);
        mutex = new Semaphore(1);
        
	}
	
	public void init(){
		unUsedSlots = new LinkedList<BufferSlot>();
		usedSlots = new HashMap<String,BufferSlot>();
		waitingForFreeSlots = new LinkedList<Object>();
		
		try {
			mutex.acquire();
		} catch (InterruptedException e) {}
		
		for(int i=0;i<maximumSlots;i++){
			unUsedSlots.add(new BufferSlot(i));
		}
		mutex.release();
		
		if(runBackGroundFlusher){
			initializeFlusher();
		}
		
		LOGGER.info("Buffer Manager Started");
		
	}
	
	
	public Page read(long transactionId,String tableName,int pageNumber,boolean bModify){
		String pageName = encodePageName(tableName, pageNumber);
		Page page = null;
		BufferSlot slot = null;
		
		try {
			mutex.acquire();
		} catch (InterruptedException e) {}
		boolean containsKey = usedSlots.containsKey(pageName);

		if(containsKey){
			slot = usedSlots.get(pageName);
			mutex.release();
			LOGGER.info(transactionId + " : Trying to get read access to " + pageName + " which exists in the memory in slot " + slot.getId());
			slot.acquire();
			LOGGER.info(transactionId + " : Read access to " + pageName + " Granted.");
			page = slot.getPage();
		}else{
			LOGGER.info(transactionId + " : Trying to get read access to " + pageName + " which doesn't exists in the memory.");
			slot = getEmptySlot(transactionId);
			LOGGER.info(transactionId + " : Read access to " + pageName + " Granted in slot " + slot.getId());
			usedSlots.put(pageName, slot);
			mutex.release();
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
			LOGGER.info(transactionId + " : Read access to " + pageName + " Released.");
		}
		
		return page;
	}
	
	public void write(long transactionId, String tableName, int pageNumber, Page page){
		String pageName = encodePageName(tableName, pageNumber);
		BufferSlot slot = usedSlots.get(pageName);
		slot.setPage(pageName, page);
		slot.setDirty(true);
		slot.release();
		LOGGER.info(transactionId + " : Lock on " + pageName + " Released.");
	}
	
	public void createTable(long id,String tableName,String[] columns){
		try {
			databaseIO.createTablePage(tableName, columns);
			LOGGER.info(id + " : Created Table Page");
		} catch (DBEngineException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void LRU(){
		
		BufferSlot leastUsedSlot = null;
		long lastAccessed = 0;
		for(String pageName : usedSlots.keySet()){
			BufferSlot bufferSlot = usedSlots.get(pageName);
			if(bufferSlot.isDirty() ){
				try {
					databaseIO.writePage(bufferSlot.getTableName(), bufferSlot.getPageNumber(), bufferSlot.getPage());
					bufferSlot.setDirty(false);
				} catch (DBEngineException e) {
					e.printStackTrace();
				}
			}
			if(lastAccessed < bufferSlot.getLastUsed() && bufferSlot.isNotUsed()){
				lastAccessed = bufferSlot.getLastUsed();
				leastUsedSlot = bufferSlot;
			}
			
		}
		LOGGER.info("Dirty Data flushed.");
		
		if(leastUsedSlot != null){
			usedSlots.remove(leastUsedSlot.getPageName());
			leastUsedSlot.clear();
			unUsedSlots.add(leastUsedSlot);
			
			Object mon = waitingForFreeSlots.poll();
			if(mon != null){
				synchronized (mon) {
					mon.notify();
				}
			}
		}
		
	}
	
	private BufferSlot getEmptySlot(long id) {
		if(unUsedSlots.size() == 0){
			Object mon = new Object();
			waitingForFreeSlots.add(mon);
			synchronized (mon) {
			    try {
			    	LOGGER.info(id + " : is waiting for an empty slot.");
			    	mutex.release();
					mon.wait(); // Wait until notified by another thread that there is an empty slot
					try {
						mutex.acquire();
					} catch (InterruptedException e) {}
					
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		BufferSlot slot = unUsedSlots.poll();
		LOGGER.info(id + " : Found an empty slot " + slot.getId());
		
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
					LOGGER.info("Flusher Started..");
					try {
						mutex.acquire();
					} catch (InterruptedException e) {}
					LRU();
					mutex.release();
					LOGGER.info("Flusher Ended..");
					try {
						Thread.sleep(FLUSH_PERIOD);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
	}
	
	public void runFlusher(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					mutex.acquire();
				} catch (InterruptedException e) {}
				LOGGER.info("Flusher Started..");
				LRU();
				LOGGER.info("Flusher Ended..");
				mutex.release();
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
