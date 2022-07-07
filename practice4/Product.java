package practice4;

public class Product {
    private Integer id;
    private String prodName;
    private double price;
    private String factoryName;

    public Product(Integer id, String prodName, double price, String factoryName) {
        this.id = id;
        this.prodName = prodName;
        this.price = price;
        this.factoryName = factoryName;
    }

    public Product(String prodName, double price, String factoryName) {
        this.prodName = prodName;
        this.price = price;
        this.factoryName = factoryName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
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
                ", prodName='" + prodName + '\'' +
                ", price=" + price +
                ", factoryName='" + factoryName + '\'' +
                '}';
    }
}
