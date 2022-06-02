package Drivers;
import java.util.*;

import StringHandoff.*;

/**
 * @author Neil Haggerty
 *
 * This class runs a series of tests where a passing thread shows up first.
 * The passing thread will wait until either it times out, or a receiving
 * thread comes along to receive the message.
 */

public class PasserShowsUpFirst {
    private final Thread internalThread;

    public PasserShowsUpFirst() {
        internalThread = new Thread(() -> runWork());
        internalThread.start();
    }

    private void runWork() {
        StringHandoffImpl h1 = new StringHandoffImpl();
        try {
            Scanner s1 = new Scanner(System.in);
            String temp;
            System.out.println("Press enter to begin test 1.");
            temp = s1.nextLine();
            noWaitTime(h1);
            System.out.println("Press enter to begin test 2.");
            temp = s1.nextLine();
            withWaitTimeFail(h1);
            System.out.println("Press enter to begin test 3.");
            temp = s1.nextLine();
            withWaitTime(h1);
        } catch (InterruptedException x) {
            x.printStackTrace();
        }

    }

    public void noWaitTime(StringHandoffImpl handoff) throws InterruptedException, IllegalStateException {
        System.out.println("--------Test 1: Passer Shows Up First(no wait time)---------");
        Passer p1 = new Passer("Eggs", handoff, 200L, "Thread A");
        Receiver r1 = new Receiver(handoff, 2000L, "Thread B");
        Thread.sleep(4000L);
        p1.stopRequest();
        r1.stopRequest();
        if (!p1.waitUntilStopped(2000L)) System.out.println("!!! passer ignored stopRequest and is still stuck running");
        if (!r1.waitUntilStopped(2000L)) System.out.println("!!! receiver ignored stopRequest and is still stuck running");
        System.out.println("-------End of Test 1--------");
        System.out.println("===============================");

    }

    public void withWaitTime(StringHandoffImpl handoff) throws InterruptedException {
        System.out.println("-------Test 3: Passer Shows Up First(with wait time)--------");
        Passer p1 = new Passer("toast", handoff, 1000L, 200L, "Thread A");
        Receiver r1 = new Receiver(handoff, 500L, "Thread B");
        Thread.sleep(5000L);
        p1.stopRequest();
        r1.stopRequest();
        p1.stopRequest();
        r1.stopRequest();
        if (!p1.waitUntilStopped(2000L)) System.out.println("!!! passer ignored stopRequest and is still stuck running");
        if (!r1.waitUntilStopped(2000L)) System.out.println("!!! receiver ignored stopRequest and is still stuck running");
        System.out.println("------End of Test 3------");
        System.out.println("===============================");
        ReceiverShowsUpFirst rsuf1 = new ReceiverShowsUpFirst();
    }

    public void withWaitTimeFail(StringHandoffImpl handoff) throws InterruptedException {
        System.out.println("-----Test 2: Passer Shows Up First(with wait time: FAIL ON PURPOSE)------");
        Passer p1 = new Passer("hotdogs", handoff, 800L, 200L, "Thread A");
        Receiver r1 = new Receiver(handoff, 1000L, 1500L, "Thread B");
        Thread.sleep(5000L);
        p1.stopRequest();
        r1.stopRequest();
        p1.stopRequest();
        r1.stopRequest();
        if (!p1.waitUntilStopped(2000L)) System.out.println("!!! passer ignored stopRequest and is still stuck running");
        if (!r1.waitUntilStopped(2000L)) System.out.println("!!! receiver ignored stopRequest and is still stuck running");
        System.out.println("------End of Test 2------");
        System.out.println("===============================");
    }


}
