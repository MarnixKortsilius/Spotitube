package exceptions;

public class DatabaseQueryException extends Exception {
    public DatabaseQueryException(String message, Throwable cause) {
        super(message, cause);
    }
}
