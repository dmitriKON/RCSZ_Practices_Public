package homework2;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class FakeReceiver implements Receiver{
    @Override
    public void receiveMessage() {

    }

    public static void receiveMessages(
            ArrayBlockingQueue<byte[]> queue_to_fetch
    ) {
        try{
            while (true) {
                byte[] item = queue_to_fetch.poll(10, TimeUnit.SECONDS);
                if (item == null) {
                    System.out.println("Found null");
                    return;
                }
                System.out.println("Received message: " + item);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

    }
}
