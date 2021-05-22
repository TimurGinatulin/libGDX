package handlers;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable, Closeable {

    private final Socket socket;
    private final Path serverPath;
    private DataInputStream is;
    private DataOutputStream out;
    private OutputStream ou;
    private InputStream in;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        serverPath = Paths.get("server", "server_files");
        try {
            is = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            ou = socket.getOutputStream();
            in = socket.getInputStream();
        } catch (IOException e) {
            System.err.println("Error in connection to server");
        }
    }

    @Override
    public void run() {
        System.out.println("Start listening...");
        try {
            while (true) {
                /*Protocol:
                 * 1.Receiver
                 * 2.Sender
                 * 3.DataType
                 * 4.Data
                 * */
                String pack = is.readUTF();
                String[] packArr = pack.split("/");
                System.out.println("Received package by " + packArr[1] + "\n" +
                        "Data type: " + packArr[2] + "\n" + "Data: " + packArr[3]);
                if (packArr[0].equals(getMyIP())) {
                    switch (packArr[2]) {
                        case "getListFiles": {
                            String sPack = packArr[1] + "/" + getMyIP() + "/takeFiles/" + getFiles();
                            out.writeUTF(sPack);
                            out.flush();
                            break;
                        }
                        case "download": {
                            sendFile(packArr[3], packArr[1]);
                            break;
                        }
                        case "load": {
                            receiveFile(packArr[3]);
                            break;
                        }
                        default: {
                            out.writeUTF(packArr[3]);
                            out.flush();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Connection was broken");
            try {
                close();
            } catch (Exception ioException) {
                System.err.println("Exception while socket close");
            }
        } finally {
            try {
                close();
                System.out.println("Finish listening");
            } catch (Exception ioException) {
                System.err.println("Exception while socket close");
            }
        }
    }

    private void receiveFile(String filename) { try {
        String fileName = serverPath + "/" + filename;
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bof = new BufferedOutputStream(fos);
        byte[] buffer = new byte[1024];
        int byteReads = in.read(buffer, 0, buffer.length);
        int count = byteReads;
        if (count >= buffer.length) {
            do {
                byteReads = in.read(buffer, count, (buffer.length - count));
                if (byteReads >= 0)
                    count += byteReads;
            } while (byteReads > -1);

        }
        bof.write(buffer, 0, count);
        bof.flush();
        bof.close();
    } catch (IOException e) {
        System.err.println("Downloading fail");
    }
    }

    public String getFiles() throws IOException {
        return Files.list(serverPath)
                .map(path -> path.getFileName().toString())
                .collect(Collectors.joining(","));
    }

    public void sendFile(String fileName, String receiver) {
        File file = new File(serverPath + "/" + fileName);
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream bis = null;
        try {
            String pack = getMyIP() + "/" + receiver + "/download/" + fileName;
            out.writeUTF(pack);
            bis = new BufferedInputStream(new FileInputStream(file));
            bis.read(buffer, 0, buffer.length);
            ou.write(buffer, 0, buffer.length);
            ou.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getMyIP() {
        try {
            Enumeration<NetworkInterface> e = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface = e.nextElement();
            Enumeration<InetAddress> ee = networkInterface.getInetAddresses();
            ee.nextElement();
            InetAddress address = ee.nextElement();
            return address.getHostAddress();
        } catch (SocketException socketException) {
            socketException.printStackTrace();
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }
}
