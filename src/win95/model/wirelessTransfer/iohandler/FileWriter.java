package win95.model.wirelessTransfer.iohandler;

import org.apache.commons.lang.StringUtils;
import win95.model.wirelessTransfer.connection.Common;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

public class FileWriter {
    private final FileChannel channel;
    String path;
    public FileWriter(final String path) throws IOException {
        if (StringUtils.isEmpty(path)) {
            throw new IllegalArgumentException("path required");
        }
        this.path = path;
        this.channel = FileChannel.open(Paths.get(path), StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
    }

    public String getPath() {
        return path;
    }

    public void transfer(final SocketChannel channel, final long bytes) throws IOException {
        assert !Objects.isNull(channel);

        long position = 0l;
        long  tmp =0;
        while (position < bytes) {
            tmp+=bytes;
            position += this.channel.transferFrom(channel, position, Common.TRANSFER_MAX_SIZE);

        }
    }

    int write(final ByteBuffer buffer, long position) throws IOException {
        assert !Objects.isNull(buffer);

        int bytesWritten = 0;
        while(buffer.hasRemaining()) {
            bytesWritten += this.channel.write(buffer, position + bytesWritten);
        }

        return bytesWritten;
    }

    public void close() throws IOException {
        this.channel.close();
    }

}
