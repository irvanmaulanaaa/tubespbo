package models;

import connect.koneksi;
import java.sql.*;

public class Product {
    private String productName;
    private double productPrice;
    private int productStock;
    private String productDescription;
    private String productCode;

    public Product(String productName, double productPrice, int productStock, String productDescription,
            String productCode) {
        if (productDescription == null || productDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Deskripsi produk tidak boleh kosong.");
        }
        this.productName = productName;
        this.productPrice = productPrice;
        this.productStock = productStock;
        this.productDescription = productDescription;
        this.productCode = productCode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductStock() {
        return productStock;
    }

    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductCode() {
        return productCode;
    }

    public void reduceStock(int quantity) {
        if (productStock >= quantity) {
            this.productStock -= quantity;
        } else {
            throw new IllegalArgumentException("Stok tidak cukup untuk dipesan.");
        }
    }

    public static Product getProductByCode(String productCode) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "SELECT * FROM Products WHERE product_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, productCode);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String productName = rs.getString("product_name");
                double productPrice = rs.getDouble("price");
                int productStock = rs.getInt("product_stock");
                String productDescription = rs.getString("description");

                return new Product(productName, productPrice, productStock, productDescription, productCode);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return " Kode product: " + productCode + ", Nama produk: " + productName + ", Harga: " + productPrice + ", Stok Produk: " + productStock + ", Deskripsi:" + productDescription;
    } 
}
