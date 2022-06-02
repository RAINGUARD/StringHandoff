package Exceptions;

public class ShutdownException extends Exception {
    public ShutdownException() {
        super("Thread was shut down.");
    }
}
