package homework2;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import java.security.Key;

public class PacketUtils {

    public static byte[] generate_message(Message message, Cipher cipher, Key SECRET_KEY) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] packet = objectMapper.writeValueAsBytes(message.getPacket());
            byte[] encoded_packet = cipher.doFinal(packet);
            byte[] enc_packet = ByteBuffer.allocate(4 + 4 + encoded_packet.length)
                    .put(objectMapper.writeValueAsBytes(message.getMessageType()))
                    .putInt(message.getUserId())
                    .put(encoded_packet)
                    .array();
            return enc_packet;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] generate_header(byte client_app_id, long packetId, int messageLength) {
        byte[] header = ByteBuffer.allocate(1 + 1 + 8 + 4)
                .put((byte) 0x13)
                .put(client_app_id)
                .putLong(packetId)
                .putInt(messageLength)
                .array();
        return header;
    }
}