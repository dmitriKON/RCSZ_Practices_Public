package homework2;

public class Message {

    private MessageType messageType;
    private Packet packet;
    private int userId;

    public Message(MessageType messageType, int userId, Packet packet) {
        this.messageType = messageType;
        this.userId = userId;
        this.packet = packet;
    }

    public Message() {}

    public int getUserId() { return userId; }
    public MessageType getMessageType() {
        return messageType;
    }

    public Goods getGoods() { return packet.goods; }
    public String getMessage() { return packet.message; }
    public Packet getPacket() { return packet; }

    @Override
    public String toString() {
        return "Message{ messageType = " + messageType + ", userId = " + userId + ", packet = " + packet + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Message))
            return false;
        Message msg = (Message) o;
        return (this.userId == msg.getUserId()) && (this.packet.equals(msg.getPacket())) && (this.messageType == msg.getMessageType());
    }
}