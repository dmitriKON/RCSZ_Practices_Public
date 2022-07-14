package homework2;

import java.security.Key;
import javax.crypto.Cipher;
import java.nio.ByteBuffer;
import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Encryptor extends Thread{
    private byte app_id;
    private String CIPHER_ALGO;
    private Key SECRET_KEY;

    private ArrayBlockingQueue<Message> queue_to_fetch;
    private ArrayBlockingQueue<byte[]> queue_to_push;

    public byte[] encode(Message message) {
        try {
            Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
            byte[] gen_message = PacketUtils.generate_message(message, cipher, SECRET_KEY);
            byte[] header = PacketUtils.generate_header(app_id, Main.COMMAND_COUNTER, gen_message.length - 8);
            Main.COMMAND_COUNTER++;
            return ByteBuffer.allocate(header.length + 2 + gen_message.length + 2)
                    .put(header)
                    .putShort(CRC16.generate_crc16(header))
                    .put(gen_message)
                    .putShort(CRC16.generate_crc16(gen_message))
                    .array();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Encryptor(
            ArrayBlockingQueue<Message> queue_to_fetch,
            ArrayBlockingQueue<byte[]> queue_to_push,
            byte app_id,
            String cipher_algo,
            byte[] key_value
    ) {
        this.queue_to_fetch = queue_to_fetch;
        this.queue_to_push = queue_to_push;
        this.app_id = app_id;
        this.CIPHER_ALGO = cipher_algo;
        this.SECRET_KEY = new SecretKeySpec(key_value, CIPHER_ALGO);
        this.start();
    }

    public void run() {
        try {
            while (true) {
                Message item = queue_to_fetch.poll(10, TimeUnit.SECONDS);
                if (item == null) {
                    System.out.println("Found null");
                    return;
                }
                byte[] encoded_message = encode(item);
                System.out.println("Encoded item : " + item);
                queue_to_push.put(encoded_message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
