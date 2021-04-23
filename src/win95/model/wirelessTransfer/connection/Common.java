package win95.model.wirelessTransfer.connection;

import win95.model.wirelessTransfer.connection.sockets.FileReceiver;
import win95.model.wirelessTransfer.connection.sockets.FileSender;
import win95.controller.Transfer;
import win95.model.wirelessTransfer.ConnectionIO;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.FileSystems;

public class Common {
    public static boolean CLIENT = false;
    public static ConnectionIO CONNECTION_STATUS = ConnectionIO.BREAK;
    public static String ip = "localhost";//"192.168.137.1";
    public static Transfer transfer;
    public static int port = 49452;
    public static int metaPort = 47452;
    public static FileSender fileSender = null;
    public static Socket metaClient = null;
    public static Socket metaServer = null;
    public static FileReceiver fileReceiver = null;
    public static ServerSocketChannel serverSocketChannel = null;
    public static SocketChannel server = null;
    public static InetSocketAddress hostAddress = null;
    public static SocketChannel client = null;
    public static ObjectOutputStream clientObjectOutputStream;
    public static ObjectInputStream clientObjectInputStream;
    public static ObjectOutputStream serverObjectOutputStream;
    public static ObjectInputStream serverObjectInputStream;
    public static String receivedPath = new File(System.getProperty("user.home")).getAbsolutePath() + FileSystems.getDefault().getSeparator() + "Downloads/";
    public static Socket serverMeta;
    public static final String INSTANTIATION_NOT_ALLOWED = "Instantiation not allowed";
    public static final long TRANSFER_MAX_SIZE = (1024 * 1024);

    private Common() {
        throw new IllegalStateException(INSTANTIATION_NOT_ALLOWED);
    }
}
