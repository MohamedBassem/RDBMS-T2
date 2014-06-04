package eg.edu.guc.dbms.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import eg.edu.guc.dbms.helpers.BufferQueueEntry;

public class BufferManager {
	
	/**
	 * The Queue of unused buffer slots
	 */
	Queue<BufferQueueEntry> unUsedSlots;
	
	/**
	 * The hashMap containing the used slots 
	 */
	HashMap<Integer,BufferQueueEntry> usedSlots;
	
	
	public void init(){
		unUsedSlots = new LinkedList<BufferQueueEntry>();
		usedSlots = new HashMap<Integer,BufferQueueEntry>();
	}
	
	public synchronized void read(String tableName,int pageNumber){
		
	}
	
	// TODO The write phase
	public synchronized void write(String tableName,int pageNumber){
		
	}

}
