package win95.model.wirelessTransfer.connection.sockets;

import win95.model.wirelessTransfer.connection.Common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class FileSender {
    private final InetSocketAddress hostAddress;
    private SocketChannel client;

    public FileSender() throws IOException {
        this.hostAddress = Common.hostAddress;
        this.client = Common.client;
    }

    public void transfer(final FileChannel channel, long position, long size) throws IOException {
        assert !Objects.isNull(channel);
        while (position < size) {
            position += channel.transferTo(position, Common.TRANSFER_MAX_SIZE, this.client);
        }
    }

    SocketChannel getChannel() {
        return this.client;
    }

    public void close() throws IOException {
        this.client.close();
    }
}
