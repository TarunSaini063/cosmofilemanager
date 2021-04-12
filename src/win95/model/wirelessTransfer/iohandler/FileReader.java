package win95.model.wirelessTransfer.iohandler;

import org.apache.commons.lang.StringUtils;
import win95.model.wirelessTransfer.connection.sockets.FileSender;
import win95.model.wirelessTransfer.connection.callbacks.FileCNF;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class FileReader implements Runnable {
    private final FileChannel channel;
    private final FileSender sender;
    private FileCNF callback;
    private String path;
    public FileReader( FileSender sender,  String path,FileCNF callback) throws IOException {
        System.out.println("in fileReader class path : "+path);
        if (Objects.isNull(sender)){
            throw new IllegalArgumentException("sender required");
        }
        if(StringUtils.isEmpty(path)){
            throw new IllegalArgumentException(" path required");

        }

        this.sender = sender;
        this.channel = FileChannel.open(Paths.get(path), StandardOpenOption.READ);
        this.callback = callback;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void read() throws IOException {
        try {
            transfer();
        } finally {
            close();
        }
    }

    void close() throws IOException {
        this.channel.close();
    }

    private void transfer() throws IOException {
        this.sender.transfer(this.channel, 0l, this.channel.size());
    }

    @Override
    public void run() {
        try {
            transfer();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                close();
                callback.onSend(path);
            } catch (IOException e) {
                e.printStackTrace();
                callback.onSend(path);
            }
        }
    }
}
