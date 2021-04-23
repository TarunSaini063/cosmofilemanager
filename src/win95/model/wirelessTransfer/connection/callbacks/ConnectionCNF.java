package win95.model.wirelessTransfer.connection.callbacks;

public interface ConnectionCNF {
    void clientConnected(String message);

    void serverConnected(String message);
}
