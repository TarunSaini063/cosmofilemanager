package win95.model.wirelessTransfer.connection;

import win95.model.wirelessTransfer.FileMetaData;
import win95.model.wirelessTransfer.connection.callbacks.ConnectionCNF;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;
import win95.model.wirelessTransfer.connection.sockets.FileReceiver;
import win95.model.wirelessTransfer.iohandler.FileWriter;

import java.io.IOException;

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

    }


}
