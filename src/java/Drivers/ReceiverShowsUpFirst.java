package Drivers;
import java.util.*;

import StringHandoff.*;

/**
 * @author Neil Haggerty
 *
 * This class runs a series of tests where a receiving thread shows up first.
 * The receiving thread waits until either it times out, or a passing thread
 * comes along to pass it a message.
 */

public class ReceiverShowsUpFirst extends Thread {
    private final Thread internalThread;

    public ReceiverShowsUpFirst() {
        internalThread = new Thread(() -> runWork());
        internalThread.start();
    }

    public void runWork() {
        Scanner s1 = new Scanner(System.in);
        String temp;
        StringHandoffImpl h1 = new StringHandoffImpl();
        try {
            System.out.println("Press enter to begin test 4.");
            temp = s1.nextLine();
            noWaitTime(h1);
            System.out.println("Press enter to begin test 5.");
            temp = s1.nextLine();
            withWaitTimeFail(h1);
            System.out.println("Press enter to begin test 6.");
            temp = s1.nextLine();
            withWaitTime(h1);
        } catch (InterruptedException x) {
            x.printStackTrace();
        }

    }

    private void noWaitTime(StringHandoffImpl handoff) throws InterruptedException {
        System.out.println("--------Test 4: Receiver Shows Up First(no wait time)---------");
        Receiver r1 = new Receiver(handoff, 200L, "Thread B");
        Passer p1 = new Passer("Eggs", handoff, 2500L, "Thread A");
        Thread.sleep(4000L);
        p1.stopRequest();
        r1.stopRequest();
        if (!p1.waitUntilStopped(2000L)) System.out.println("!!! passer ignored stopRequest and is still stuck running");
        if (!r1.waitUntilStopped(2000L)) System.out.println("!!! receiver ignored stopRequest and is still stuck running");
        System.out.println("------End of Test 4------");
        System.out.println("===============================");
    }

    public void withWaitTime(StringHandoffImpl handoff) throws InterruptedException {
        System.out.println("-------Test 6: Receiver Shows Up First(with wait time)--------");
        Receiver r1 = new Receiver(handoff, 1000L, 200L, "Thread B");
        Passer p1 = new Passer("toast", handoff, 1000L, 500L, "Thread A");
        Thread.sleep(5000L);
        p1.stopRequest();
        r1.stopRequest();
        p1.stopRequest();
        r1.stopRequest();
        if (!p1.waitUntilStopped(2000L)) System.out.println("!!! passer ignored stopRequest and is still stuck running");
        if (!r1.waitUntilStopped(2000L)) System.out.println("!!! receiver ignored stopRequest and is still stuck running");
        System.out.println("------End of Test 6------");
        System.out.println("===============================");
        System.out.println("End of Tests");
    }

    public void withWaitTimeFail(StringHandoffImpl handoff) throws InterruptedException {
        System.out.println("-----Test 5: Receiver Shows Up First(with wait time: FAIL ON PURPOSE)------");
        Receiver r1 = new Receiver(handoff, 800L, 200L, "Thread B");
        Passer p1 = new Passer("hotdogs", handoff, 1000L, 1500L, "Thread A");
        Thread.sleep(5000L);
        p1.stopRequest();
        r1.stopRequest();
        p1.stopRequest();
        r1.stopRequest();
        if (!p1.waitUntilStopped(2000L)) System.out.println("!!! passer ignored stopRequest and is still stuck running");
        if (!r1.waitUntilStopped(2000L)) System.out.println("!!! receiver ignored stopRequest and is still stuck running");
        System.out.println("------End of Test 5------");
        System.out.println("===============================");
    }
}

