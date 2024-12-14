package Strore;

import Product.Product;

import java.sql.*;
import java.util.ArrayList;

public class StoreOperation extends Product implements StroreInterface {

    private static final String url  = "jdbc:mysql://localhost:3306/tokomadura";
    private static final String user = "tugaspbw";
    private static final String pass = "";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    public StoreOperation(String productCode, String productName, String productPrice, String productStock,
            String productDescription) {
        super(productCode, productName, productPrice, productStock, productDescription);
    }

    @Override
    public void insertProduct(Product product) throws SQLException {
        try (Connection conn = getConnection();) {

            String QueryInsert = "Insert into product (productCode, productName, productPrice, productStock, productDescription) values (?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(QueryInsert);

            statement.setString(1, product.getProductCode());
            statement.setString(2, product.getProductName());
            statement.setString(3, product.getProductPrice());
            statement.setString(4, product.getProductStock());
            statement.setString(5, product.getProductDescription());
            statement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Pesan Eksepsi : " + e.getMessage());
        }
    }

    @Override
    public void removeProduct(Product product) throws SQLException {
        String QueryDelete = "DELETE FROM product WHERE productCode = ?";
        try (Connection conn = getConnection();) {
            PreparedStatement statement = conn.prepareStatement(QueryDelete);
            statement.setString(1, product.getProductCode());

            int rowCount = statement.executeUpdate();

            if (rowCount > 0) {
                System.out.println("Produk berhasil dihapus" + product.getProductCode());
            } else {
                System.out.println("Produk tidak ditemukan");
            }
        } catch (SQLException e) {
            System.out.println("Pesan Eksepsi : " + e.getMessage());
        }
    }

    @Override
    public void viewProduct(Product product) throws SQLException {
        String QuerySelect = "SELECT * FROM product";
        try (Connection conn = getConnection();) {
            PreparedStatement statement = conn.prepareStatement(QuerySelect);
            ResultSet result = statement.executeQuery();

            ArrayList<Product> products = new ArrayList<>();
            while (result.next()) {

                String productCode = result.getString("productCode");
                String productName = result.getString("productName");
                String productPrice = result.getString("productPrice");
                String productStock = result.getString("productStock");
                String productDescription = result.getString("productDescription");

                Product objekProduct = new Product(productCode, productName, productPrice, productStock, productDescription);
                products.add(objekProduct);
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

        String QueryUpdate = "UPDATE product SET productStock = ? WHERE productCode = ?";
        String QueryCheck = "SELECT * FROM Product WHERE productCode = ?";

        try (Connection conn = getConnection();) {
            PreparedStatement checkStatement = conn.prepareStatement(QueryCheck);

            checkStatement.setString(1, getProductCode());

            ResultSet result = checkStatement.executeQuery();
            if (result.next()) {
                PreparedStatement updateStatement = conn.prepareStatement(QueryUpdate);

                updateStatement.setString(1, getProductCode());
                updateStatement.setString(2, getProductStock());

                int count = updateStatement.executeUpdate();

                if (count != 0 && count > 0) {

                    System.out.println(" Produk berhasil diupdate " + product.getProductCode() + " | "
                            + product.getProductStock());

                } else {

                    System.out.println(" Produk tidak ditemukan ");

                }
            } else {

                System.out.println("Produk tidak ditemukan");

            }
        }
    }
}