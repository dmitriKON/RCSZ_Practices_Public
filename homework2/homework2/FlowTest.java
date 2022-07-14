package homework2;

import org.junit.jupiter.api.Test;
import java.util.concurrent.ArrayBlockingQueue;

public class FlowTest {
    @Test
    void TestWithASmallAmountOfPackages() throws InterruptedException {
        final int capacity = 100;
        final int n_of_workers = 10;
        final int n_of_messages = 10;
        MessageType message_type = MessageType.CP;
        int user_id = 1;
        ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(capacity);
        for (int i = 0; i < n_of_messages; i++) {
            Message message = RandomMessageGenerator.generate_message(message_type);
            messages.put(message);
        }
        Main.SendMessages(
                messages,
                n_of_workers
        );
    }

    @Test
    void TestWithAGreatAmountOfPackages() throws InterruptedException {
        final int capacity = 100000;
        final int n_of_workers = 1000;
        final int n_of_messages = 100000;
        MessageType message_type = MessageType.CP;
        int user_id = 1;
        ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(capacity);
        for (int i = 0; i < n_of_messages; i++) {
            Message message = RandomMessageGenerator.generate_message(message_type);
            messages.put(message);
        }
        Main.SendMessages(
                messages,
                n_of_workers
        );
    }

    @Test
    void TestWithAmountOfWorkersGreaterThanPackages() throws InterruptedException {
        final int capacity = 1;
        final int n_of_workers = 1000;
        final int n_of_messages = 1;
        MessageType message_type = MessageType.CP;
        int user_id = 1;
        ArrayBlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(capacity);
        for (int i = 0; i < n_of_messages; i++) {
            Message message = RandomMessageGenerator.generate_message(message_type);
            messages.put(message);
        }
        Main.SendMessages(
                messages,
                n_of_workers
        );
    }

}
