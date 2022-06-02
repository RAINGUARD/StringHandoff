package Drivers;
/**
* @author Neil Haggerty
*
* This program can pass a String from one thread to another. It is basic exercise in multithreading.
* It is safe to use with multiple threads and will not deadlock.
* It creates a single instance of the StringHandoff class, and all threads operate on that instance.
*
* This is the main driver. Run this to start the program.
*/

public class Driver {

    public static void main(String[] args) {
        PasserShowsUpFirst p1 = new PasserShowsUpFirst();

    }
}
