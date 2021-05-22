package NIO;

import NIO.FileSystemReader.CloudWatcher;

import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) {
        try {
            CloudWatcher.getInstance();
        } catch (IOException e) {
            System.err.println("Cloud watchService down");
        }
        ServerNIO server = ServerNIO.getInstance();
        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Server Broken");
        }
    }
}
