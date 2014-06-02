package eg.edu.guc.dbms.exceptions;

public class DBEngineException extends Throwable {

	
	private static final long serialVersionUID = 1L;

	public DBEngineException() {
		
	}
	
	public DBEngineException(String error) {
		super(error);
	}
	
}
