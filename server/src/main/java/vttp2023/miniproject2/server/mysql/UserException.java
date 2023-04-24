package vttp2023.miniproject2.server.mysql;

public class UserException extends Exception {
    
    public UserException() {
        super();
    }

    public UserException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
