package eg.edu.guc.dbms.components;

public class BufferManagerFlushRunnable implements Runnable {
	
	BufferManager bufferManager;
	
	int period;
	
	public BufferManagerFlushRunnable(BufferManager bufferManager,int period){
		this.bufferManager = bufferManager;
		this.period = period;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
