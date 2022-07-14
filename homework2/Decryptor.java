package homework2;

import java.security.Key;
import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.ArrayBlockingQueue;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Decryptor extends Thread{

    private String CIPHER_ALGO;
    private Key SECRET_KEY;
    private ArrayBlockingQueue<byte[]> queue_to_fetch;
    private ArrayBlockingQueue<Message> queue_to_push;

    public Message decrypt(byte[] packet) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            ByteBuffer wrap = ByteBuffer.wrap(packet);
            // Extracting header bytes
            byte clientAppId = wrap.get(1);
            long packetId = wrap.getLong(2);
            int messageLength = wrap.getInt(10);
            short header_crc16 = wrap.getShort(14);
            // Check header CRC16 for validness
            CRC16.check_crc16_from_bytes(PacketUtils.generate_header(clientAppId, packetId, messageLength), header_crc16);
            // Copy message bytes
            byte[] messageBytes = new byte[4 + 4 + messageLength];
            System.arraycopy(packet, 16, messageBytes, 0, 4 + 4 + messageLength);
            ByteBuffer messageBuffer = ByteBuffer.wrap(messageBytes);
            // Extracting goods from message
            byte[] message_type_id = new byte[4];
            messageBuffer.get(message_type_id, 0, message_type_id.length);
            String messageType = (new String(message_type_id)).substring(1, message_type_id.length - 1);
            int userId = messageBuffer.getInt();
            byte[] encoded_packet = new byte[messageLength];
            messageBuffer.get(encoded_packet, 0, encoded_packet.length);
            Packet messpacket = objectMapper.readValue(cipher.doFinal(encoded_packet), Packet.class);
            // Generate message from data
            Message result = new Message(MessageType.valueOf(messageType), userId, messpacket);
            short message_crc16 = wrap.getShort(24 + messageLength);
            // Check message CRC16 for validness
            Cipher gen_cipher = Cipher.getInstance(CIPHER_ALGO);
            CRC16.check_crc16_from_bytes(PacketUtils.generate_message(result, gen_cipher, SECRET_KEY), message_crc16);
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Decryptor(
            ArrayBlockingQueue<byte[]> queue_to_fetch,
            ArrayBlockingQueue<Message> queue_to_push,
            String cipher_algo,
            byte[] key_value
    ) {
        this.queue_to_fetch = queue_to_fetch;
        this.queue_to_push = queue_to_push;
        this.CIPHER_ALGO = cipher_algo;
        this.SECRET_KEY = new SecretKeySpec(key_value, CIPHER_ALGO);
        this.start();
    }

    public void run() {
        try {
            while (true) {
                byte[] item = queue_to_fetch.poll(10, TimeUnit.SECONDS);
                if (item == null) {
                    System.out.println("Found null");
                    return;
                }
                Message decrypted_message = decrypt(item);
                System.out.println("Decrypted message: " + decrypted_message);
                queue_to_push.put(decrypted_message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
