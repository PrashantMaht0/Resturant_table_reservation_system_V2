package exception;
// Exception thrown when a table is not available for reservation
public class TableNotAvailableException extends Exception {
    public TableNotAvailableException(String message) {
        super(message);
    }
    
}
