package practice3;

import practice3.packet.PacketBuilder;
import practice3.packet.PacketReceiver;
import practice3.packet.Processor;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class UDPServer extends Thread {

    private DatagramSocket socket;
    private boolean running;
    private byte[] buf = new byte[1024];

    public UDPServer() throws SocketException {
        socket = new DatagramSocket(4445);
    }

    public void run() {
        running = true;
        try {
            while (running) {
                buf = new byte[256];
                DatagramPacket packet
                        = new DatagramPacket(buf, buf.length);
                socket.receive(packet);

                InetAddress address = packet.getAddress();
                int port = packet.getPort();

                PacketReceiver packetReceiver = new PacketReceiver(packet.getData());

                String answer = Processor.process(packetReceiver.getMessage());

                PacketBuilder pb = new PacketBuilder(answer, 0, 6666);
                packet = new DatagramPacket(pb.getPacketBytes(), pb.getPacketBytes().length, address, port);
                socket.send(packet);
            }
            socket.close();

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
