package win95.model.wirelessTransfer.connection.sockets;

import win95.model.wirelessTransfer.FileMetaData;
import win95.model.wirelessTransfer.connection.Common;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class FileMetaDataSender implements Runnable{
    FileMetaDataCallBack callBack;
    FileMetaData fileMetaData;
    public FileMetaDataSender(FileMetaDataCallBack callBack, FileMetaData fileMetaData) {
        this.callBack = callBack;
        this.fileMetaData = fileMetaData;
    }

    @Override
    public void run() {
        ObjectOutputStream objectOutputStream = null;
        System.out.println("fetching object output stream ");
        if(Common.CLIENT) {
                System.out.println("Waiting in client");
                objectOutputStream = Common.clientObjectOutputStream;
        }else{
                objectOutputStream = Common.serverObjectOutputStream;
        }
        assert objectOutputStream != null;
        System.out.println("get object output steam... "+objectOutputStream.toString());
        try {
            System.out.println("writing object");
            objectOutputStream.writeObject(fileMetaData);
            System.out.println("object send successfully");
            objectOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            try {
                callBack.onSend(null);
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return;
        }
        try {
            callBack.onSend(fileMetaData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
