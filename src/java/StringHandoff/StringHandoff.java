package StringHandoff;
import Exceptions.*;

/**
 * @author Neil Haggerty
 * This is the interface for the StringHandoffImpl class
 */

public interface StringHandoff {
    /**
     * Attempts to pass the specified String to a receiver and waits until either it times out, or the String
     * is received.
     *
     * @param msg the String to be passed
     * @param msTimeout number of milliseconds to wait for the pass to complete before timing out. If number
     * is 0, it will wait indefinitely.
     * @throws InterruptedException if the calling thread is interrupted
     * @throws ShutdownException if StringHandoff has been shutdown
     * @throws IllegalStateException if there is already a thread waiting to receive
     * @throws TimedOutException if a pass is not completed by the time allotted
     */
    void pass(String msg, long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException;

    /**
     * Attempts to pass the specified String to a receiver and waits for the String to be received.
     *
     * @param msg the String to be passed to a receiver.
     * @throws InterruptedException if the calling thread is interrupted
     * @throws ShutdownException if StringHandoff has been shutdown
     * @throws IllegalStateException if there is already a thread waiting to receive
     * @throws TimedOutException if a pass is not completed by the time allotted
     */
    void pass(String msg) throws InterruptedException, ShutdownException, IllegalStateException, TimedOutException;

    /**
     * Attempts to receive a String from a passer and will wait until either it times out, or a String is passed.
     *
     * @param msTimeout number of milliseconds to wait for the pass to occur before timing out. If the number
     * is 0, it will wait indefinitely.
     * @throws InterruptedException if the calling thread is interrupted
     * @throws ShutdownException if StringHandoff has been shutdown
     * @throws IllegalStateException if there is already a thread waiting to receive
     * @throws TimedOutException if a pass is not completed by the time allotted
     */
    String receive(long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException;

    /**
     * Attempts to receive a String from a passer and will wait until the hand-off can be completed.
     *
     * @throws InterruptedException if the calling thread is interrupted
     * @throws ShutdownException if StringHandoff has been shutdown
     * @throws IllegalStateException if there is already a thread waiting to receive
     * @throws TimedOutException if a pass is not completed by the time allotted
     */
    String receive() throws InterruptedException, ShutdownException, IllegalStateException, TimedOutException;

    /**
     * Called to shutdown this instance of StringHandoff.
     * This call is asynchronous. It does not wait, but simply signal that a shutdown has been requested.
     * This method may be safely called any number of times.
     * This method never throws any exceptions.
     */
    void shutdown();

    /**
     * Return a reference to the object being used for synchronization.
     * This can be used to allow the caller to bridge holding the lock between method calls.
     */
    Object getLockObject();
}
