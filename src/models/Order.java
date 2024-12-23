package models;

import connect.koneksi;
import java.sql.*;

public class Order {
    private Product product;
    private int quantity;

    public Order(Product product, int quantity) {
        if (product.getProductStock() >= quantity) {
            this.product = product;
            this.quantity = quantity;
            product.reduceStock(quantity);
            updateProductStockInDatabase(product);
        } else {
            throw new IllegalArgumentException("Stok produk tidak mencukupi untuk jumlah yang dipesan.");
        }
    }

    private void updateProductStockInDatabase(Product product) {
        try (Connection conn = koneksi.getConnection()) {
            String sql = "UPDATE products SET product_stock = ? WHERE product_code = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, product.getProductStock());
            stmt.setString(2, product.getProductCode());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            System.out.println("Error saat memperbarui stok produk di database: " + ex.getMessage());
        }
    }

    public double getTotalAmount() {
        return product.getProductPrice() * quantity;
    }

    public Product getProduct() {
        return product;
    }
    
    @Override
    public String toString() {
        return "Produk: " + product.getProductName() + ", Jumlah: " + quantity + ", Total: " + getTotalAmount();
    }
}
