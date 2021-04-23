package win95.model.wirelessTransfer.terminate;

import java.util.ArrayList;

public class PublicThreads {
    static ArrayList<Thread> userThread = new ArrayList<>();

    public static void clearThreads() {
        for (Thread thread : userThread) {
            if (thread.isAlive()) {
                thread.interrupt();
            }
        }
    }

    public static void add(Thread thread) {
        userThread.add(thread);
    }
}
