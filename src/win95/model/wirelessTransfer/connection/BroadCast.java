package win95.model.wirelessTransfer.connection;

import win95.model.wirelessTransfer.connection.callbacks.ConnectionCNF;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;
import win95.model.wirelessTransfer.connection.sockets.FindServer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BroadCast {

    private static final int MAX_T = 255;
    static ExecutorService pool = Executors.newFixedThreadPool(MAX_T);

    public static void main(ConnectionCNF callback, FileMetaDataCallBack fileMetaDataCallBack, int j) throws IOException {
        ArrayList<Thread> arrayList = new ArrayList<>();
        for (int i = 1; i <= 254 && Common.ip.equals("localhost"); i++) {
            final String iIPv4 = "192.168." + j + "." + i;
            FindServer findServer = new FindServer(callback, fileMetaDataCallBack, iIPv4);
            Thread thread = new Thread(findServer);
            arrayList.add(thread);
            thread.start();
//            pool.execute(thread);
        }
        System.out.println("Waiting in broadcaster");
        for(Thread thread : arrayList){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Exiting broadcaster");
//        pool.shutdown();
//        try {
//            System.out.println("Waiting");
//            pool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
//            System.out.println("Waiting finish");
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        try {
//            if (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
//                pool.shutdownNow();
//            }
//        } catch (InterruptedException ex) {
//            pool.shutdownNow();
//            Thread.currentThread().interrupt();
//        }
    }

}
