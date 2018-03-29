package addressbook.exception;

import java.sql.SQLException;

public class ServiceException extends SQLException {

	private static final long serialVersionUID = 1L;
	
	public Throwable cause;
    
    public ServiceException() {
        super();
        cause = null;
    }
    
    public ServiceException(String str) {
        super(str);
        cause = null;
    }
    
    public ServiceException(String str, Throwable t) {
        super(str);            
        cause = t;
    }
    
}