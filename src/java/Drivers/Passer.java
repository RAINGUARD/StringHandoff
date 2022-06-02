package Drivers;
import StringHandoff.*;

/**
 * @author Neil Haggerty
 *
 * This class creates a new thread that will act as a passer. The constructor is passed an
 * instance of StringHandoff, and the new thread operates on that instance.
 */

public class Passer {
    private final String message;
    private final StringHandoffImpl handOff;
    private final long msTimeout;
    private final long msDelayBeforePassing;
    private final Thread internalThread;

    public Passer(String msg, StringHandoffImpl handOff, long delay, String name) {
        message = msg;
        this.handOff = handOff;
        msTimeout = 0L;
        msDelayBeforePassing = delay;
        internalThread = new Thread(() -> runWork(), name != null ? name : "Passer");
        internalThread.start();

    }

    public Passer(String msg, StringHandoffImpl handOff, long msTimeout, long delay, String name) {
        message = msg;
        this.handOff = handOff;
        this.msTimeout = msTimeout;
        msDelayBeforePassing = delay;
        internalThread = new Thread(() -> runWork(), name != null ? name : "Passer");
        internalThread.start();

    }

    private void runWork() {
        boolean wasInterrupted = false;
        try {
            System.out.printf("%s - starting\n", Thread.currentThread().getName());
            System.out.printf("%s - Taking a " + msDelayBeforePassing + " ms nap before calling pass\n", Thread.currentThread().getName());
            Thread.sleep(msDelayBeforePassing);

            if(msTimeout == 0L) {
                System.out.printf("%s - Attempting to call pass(wait indefinitely)\n", Thread.currentThread().getName());
            } else {
                System.out.printf("%s - Attempting to call pass(wait for " + msTimeout + " ms)\n", Thread.currentThread().getName());
            }
            handOff.pass(message, msTimeout);

        } catch ( InterruptedException x ) {
            // ignore and let thread die
            //wasInterrupted = true;
        } catch ( Throwable x ) {
            System.out.println(x);
        } finally {
            System.out.printf("%s - finished%s\n", Thread.currentThread().getName(), wasInterrupted ? " (was interrupted)\n" : "");
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
