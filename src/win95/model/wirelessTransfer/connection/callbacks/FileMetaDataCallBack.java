package win95.model.wirelessTransfer.connection.callbacks;

import win95.model.wirelessTransfer.FileMetaData;

import java.io.IOException;

public interface FileMetaDataCallBack {
    void onReceived(FileMetaData message);

    void onSend(FileMetaData message) throws IOException;
}
