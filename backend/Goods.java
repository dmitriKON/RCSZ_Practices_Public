package backend;

import java.util.Objects;

public class Goods {
    private int id;
    private String name;
    private int amount;

    private String groupName;
    private double price;

    private String about;

    private String producer;

    public Goods(int id, String name, int amount, String groupName, double price, String about, String producer) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.groupName = groupName;
        this.price = price;
        this.about = about;
        this.producer = producer;
    }

    public Goods(String name, int amount, String groupName, double price, String about, String producer) {
        this.name = name;
        this.amount = amount;
        this.groupName = groupName;
        this.price = price;
        this.about = about;
        this.producer = producer;
    }

    public Goods(int id) {
        this.id = id;
    }

    public Goods(){}

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public String getProducer() {
        return producer;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", groupName=" + groupName +
                ", price=" + price +
                ", about='" + about + '\'' +
                ", producer='" + producer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods goods = (Goods) o;
        return amount == goods.amount && groupName == goods.groupName && Double.compare(goods.price, price) == 0 && name.equals(goods.name) && Objects.equals(about, goods.about) && Objects.equals(producer, goods.producer);
    }
}
