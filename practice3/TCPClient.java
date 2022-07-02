package practice3;

import practice3.packet.PacketBuilder;
import practice3.packet.PacketReceiver;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TCPClient {

    private Socket clientSocket;
    private OutputStream out;
    private InputStream in;

    public void startConnection(int port) throws IOException {
        clientSocket = new Socket(InetAddress.getLocalHost().getHostAddress(), port);
        out = new DataOutputStream(clientSocket.getOutputStream());
        in = new DataInputStream(clientSocket.getInputStream());
    }

    public String sendMessage(String msg, int cType, int bUserId) throws InterruptedException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        PacketBuilder pb = new PacketBuilder(msg, cType, bUserId);
        return sendMessage(pb);
    }

    private String sendMessage(PacketBuilder pb) throws InterruptedException {
        byte[] toSend = pb.getPacketBytes();

        while (true) {
            try {
                out.write(toSend);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                baos.write(buffer, 0 , in.read(buffer));
                byte result[] = baos.toByteArray();
                PacketReceiver packet = new PacketReceiver(result);

                return packet.getMessageStr();
            } catch (IOException e) {
                try {
                    Thread.sleep(2000);
                    startConnection(6666);
                    return sendMessage(pb);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopConnection() throws IOException {
        in.close();
        out.close();
        clientSocket.close();
    }

    private static class ReceiveMessageThread extends Thread {
        public String getReceived() {
            return received;
        }

        private String received = null;
        private BufferedReader in;

        public ReceiveMessageThread(BufferedReader in) {
            this.in = in;
        }

        @Override
        public void run() {
            try {
                received = in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
