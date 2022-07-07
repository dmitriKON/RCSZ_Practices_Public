package practice4;

import java.sql.*;

public class Database {

    private final Connection connection;

    public Database(String dbName) {
        try{
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite: " + dbName);

            Statement statement = connection.createStatement();
            String createTable = "CREATE TABLE IF NOT EXISTS PRODUCTS(" +
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    "PRODUCT_NAME TEXT NOT NULL," +
                    "PRICE REAL NOT NULL," +
                    "FACTORY_NAME TEXT NOT NULL)";
            statement.execute(createTable);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createProduct(Product product){
        String sql = "INSERT INTO PRODUCTS (PRODUCT_NAME, PRICE, FACTORY_NAME) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)){
            preparedStatement.setString(1, product.getProdName());
            preparedStatement.setDouble(2, product.getPrice());
            preparedStatement.setString(3, product.getProdName());
            preparedStatement.execute();

            //product.setId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Database db = new Database("products.db");
        db.createProduct(new Product("name1", 1, "factory1"));
    }

}
