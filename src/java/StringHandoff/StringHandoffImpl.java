package StringHandoff;
import Exceptions.*;

/**
 *  @author Neil Haggerty
 *
 *  This is a class for passing a string from one thread to another.
 *  It is basic exercise in multithreading.
 *  Safe to use with multiple threads and will not deadlock.
 */
public class StringHandoffImpl implements StringHandoff {

    private String message;
    private boolean messagePresent;
    private boolean receiverWaiting;
    private boolean passerWaiting;
    private boolean isShutdown;

    public StringHandoffImpl() {
        message = null;
    }

    @Override
    public synchronized void pass(String msg, long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        if(Thread.interrupted()) throw new InterruptedException();
        if(isShutdown) throw new ShutdownException();
        if(passerWaiting) throw new IllegalStateException("cannot pass. another passer is already waiting.");

        try {
            passerWaiting = true;
            message = msg;
            messagePresent = true;
            notifyAll();
            waitWhileMessagePresent(msTimeout);
        } finally {
            message = null;
            messagePresent = false;
            passerWaiting = false;
        }
    }

    @Override
    public synchronized void pass(String msg) throws InterruptedException, ShutdownException, IllegalStateException, TimedOutException {
        pass(msg, 0L);
    }

    @Override
    public synchronized String receive(long msTimeout)
        throws InterruptedException, TimedOutException, ShutdownException, IllegalStateException {

        if(Thread.interrupted()) throw new InterruptedException();
        if(isShutdown) throw new ShutdownException();
        if (receiverWaiting) throw new IllegalStateException("cannot receive, another receiver is already waiting.");

        try {
            receiverWaiting = true;
            waitUntilMessagePresent(msTimeout);
            String msg = message;
            messagePresent = false;
            notifyAll();
            return msg;
        } finally {
            receiverWaiting = false;
        }

    }

    @Override
    public synchronized String receive() throws InterruptedException, ShutdownException, IllegalStateException, TimedOutException {
        return receive(0L);
    }

    private synchronized void waitWhileMessagePresent(long msTimeout) throws TimedOutException, ShutdownException, InterruptedException {
        long waitValue;
        if(msTimeout < 0L) {
            waitValue = 100L;
        } else {
            waitValue = msTimeout;
        }

        if(!messagePresent) return;

        if(waitValue == 0L) {
            while(messagePresent) {
                wait();
                if(isShutdown) throw new ShutdownException();
            }
        } else {
            long msEndTime = System.currentTimeMillis() + waitValue;
            long msRemaining = waitValue;
            while(msRemaining >= 1L) {
                wait(msRemaining);
                if(isShutdown) throw new ShutdownException();
                if(!messagePresent) return;
                msRemaining = msEndTime - System.currentTimeMillis();
            }
            throw new TimedOutException(waitValue);
        }
    }

    private synchronized void waitUntilMessagePresent(long msTimeout) throws TimedOutException, ShutdownException, InterruptedException {
        long waitValue;
        if(msTimeout < 0L) {
            waitValue = 100L;
        } else {
            waitValue = msTimeout;
        }

        if(messagePresent) return;

        if(waitValue == 0L) {
            while(!messagePresent) {
                wait();
                if(isShutdown) throw new ShutdownException();
            }
        } else {
            long msEndTime = System.currentTimeMillis() + waitValue;
            long msRemaining = waitValue;
            while(msRemaining >= 1L) {
                wait(msRemaining);
                if(isShutdown) throw new ShutdownException();
                if(messagePresent) return;
                msRemaining = msEndTime - System.currentTimeMillis();
            }
            throw new TimedOutException(waitValue);
        }
    }

    @Override
    public synchronized void shutdown() {
        isShutdown = true;
        notifyAll();
    }

    @Override
    public Object getLockObject() {
        return this;
    }
}