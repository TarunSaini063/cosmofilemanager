package win95.model.wirelessTransfer.connection;

import win95.model.wirelessTransfer.FileMetaData;
import win95.model.wirelessTransfer.connection.callbacks.ConnectionCNF;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;
import win95.model.wirelessTransfer.connection.sockets.FileMetaDataReceiver;
import win95.model.wirelessTransfer.connection.sockets.FileReceiver;
import win95.model.wirelessTransfer.iohandler.FileWriter;
import win95.model.wirelessTransfer.terminate.PublicThreads;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

public class InitConnectionServer implements Runnable {
    ConnectionCNF callback;
    FileMetaDataCallBack fileMetaDataCallBack = new FileMetaDataCallBack() {
        @Override
        public void onReceived(FileMetaData fileMetaData) {
            if (fileMetaData != null) {
                System.out.println("in receiver side metaData : " + fileMetaData.toString());
                try {
                    Common.transfer.appendListView(fileMetaData);
                    System.out.println("writing data at " + Common.receivedPath + fileMetaData.getName());
                    FileWriter fileWriter = new FileWriter(Common.receivedPath + fileMetaData.getName());
                    FileReceiver fileReceiver = new FileReceiver(fileMetaData, Common.transfer.fileCNFCallback, fileWriter);
                    Thread receiverThread = new Thread(fileReceiver);
                    receiverThread.start();
                    PublicThreads.add(receiverThread);
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


    public InitConnectionServer(ConnectionCNF callback) {
        this.callback = callback;
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(Common.metaPort);
            System.out.println("waiting for client to connect with meta server");
            Socket serverMeta = serverSocket.accept();
            System.out.println("meta server get connected with client");
            Common.serverMeta = serverMeta;
            OutputStream outputStream = serverMeta.getOutputStream();
            Common.serverObjectOutputStream = new ObjectOutputStream(outputStream);
            InputStream inputStream = serverMeta.getInputStream();
            Common.serverObjectInputStream = new ObjectInputStream(inputStream);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        System.out.println("starting serverChanel after successful meta link");
        try (
                ServerSocketChannel server = ServerSocketChannel.open()) {
            server.configureBlocking(true);
            server.socket().bind(new InetSocketAddress(Common.port));
            Common.serverSocketChannel = server;
            Common.server = server.accept();
            System.out.println("Server Connected\npreparing thread for meta data");
            FileMetaDataReceiver fileMetaDataReceiver = new FileMetaDataReceiver(fileMetaDataCallBack);
            Thread thread = new Thread(fileMetaDataReceiver);
            System.out.println("Starting reader thread in server");
            thread.start();
            PublicThreads.add(thread);
            System.out.println("reader thread started successfully in server");
            callback.serverConnected("SUCCESS");

        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }
}
