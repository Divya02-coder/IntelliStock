package exception;
// Base custom exception for all inventory related errors
public class InventoryException extends Exception {
    public InventoryException(String message){
        super(message);
    }
    
 
    public InventoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
