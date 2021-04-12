package win95.model.wirelessTransfer.connection.callbacks;


public interface FileCNF {
    void onReceived(String fileName);
    void onSend(String fileMetaData);
}
