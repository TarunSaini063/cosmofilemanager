package win95.model.wirelessTransfer.connection.sockets;

import win95.model.wirelessTransfer.connection.Common;
import win95.model.wirelessTransfer.connection.callbacks.ConnectionCNF;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;
import win95.model.wirelessTransfer.terminate.PublicThreads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.SocketChannel;

public class FindServer implements Runnable{
    ConnectionCNF callback;
    FileMetaDataCallBack fileMetaDataCallBack;
    String iIPv4;
    public FindServer(ConnectionCNF callback, FileMetaDataCallBack fileMetaDataCallBack,String iIPv4) {
        this.callback = callback;
        this.fileMetaDataCallBack = fileMetaDataCallBack;
        this.iIPv4 = iIPv4;
    }

    @Override
    public void run() {
        System.out.println("start meta link with : " + iIPv4);
        SocketChannel client = null;
        Socket socket = null;
        boolean status = true;
        boolean metaStatus = true;
        long start = System.currentTimeMillis();
        long end = start + 1000;
        end = 0;
        while (end<2) {
            end++;
            try {
                socket = new Socket(iIPv4, Common.metaPort);
                System.out.println(socket);
                break;
            } catch (IOException e) {
                System.out.println("waiting for server : " + iIPv4 +" end = "+end);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
        if(socket == null) {
            return;
        }
        Common.ip = iIPv4;
        System.out.println("Connected with : " + iIPv4);
        try {
            if (Common.clientObjectOutputStream == null) {
                Common.clientObjectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }catch (NullPointerException e){
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
        callback.clientConnected(iIPv4);
        int trying = 0;
        while (status&&trying<20) {
            try {
                trying++;
                client = SocketChannel.open();
                client.configureBlocking(true);
                client.connect((new InetSocketAddress(Common.ip, Common.port)));
                status = false;
                Common.client = client;
                FileMetaDataReceiver fileMetaDataReceiver = new FileMetaDataReceiver(fileMetaDataCallBack);
                Thread thread = new Thread(fileMetaDataReceiver);
                System.out.println("Starting reader thread in client");
                thread.start();
                PublicThreads.add(thread);
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
}
