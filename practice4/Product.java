package practice4;

public class Product {
    private Integer id;
    private String name;
    private double price;
    private String factoryName;

    public Product(Integer id, String prodName, double price, String factoryName) {
        this.id = id;
        this.name = prodName;
        this.price = price;
        this.factoryName = factoryName;
    }

    public Product(String name, double price, String factoryName) {
        this.name = name;
        this.price = price;
        this.factoryName = factoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getFactoryName() {
        return factoryName;
    }

    public void setFactoryName(String factoryName) {
        this.factoryName = factoryName;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", factoryName='" + factoryName + '\'' +
                '}';
    }
}
