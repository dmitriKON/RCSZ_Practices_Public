package homework2;


public class Packet {
    public Goods goods = null;
    public String message;

    public Packet(String message) {
        this.message = message;
    }

    public Packet(Goods goods, String message) {
        this.goods = goods;
        this.message = message;
    }

    public Packet() {}

    @Override
    public String toString() {
        return "Packet{ " + "goods = " + goods + ", message = " + message + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Packet))
            return false;
        Packet packet = (Packet) o;
        boolean goods_equal = (this.goods == null) ? (packet.goods == null) : (this.goods.equals(packet.goods));
        return (goods_equal) && (this.message.equals(packet.message));
    }

}