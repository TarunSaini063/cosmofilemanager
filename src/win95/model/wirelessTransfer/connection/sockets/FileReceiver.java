package win95.model.wirelessTransfer.connection.sockets;

import win95.model.wirelessTransfer.FileMetaData;
import win95.model.wirelessTransfer.connection.Common;
import win95.model.wirelessTransfer.connection.callbacks.FileCNF;
import win95.model.wirelessTransfer.iohandler.FileWriter;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class FileReceiver implements Runnable{
    private final int port = Common.port;
    private FileWriter fileWriter;
    private long size;
    FileCNF callback;
    public FileReceiver(FileMetaData fileMetaData,FileCNF callback,FileWriter fileWriter) {
        this.callback = callback;
        this.fileWriter = fileWriter;
        this.size = fileMetaData.getSize();
    }
    public void setCallBack(FileCNF callback){
        this.callback = callback;
    }
    public void receiveNextFile(FileWriter fileWriter, long size) {
        this.fileWriter = fileWriter;
        this.size = size;
    }

    private void doTransfer(final SocketChannel channel) throws IOException {
        assert !Objects.isNull(channel);

        this.fileWriter.transfer(channel, this.size);
    }

    @Override
    public void run() {
        SocketChannel channel = Common.server;
        try {
            try {
                doTransfer(channel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }finally {
            try {
                this.fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                callback.onReceived(fileWriter.getPath());
            }
        }
    }
}
