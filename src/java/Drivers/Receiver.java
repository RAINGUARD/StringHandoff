package Drivers;
import StringHandoff.*;

/**
 * @author Neil Haggerty
 *
 * This class creates a new thread that will act as a receiver. The constructor is passed an
 * instance of StringHandoff, and the new thread operates on that instance.
 */

public class Receiver extends Thread {
    private final StringHandoffImpl handOff;
    private final long msTimeout;
    private final long msDelayBeforeAttemptingToReceive;
    private final Thread internalThread;

    public Receiver(StringHandoffImpl handOff, long delay, String name) {
        this.handOff = handOff;
        msTimeout = 0L;
        msDelayBeforeAttemptingToReceive = delay;
        internalThread = new Thread(() -> runWork(), name != null ? name : "Passer");
        internalThread.start();

    }

    public Receiver(StringHandoffImpl handOff, long msTimeout, long delay, String name) {
        this.handOff = handOff;
        this.msTimeout = msTimeout;
        msDelayBeforeAttemptingToReceive = delay;
        internalThread = new Thread(() -> runWork(), name != null ? name : "Passer");
        internalThread.start();

    }

    public void runWork() {
        boolean wasInterrupted = false;
        try {
            System.out.printf("%s - starting\n", Thread.currentThread().getName());
            System.out.printf("%s - Taking a " + msDelayBeforeAttemptingToReceive + " ms nap before calling receive\n", Thread.currentThread().getName());
            Thread.sleep(msDelayBeforeAttemptingToReceive);

            if(msTimeout == 0L) {
                System.out.printf("%s - Attempting to call receive(wait indefinitely)\n", Thread.currentThread().getName());
            } else {
                System.out.printf("%s - Attempting to call receive(wait for " + msTimeout + " ms)\n", Thread.currentThread().getName());
            }
            String result = handOff.receive(msTimeout);
            System.out.printf("%s - back from receive() with message: " + result + "\n", Thread.currentThread().getName());
        } catch ( InterruptedException x ) {
            // ignore and let thread die
            wasInterrupted = true;
        } catch ( Throwable x ) {
            System.out.println(x);
        } finally {
            System.out.printf("%s - finished%s\n", Thread.currentThread().getName(), wasInterrupted ? " (was interrupted)" : "");
        }

    }

    public void stopRequest() {
        internalThread.interrupt();
    }

    public boolean waitUntilStopped(long msTimeout) throws InterruptedException {
        internalThread.join(msTimeout);
        return !internalThread.isAlive();
    }
}
