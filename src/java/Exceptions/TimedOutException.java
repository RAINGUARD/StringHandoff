package Exceptions;

public class TimedOutException extends Exception {
    public TimedOutException() {
        super("Thread timed out.");
    }

    public TimedOutException(long waitValue) {
        super("Thread timed out after waiting " + waitValue + " ms.");
    }
}
