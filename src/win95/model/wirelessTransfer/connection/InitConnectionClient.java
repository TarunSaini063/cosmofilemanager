package win95.model.wirelessTransfer.connection;

import win95.model.wirelessTransfer.FileMetaData;
import win95.model.wirelessTransfer.connection.callbacks.ConnectionCNF;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;
import win95.model.wirelessTransfer.connection.sockets.FileReceiver;
import win95.model.wirelessTransfer.iohandler.FileWriter;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public class InitConnectionClient implements Runnable {
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

    public InitConnectionClient(ConnectionCNF callback) {
        this.callback = callback;
    }

    @Override
    public void run() {

        SocketChannel client = null;
//        Socket socket = null;
//        boolean metaStatus = true;
//        while (metaStatus) {
//            try {
//                System.out.println("Waiting for server to make meta link");
//                socket = new Socket(Common.ip, Common.metaPort);
//                metaStatus = false;
//            } catch (IOException e) {
//                System.out.println("waiting for server " + e.getMessage());
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException interruptedException) {
//                    interruptedException.printStackTrace();
//                }
//            }
//        }
//        assert socket != null;
//        try {
//            Common.clientObjectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//        } catch (IOException e) {
////            e.printStackTrace();
//            System.out.println(e.getMessage());
//            return;
//        }
//        try {
//            Common.clientObjectInputStream = new ObjectInputStream(socket.getInputStream());
//        } catch (IOException e) {
////            e.printStackTrace();
//            System.out.println(e.getMessage());
//            return;
//        }

        int count = 1;
        while (count<=10&&Common.ip.equals("localhost")) {
            System.out.println("Broadcasting for turn : "+count);
            try {
                BroadCast.main(callback, fileMetaDataCallBack);
                count++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(Common.ip.equals("localhost")){
            System.out.println("Request time out");
        }
        /*boolean lock = false;
        while (!lock);
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
                System.out.println("Error while connecting with "+Common.ip+" " + e.getMessage());
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
        }*/
    }


}
