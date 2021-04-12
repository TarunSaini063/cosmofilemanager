package win95.model.wirelessTransfer.connection;

import win95.model.wirelessTransfer.connection.callbacks.ConnectionCNF;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;
import win95.model.wirelessTransfer.connection.sockets.FileMetaDataReceiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BroadCast {


    private static final int MAX_T = 1000;

    public static void main(ConnectionCNF callback, FileMetaDataCallBack fileMetaDataCallBack) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
        for (int j = 254; j >= 1 && Common.ip.equals("localhost"); j--) {
            for (int i = 1; i <= 254 && Common.ip.equals("localhost"); i++) {
                final String iIPv4 = "192.168." + j + "." + i;

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("Waiting for server to make meta link : " + iIPv4);
                        SocketChannel client = null;
                        Socket socket = null;
                        boolean status = true;
                        boolean metaStatus = true;
                        long start = System.currentTimeMillis();
                        long end = start + 100 * 1000;
                        while (System.currentTimeMillis() < end) {
                            try {
                                socket = new Socket(iIPv4, Common.metaPort);
                                break;
                            } catch (IOException e) {
                                System.out.println("waiting for server : " + iIPv4 + " " + e.getMessage());
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            }
                        }
                        assert socket != null;
                        Common.ip = iIPv4;
                        System.out.println("Connected with : " + iIPv4);
                        try {
                            if (Common.clientObjectOutputStream == null) {
                                Common.clientObjectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                        try {
                            if (Common.clientObjectInputStream == null) {
                                Common.clientObjectInputStream = new ObjectInputStream(socket.getInputStream());
                            }
                        } catch (IOException e) {
                            System.out.println(e.getMessage());
                            return;
                        }

                        while (status) {
                            try {
                                client = SocketChannel.open();
                                client.configureBlocking(true);
                                client.connect((new InetSocketAddress(Common.ip, Common.port)));
                                status = false;
                                Common.client = client;
                                FileMetaDataReceiver fileMetaDataReceiver = new FileMetaDataReceiver(fileMetaDataCallBack);
                                Thread thread = new Thread(fileMetaDataReceiver);
                                System.out.println("Starting reader thread in client");
                                thread.start();
                                System.out.println("reader thread started successfully in client");
                                callback.clientConnected("SUCCESS");
                            } catch (ConnectException e) {
                                System.out.println("Error while connecting with " + Common.ip + " " + e.getMessage());
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException interruptedException) {
                                    interruptedException.printStackTrace();
                                }
                            } catch (SocketTimeoutException e) {
                                System.out.println("Connection: " + e.getMessage() + ".");
                                System.out.println("retrying in 1 sec");
                            } catch (IOException e) {
                                e.printStackTrace();
                                callback.clientConnected("FAILURE");
                                return;
                            }
                        }
                    }
                });

                pool.execute(thread);
            }
        }
        pool.shutdown();
    }

}
