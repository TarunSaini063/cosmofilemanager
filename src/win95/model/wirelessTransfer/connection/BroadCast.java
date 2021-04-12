package win95.model.wirelessTransfer.connection;

import win95.model.wirelessTransfer.connection.callbacks.ConnectionCNF;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;
import win95.model.wirelessTransfer.connection.sockets.FileMetaDataReceiver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BroadCast {


    private static final int MAX_T = 1000;
    public static void main(ConnectionCNF callback, FileMetaDataCallBack fileMetaDataCallBack) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_T);
        for (int j = 1; j < 255; j++) {
            for (int i = 1; i < 255; i++) {
                final String iIPv4 = "192.168." + j + "." + i;

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        System.out.println("Waiting for server to make meta link : " + iIPv4);

                        try {
                            // First check if IP is reachable at all.
                            InetAddress ip = InetAddress.getByName(iIPv4);
                            int timeout = 1500;
                            if (!ip.isReachable(timeout)) {
                                return;
                            }

                            // Address is reachable -> try connecting to socket.
                            Socket socket = new Socket();
                            SocketAddress address = new InetSocketAddress(ip, Common.metaPort);
                            socket.connect(address, timeout);
                            assert socket != null;
                            try {
                                Common.clientObjectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                                return;
                            }
                            try {
                                Common.clientObjectInputStream = new ObjectInputStream(socket.getInputStream());
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                            SocketChannel client = null;
                            Common.ip = iIPv4;
                            boolean status = true;
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

                        } catch (IOException ignored) {
                        }
                    }
                });

                pool.execute(thread);
            }
        }
        pool.shutdown();
    }

}
