package homework2;

import java.util.Random;

public class RandomMessageGenerator {

    private static Random random_generator = new Random();

    public static Message generate_message(MessageType messageType) {
        int amount = random_generator.nextInt(100);
        int type = random_generator.nextInt(10);
        int price = random_generator.nextInt(2000);
        int user_id = random_generator.nextInt(10);
        byte[] message_body = new byte[random_generator.nextInt(50)];
        random_generator.nextBytes(message_body);
        String message = new String(message_body);
        Goods goods = new Goods(amount, type, price);
        Packet packet = new Packet(goods, message);
        return new Message(messageType, user_id, packet);
    }
}
