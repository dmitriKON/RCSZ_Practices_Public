package homework2;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {
    public static long COMMAND_COUNTER = 0;
    private static int CAPACITY = 100000;
    private static int NUMBER_OF_WORKERS = 100;
    private static int NUMBER_OF_MESSAGES = 99999;
    private static byte APP_ID = 1;
    private static String CIPHER_ALGO = "AES";
    private static final byte[] KEY_VALUE = new byte[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16};
    private static ArrayBlockingQueue<Message> MESSAGES = new ArrayBlockingQueue<Message>(CAPACITY);
    private static ArrayBlockingQueue<byte[]> ENCRYPTED_SENT_MESSAGES = new ArrayBlockingQueue<byte[]>(CAPACITY);
    private static ArrayBlockingQueue<Message> DECRYPTED_SENT_MESSAGES = new ArrayBlockingQueue<Message>(CAPACITY);
    private static ArrayBlockingQueue<Message> PROCESSED_SENT_MESSAGES = new ArrayBlockingQueue<Message>(CAPACITY);
    private static ArrayBlockingQueue<byte[]> ENCRYPTED_PROCESSED_MESSAGE = new ArrayBlockingQueue<byte[]>(CAPACITY);

    public static void SendMessages(ArrayBlockingQueue<Message> messages, int number_of_workers) throws InterruptedException {
        ArrayBlockingQueue<byte[]> encrypted_sent_messages = new ArrayBlockingQueue<byte[]>(messages.size());
        ArrayBlockingQueue<Message> decrypted_sent_messages = new ArrayBlockingQueue<Message>(messages.size());
        ArrayBlockingQueue<Message> processed_sent_messages = new ArrayBlockingQueue<Message>(messages.size());
        ArrayBlockingQueue<byte[]> encrypted_processed_messages = new ArrayBlockingQueue<byte[]>(messages.size());
        List<Encryptor> MESSAGE_ENCRYPTORS = new ArrayList<Encryptor>();
        for (int i = 0; i < number_of_workers; i++) {
            MESSAGE_ENCRYPTORS.add(
                    new Encryptor(
                        messages, encrypted_sent_messages, APP_ID, CIPHER_ALGO, KEY_VALUE
                    )
            );
        }
        List<Decryptor> SENT_MESSAGE_DECRYPTORS = new ArrayList<Decryptor>();
        for (int i = 0; i < number_of_workers; i++) {
            SENT_MESSAGE_DECRYPTORS.add(
                    new Decryptor(
                            encrypted_sent_messages, decrypted_sent_messages, CIPHER_ALGO, KEY_VALUE
                    )
            );
        }
        List<Processor> DECRYPTED_SENT_MESSAGE_PROCESSORS = new ArrayList<Processor>();
        for (int i = 0; i < number_of_workers; i++) {
            DECRYPTED_SENT_MESSAGE_PROCESSORS.add(
                    new Processor(
                            decrypted_sent_messages, processed_sent_messages
                    )
            );
        }
        List<Encryptor> PROCESSED_MESSAGE_ENCRYPTORS = new ArrayList<Encryptor>();
        for (int i = 0; i < number_of_workers; i++) {
            PROCESSED_MESSAGE_ENCRYPTORS.add(
                    new Encryptor(
                            processed_sent_messages, encrypted_processed_messages, APP_ID, CIPHER_ALGO, KEY_VALUE
                    )
            );
        }
        FakeReceiver.receiveMessages(encrypted_processed_messages);
        for (int i = 0; i < number_of_workers; i++) {
            PROCESSED_MESSAGE_ENCRYPTORS.get(i).join();
        }
    }
}
