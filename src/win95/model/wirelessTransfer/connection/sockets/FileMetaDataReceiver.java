package win95.model.wirelessTransfer.connection.sockets;

import win95.model.wirelessTransfer.FileMetaData;
import win95.model.wirelessTransfer.connection.Common;
import win95.model.wirelessTransfer.connection.callbacks.FileMetaDataCallBack;

import java.io.*;
import java.net.Socket;

public class FileMetaDataReceiver implements Runnable {
    FileMetaDataCallBack callBack;

    public FileMetaDataReceiver(FileMetaDataCallBack callBack) {
        this.callBack = callBack;
    }
    Socket socket = null;
    InputStream inputStream = null;
    ObjectInputStream objectInputStream = null;
    OutputStream outputStream = null;
    ObjectOutputStream objectOutputStream = null;
    @Override
    public void run() {
        if(Common.CLIENT){
            objectInputStream = Common.clientObjectInputStream;
        }else{
            objectInputStream = Common.serverObjectInputStream;
        }
        System.out.println("Connected!");

        assert objectInputStream!=null;
        System.out.println("get objectInputStream ... "+objectInputStream.toString());
        while (true){
            FileMetaData fileMetaData;
            try {
                System.out.println("waiting in while loop for object receiver thread for meta info");
                fileMetaData = (FileMetaData)objectInputStream.readObject();
                System.out.println("meta info received :  "+fileMetaData.toString());
                callBack.onReceived(fileMetaData);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                callBack.onReceived(null);
                return;
            }

        }

    }
}
