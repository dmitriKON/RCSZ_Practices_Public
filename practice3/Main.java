package practice3;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        Thread.sleep(100);
        UDPClient udpClient = new UDPClient();
        UDPServer udpServer= new UDPServer();
        udpClient.sendMessage("hello", 1, 2);
        udpServer.run();
        TCPClient tcpClient = new TCPClient();
        TCPServer tcpServer = new TCPServer();
        tcpClient.startConnection(777);
        tcpClient.sendMessage("hello", 1, 4);
        tcpServer.start(777);
        tcpServer.close();
        tcpClient.stopConnection();


    }

}
