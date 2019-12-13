package exceptions;

public class DatabaseUpdateException extends Exception {
    public DatabaseUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
