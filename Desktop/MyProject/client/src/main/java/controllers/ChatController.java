package controllers;

import java.io.*;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import net.Net;

public class ChatController implements Initializable {

    public Button downloadButton;
    public Button loadButton;
    private Net net;
    private Path path;
    public TextField input;
    public ListView<String> listView;

    /*Protocol:
     * 1.Receiver
     * 2.Sender
     * 3.DataType
     * 4.Data
     * */

    public void sendMessage() throws IOException {
        net.sendMessage("192.168.0.102/" + getMyIP() + "/msg/" + input.getText());
        input.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadButton.setVisible(false);
        downloadButton.setVisible(false);
        try {
            net = Net.start(2021);
            path = Paths.get("client", "myCloudFiles");
            Thread readThread = new Thread(() -> {
                try {
                    while (true) {
                        String pack = net.getMessage();
                        String[] packArr = pack.split("/");
                        if (packArr[0].equals(getMyIP())) {
                            switch (packArr[2]) {
                                case "takeFiles": {
                                    printList(packArr[3]);
                                    break;
                                }
                                case "download": {
                                    downloadFileFromServer(packArr[3]);
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Exception while reading");
                }
            });
            readThread.setDaemon(true);
            readThread.start();
        } catch (IOException ioException) {
            System.err.println("Connection was broken");
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

    public void getMyFiles() {
        loadButton.setVisible(true);
        downloadButton.setVisible(false);
        try {
            String[] file = Files.list(path)
                    .map(path -> path.getFileName().toString())
                    .collect(Collectors.joining(",")).split(",");
            Platform.runLater(() -> {
                listView.getItems().clear();
                listView.getItems().addAll(file);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getList() throws IOException {
        loadButton.setVisible(false);
        downloadButton.setVisible(true);
        net.sendMessage("192.168.0.102/" + getMyIP() + "/getListFiles/NoN");
        input.clear();
    }

    private void printList(String data) {
        String[] names = data.split(",");
        Platform.runLater(() -> {
            listView.getItems().clear();
            listView.getItems().addAll(names);
        });
    }

    private void downloadFileFromServer(String filename) {
        try {
            String fileName = path + "/" + filename;
            FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bof = new BufferedOutputStream(fos);
            byte[] buffer = new byte[1024];
            int byteReads = net.getIO().read(buffer, 0, buffer.length);
            int count = byteReads;
            if (count >= buffer.length) {
                do {
                    byteReads = net.getIO().read(buffer, count, (buffer.length - count));
                    if (byteReads >= 0)
                        count += byteReads;
                } while (byteReads > -1);

            }
            bof.write(buffer, 0, count);
            bof.flush();
            bof.close();
            Platform.runLater(()->{
                listView.getItems().clear();
                listView.getItems().add("Download " + fileName + " complete.");
            });

        } catch (IOException e) {
            System.err.println("Downloading fail");
        }
    }

    public void downloadFile() throws IOException {
        String file = listView.getSelectionModel().getSelectedItem();
        net.sendMessage("192.168.0.102/" + getMyIP() + "/download/" + file);
    }

    public void loadFile() {
        int count;
        String fileName=listView.getSelectionModel().getSelectedItem();
        File file = new File(path + "/" + fileName);
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream bis = null;
        try {
            String pack = "192.168.0.102/" + getMyIP() + "/load/" + fileName;
            net.sendMessage(pack);
            bis = new BufferedInputStream(new FileInputStream(file));
            count = bis.read(buffer, 0, buffer.length);
            net.getOu().write(buffer, 0, count);
            net.getOu().flush();
            Platform.runLater(()->{
                listView.getItems().clear();
                listView.getItems().add("Load " + fileName + " complete.");
            });
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
}
