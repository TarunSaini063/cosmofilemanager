package win95.model.wirelessTransfer.connection;

import win95.model.wirelessTransfer.FileMetaData;
import win95.model.wirelessTransfer.connection.callbacks.ConnectionCNF;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;
import win95.model.wirelessTransfer.connection.sockets.FileMetaDataReceiver;
import win95.model.wirelessTransfer.connection.sockets.FileReceiver;
import win95.model.wirelessTransfer.iohandler.FileWriter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.channels.SocketChannel;

public class InitConnectionClientIP implements Runnable{
    ConnectionCNF callback;
    FileMetaDataCallBack fileMetaDataCallBack = new FileMetaDataCallBack() {
        @Override
        public void onReceived(FileMetaData fileMetaData) {
            if (fileMetaData != null) {
                System.out.println("in receiver side metaData : " + fileMetaData.toString());
                try {
                    System.out.println("writing data at " + Common.receivedPath + fileMetaData.getName());
                    FileWriter fileWriter = new FileWriter(Common.receivedPath + fileMetaData.getName());
                    FileReceiver fileReceiver = Common.fileReceiver;
                    fileReceiver.receiveNextFile(fileWriter, fileMetaData.getSize());
                    Thread receiverThread = new Thread(fileReceiver);
                    receiverThread.start();
                    System.out.println("Started Receiving data");
                } catch (IOException e) {
                    System.out.println("this file is already exist ");
                    e.printStackTrace();
                }
            } else {
                System.out.println("Error in meta sending");
            }

        }

        @Override
        public void onSend(FileMetaData fileMetaData) {

        }

    };

    public InitConnectionClientIP(ConnectionCNF callback) {
        this.callback = callback;
    }
    @Override
    public void run() {
        System.out.println("start meta link with : " + Common.ip);
        SocketChannel client = null;
        Socket socket = null;
        boolean status = true;
        boolean metaStatus = true;
        int tries = 0;
        while (metaStatus&&tries<20) {
            tries++;
            try {
                socket = new Socket(Common.ip, Common.metaPort);
                metaStatus = false;
                break;
            } catch (IOException e) {
                System.out.println("waiting for server : " + Common.ip);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        }
        if(tries==20&&socket==null){
            callback.clientConnected("FAILURE");
        }
        System.out.println("Connected with : " + Common.ip);
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
        callback.clientConnected(Common.ip);

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
}
