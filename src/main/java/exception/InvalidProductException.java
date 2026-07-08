package exception;
/*
  Thrown when product data is malformed, fails validation rules
  (negative price, unknown category, blank SKU, etc.), or when the
 */
public class InvalidProductException extends InventoryException {
 
    public InvalidProductException(String message) {
        super(message);
    }
 
    public InvalidProductException(String message, Throwable cause) {
        super(message, cause);
    }
}