import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

public class Server {
    private DatagramSocket socket;
    private byte[] buffer;
    private DatagramPacket packet;

    public void init() {
        try {
            socket = new DatagramSocket(4569);
            buffer = new byte[256];
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        String message = "Hello world ";
        while (true) {
            buffer = message.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.send(packet);
        }
    }
}
