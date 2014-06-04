package eg.edu.guc.dbms.helpers;

import java.util.concurrent.Semaphore;

public class BufferSlot {
	private int id;
	private String pageName;
	private Page page;
	private Semaphore mutex;
	private long lastUsed;
	private int numUsing;
	
	public BufferSlot(int id){
		this.id = id;
		numUsing = 0;
		mutex = new Semaphore(1, true);
	}
	
	public void setPage(String pageName,Page page){
		this.pageName = pageName;
		this.page = page;
		lastUsed = System.currentTimeMillis();
	}
	
	public void acquire(){
		numUsing++;
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public Page getPage(){
		lastUsed = System.currentTimeMillis();
		return page;
	}
	
	public String getPageName(){
		return pageName;
	}
	
	public int getId(){
		return id;
	}
	
	public long getLastUsed(){
		return System.currentTimeMillis()- lastUsed;
	}
	
	public void release(){
		mutex.release();
		numUsing--;
	}
	
	public boolean isNotUsed(){
		return numUsing == 0;
	}
	
	public void clear(){
		this.pageName = null;
		this.page = null;
		this.lastUsed = System.currentTimeMillis();
	}
}
