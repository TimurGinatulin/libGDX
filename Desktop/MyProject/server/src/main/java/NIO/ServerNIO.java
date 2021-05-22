package NIO;

import NIO.FileSystemReader.FileSystemUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.Set;

@Slf4j
public class ServerNIO {
    private static ServerNIO instance;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;
    private ByteBuffer byteBuffer;
    private final FileSystemUtils fileSystemUtils;
    private Path path;

    private ServerNIO() {
        fileSystemUtils = new FileSystemUtils();
        path = Path.of(System.getProperty("user.home"));
    }

    public static ServerNIO getInstance() {
        if (instance == null)
            instance = new ServerNIO();
        return instance;
    }

    public void start() throws IOException {
        byteBuffer = ByteBuffer.allocate(256);
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(8081));
        serverSocketChannel.configureBlocking(false);
        //log.debug("Server Started");
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (serverSocketChannel.isOpen()) {
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isValid()) {
                    if (key.isAcceptable()) {
                        accept(key);
                    }
                    if (key.isReadable()) {
                        read(key);
                    }
                }
                iterator.remove();
            }
        }
    }

    private void accept(SelectionKey key) {
        try {
            SocketChannel socketChannel = serverSocketChannel.accept();
            socketChannel.configureBlocking(false);
            //log.debug("Connected " + socketChannel.getRemoteAddress());
            socketChannel.register(selector, SelectionKey.OP_READ, "root");
            sendMessage("", socketChannel, key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) {
        if (key.isValid()) {
            SocketChannel channel = (SocketChannel) key.channel();
            StringBuilder reader = new StringBuilder();
            int readByte;
            try {

                while (true) {
                    readByte = channel.read(byteBuffer);
                    if (readByte == 0) {
                        break;
                    }
                    if (readByte == -1) {
                        channel.close();
                    }
                    byteBuffer.flip();
                    while (byteBuffer.hasRemaining()) {
                        reader.append((char) byteBuffer.get());
                    }
                    byteBuffer.clear();
                    String[] msg = reader.toString().trim().split(" ");
                    switch (msg[0]) {
                        case "help": {
                            String helpMsg = "Welcome to home server v.Alpha\n Commands:" +
                                    "\nhelp->help menu." +
                                    "\nls->show files and directory." +
                                    "\ncd [path]-> change directory to path." +
                                    "\ncat [path]-> read file path." +
                                    "\ntouch [path]-> create file path." +
                                    "\nedit [path]-> edit file path." +
                                    "\nmake dir [path]-> create directory path.";
                            sendMessage(helpMsg, channel, key);
                            break;
                        }
                        case "ls": {
                            sendMessage(
                                    fileSystemUtils.getAllFilesAtDirToString(new File(String.valueOf(path))),
                                    channel,
                                    key);
                            break;
                        }
                        case "cd": {
                            path = fileSystemUtils.cd(msg[1], path);
                            sendMessage("", channel, key);
                            break;
                        }
                        case "cat": {
                            if (msg.length > 1) {
                                String readFile = fileSystemUtils.readAsString(path, msg[1]);
                                if (!readFile.equals("NoN")) {
                                    sendMessage(readFile, channel, key);
                                    break;
                                }
                            }
                            sendMessage("File not Found", channel, key);
                        }
                        case "touch": {
                            if (msg.length > 1) {
                                if (fileSystemUtils.createFile(path, msg[1]))
                                    sendMessage("OK", channel, key);
                                else
                                    sendMessage("DOWN", channel, key);
                            }
                            break;
                        }
                        case "edit": {
                            if (msg.length > 2) {
                                StringBuilder userMsg = new StringBuilder();
                                for (int i = 2; i < msg.length; i++) {
                                    userMsg.append(msg[i]).append(" ");
                                }
                                fileSystemUtils.write(path, msg[1], userMsg.toString());
                                sendMessage("OK", channel, key);
                                break;
                            }
                        }
                        case "make": {
                            if (msg.length > 2) {
                                if (msg[1].equals("dir")) {
                                    if (fileSystemUtils.createDir(path, msg[2]))
                                        sendMessage("OK", channel, key);
                                    else
                                        sendMessage("Failed", channel, key);
                                }
                                break;
                            }
                        }
                        default: {
                            channel.write(ByteBuffer.wrap(("Uncorrected command. Print \"help\"".getBytes(StandardCharsets.UTF_8))));
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                // log.error(e.toString());
            }
        }
    }

    private void sendMessage(String msg, SocketChannel channel, SelectionKey key) throws IOException {
        channel.write(ByteBuffer.wrap((msg + "\n" +
                key.attachment() + "@" + channel.getRemoteAddress() + ":" + path + "# ").
                getBytes(StandardCharsets.UTF_8)));
    }
}