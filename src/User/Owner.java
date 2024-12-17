package User;

import Product.Product;
import Strore.StroreInterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Owner extends Product implements StroreInterface {

    private static final String url  = "jdbc:mysql://localhost:3306/tokomadura";
    private static final String user = "tugaspbw";
    private static final String pass = "";

    public Owner(String productCode, String productName, double productPrice, int productStock, String productDescription) {
        super(productCode, productName, productPrice, productStock, productDescription);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    @Override
    public boolean login(String username, String password) {
        String query = "SELECT * FROM owner WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();
            return result.next();

        } catch (SQLException e) {
            System.out.println("Pesan Eksepsi : " + e.getMessage());
            return false;
        }
    }

    @Override
    public void insertProduct(Product product) throws SQLException {
        String queryInsert = "INSERT INTO product (productCode, productName, productPrice, productStock, productDescription) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(queryInsert)) {

            statement.setString(1, product.getProductCode());
            statement.setString(2, product.getProductName());
            statement.setDouble(3, product.getProductPrice());  // Menggunakan setDouble untuk harga
            statement.setInt(4, product.getProductStock());    // Menggunakan setInt untuk stok
            statement.setString(5, product.getProductDescription());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Pesan Eksepsi : " + e.getMessage());
        }
    }

    @Override
    public void removeProduct(Product product) throws SQLException {
        String queryDelete = "DELETE FROM product WHERE productCode = ?";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(queryDelete)) {

            statement.setString(1, product.getProductCode());
            int rowCount = statement.executeUpdate();

            if (rowCount > 0) {
                System.out.println("Produk berhasil dihapus: " + product.getProductCode());
            } else {
                System.out.println("Produk tidak ditemukan");
            }

        } catch (SQLException e) {
            System.out.println("Pesan Eksepsi : " + e.getMessage());
        }
    }

    @Override
    public void viewProduct(Product product) throws SQLException {
        String querySelect = "SELECT * FROM product";

        try (Connection conn = getConnection();
             PreparedStatement statement = conn.prepareStatement(querySelect);
             ResultSet result = statement.executeQuery()) {

            ArrayList<Product> products = new ArrayList<>();
            while (result.next()) {
                String productCode = result.getString("productCode");
                String productName = result.getString("productName");
                double productPrice = result.getDouble("productPrice");  // Menggunakan getDouble
                int productStock = result.getInt("productStock");       // Menggunakan getInt
                String productDescription = result.getString("productDescription");

                Product productObj = new Product(productCode, productName, productPrice, productStock, productDescription);
                products.add(productObj);
            }

            for (Product p : products) {
                System.out.println(p.getProductCode() + " | " + p.getProductName() + " | " + p.getProductPrice() + " | "
                        + p.getProductStock() + " | " + p.getProductDescription());
            }

        } catch (SQLException e) {
            System.out.println("Pesan Eksepsi : " + e.getMessage());
        }
    }

    @Override
    public void updateProduct(Product product) throws SQLException {
        String queryUpdate = "UPDATE product SET productStock = ? WHERE productCode = ?";
        String queryCheck = "SELECT * FROM product WHERE productCode = ?";

        try (Connection conn = getConnection()) {
            // Check if the product exists
            PreparedStatement checkStatement = conn.prepareStatement(queryCheck);
            checkStatement.setString(1, product.getProductCode());
            ResultSet result = checkStatement.executeQuery();

            if (result.next()) {
                // Update the product stock if it exists
                PreparedStatement updateStatement = conn.prepareStatement(queryUpdate);
                updateStatement.setInt(1, product.getProductStock()); // Menggunakan setInt
                updateStatement.setString(2, product.getProductCode());

                int count = updateStatement.executeUpdate();

                if (count > 0) {
                    System.out.println("Produk berhasil diupdate: " + product.getProductCode() + " | " + product.getProductStock());
                } else {
                    System.out.println("Produk tidak ditemukan");
                }
            } else {
                System.out.println("Produk tidak ditemukan");
            }

        } catch (SQLException e) {
            System.out.println("Pesan Eksepsi : " + e.getMessage());
        }
    }
}
