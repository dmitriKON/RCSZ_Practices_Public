package practice3;

import practice3.packet.PacketBuilder;
import practice3.packet.PacketReceiver;
import practice3.packet.Processor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TCPServer extends Thread {

    private ServerSocket serverSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(6666);
        while (true)
            new EchoClientHandler(serverSocket.accept()).start();
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    @Override
    public void run() {
        TCPServer server = new TCPServer();
        try {
            server.start(6666);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static class EchoClientHandler extends Thread {
        private Socket clientSocket;
        private InputStream in;
        private OutputStream out;

        private PacketReceiver packet;

        public EchoClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                in = new DataInputStream(clientSocket.getInputStream());
                out = new DataOutputStream(clientSocket.getOutputStream());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte buffer[] = new byte[1024];
                baos.write(buffer, 0 , in.read(buffer));
                byte result[] = baos.toByteArray();
                packet = new PacketReceiver(result);
                String response = Processor.process(packet.getMessage());
                PacketBuilder pb = new PacketBuilder(response, 0, 6666);
                out.write(pb.getPacketBytes());

            } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                    InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            }

        }
    }

}
