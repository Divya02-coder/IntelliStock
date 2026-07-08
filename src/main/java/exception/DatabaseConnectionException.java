package exception;

public class DatabaseConnectionException extends InventoryException {
 
    public DatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
 
    public DatabaseConnectionException(String message) {
        super(message);
    }
}