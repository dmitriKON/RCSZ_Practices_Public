package homework2;


public class Goods {

    private int amount;
    private int type;
    private int price;

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public Goods(int amount, int type, int price) {
        this.amount = amount;
        this.type = type;
        this.price = price;
    }

    public Goods(){}

    public int getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }

    private void setAmount(int amount) {
        this.amount = amount;
    }

    public void addGoods(int am){
        setAmount(this.amount + am);
    }

    public void deleteGoods(int am){
        setAmount(this.amount - am);
    }

    @Override
    public String toString() {
        return "Goods{ " +
                "amount = " + amount +
                ", type = " + type +
                ", price = " + price + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Goods))
            return false;
        Goods goods = (Goods) o;
        return (this.amount == goods.getAmount()) && (this.type == goods.getType()) && (this.price == goods.getPrice());
    }

}
