package homework2;

import javax.crypto.spec.SecretKeySpec;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Processor extends Thread{

    private ArrayBlockingQueue<Message> queue_to_fetch;
    private ArrayBlockingQueue<Message> queue_to_push;

    public Message process(Message message) {
        try {
            // Some processing is going on here....
            Packet response_message = new Packet("OK");
            return new Message(message.getMessageType(), message.getUserId(), response_message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Processor(
            ArrayBlockingQueue<Message> queue_to_fetch,
            ArrayBlockingQueue<Message> queue_to_push
    ) {
        this.queue_to_fetch = queue_to_fetch;
        this.queue_to_push = queue_to_push;
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
                Message response_message = process(item);
                System.out.println("Processed item : " + item);
                queue_to_push.put(response_message);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
