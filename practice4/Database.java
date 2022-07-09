package practice4;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Database {

    private final Connection connection;

    public Database(String dbName) {
        try{
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite: " + dbName);

            Statement statement = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS PRODUCTS(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "NAME TEXT NOT NULL," +
                    "PRICE REAL NOT NULL," +
                    "FACTORY_NAME TEXT NOT NULL)";
            statement.execute(createTable);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createProduct(Product product) {
        String sql = "INSERT INTO PRODUCTS (NAME, PRICE, FACTORY_NAME) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, product.getName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getFactoryName());
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteProduct(Integer id) {
        String sql = "DELETE FROM PRODUCTS WHERE ID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateProduct(Integer id, String name, Double price, String factoryName) {
        String sql = "UPDATE PRODUCTS SET NAME = ? , "
                    + "PRICE = ? , "
                    + "FACTORY_NAME = ? "
                    + " WHERE ID = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);
            preparedStatement.setDouble(2, price);
            preparedStatement.setString(3, factoryName);
            preparedStatement.setInt(4, id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getProducts(FilterProduct filterProduct) {
        List<Product> resultList = new ArrayList<>();

        List<String> whereFilters = Stream.of(
                SqlBuilder.gte(filterProduct.getPriceFrom(), "PRICE"),
                SqlBuilder.lte(filterProduct.getPriceTo(), "PRICE"),
                SqlBuilder.startWith(filterProduct.getNameStart(), "NAME"),
                SqlBuilder.endWith(filterProduct.getNameEnd(), "NAME")
        )
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        String sql = "SELECT * FROM PRODUCTS";
        if(!whereFilters.isEmpty()) {
            sql += " WHERE " + String.join(" AND ", whereFilters);
        }
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            Product nextProduct;
            while(resultSet.next()) {
                nextProduct = new Product(resultSet.getInt("ID"), resultSet.getString("NAME"), resultSet.getDouble("PRICE"), resultSet.getString("FACTORY_NAME"));
                resultList.add(nextProduct);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }

    public static void main(String[] args) {
        Database db = new Database("products.db");
        db.createProduct(new Product("name1", 10, "factory1"));
        db.createProduct(new Product("name2", 2, "factory1"));
        db.createProduct(new Product("name3", 5, "factory1"));
        db.updateProduct(1, "name1", 4.0, "factory1");
        List<Product> productList = db.getProducts(new FilterProduct());
        db.deleteProduct(2);
        FilterProduct filterProduct = new FilterProduct();
        filterProduct.setPriceFrom(2.0);
        filterProduct.setPriceTo(5.0);

        List<Product> productList2 = db.getProducts(filterProduct);
        System.out.println(productList);
        System.out.println(productList2);
    }
}
