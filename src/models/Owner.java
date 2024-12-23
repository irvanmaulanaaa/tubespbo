package models;

import connect.koneksi;
import interfaces.CRUD;

import java.sql.*;
import java.util.ArrayList;

public class Owner extends User implements CRUD {
    private  ArrayList<Product> productList;

    public Owner(String username, String password) {
        super(username, password);
        this.productList = new ArrayList<>();
    }

    @Override
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM Owners WHERE username = ? AND password = ?";

        try (Connection conn = koneksi.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Menambahkan produk ke database dan ke dalam list produk
    @Override
    public  void insertProduct(Product product) {
        String sql = "INSERT INTO Products (product_code, product_name, price, product_stock, description) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = koneksi.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, product.getProductCode());
            stmt.setString(2, product.getProductName());
            stmt.setDouble(3, product.getProductPrice());
            stmt.setInt(4, product.getProductStock());
            stmt.setString(5, product.getProductDescription() != null ? product.getProductDescription() : "");
            stmt.executeUpdate();
            productList.add(product);
            System.out.println("\nProduk berhasil ditambahkan!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("\nTerjadi kesalahan saat menambahkan produk.");
        }
    }

    // Menampilkan semua produk
    @Override
    public  void viewProduct() {
        // Periksa apakah products null terlebih dahulu
        String sql = "SELECT * FROM products";
        try (Connection conn = koneksi.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
                if (!rs.isBeforeFirst()) { 
                    System.out.println("\nBelum ada produk di toko.");
                } else {
                    System.out.println("\n=== Daftar Produk ===");
                    int i = 1;
                    while (rs.next()) {
                        System.out.println(i + ". " + "Kode Produk: " + rs.getString("product_code") + ", Nama Produk: " + rs.getString("product_name") + ", Harga: Rp." + rs.getDouble("price") + ", Stok: " + rs.getInt("product_stock") + ", Deskripsi: " + rs.getString("description"));
                        i++;
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Mengupdate produk yang sudah ada
    @Override
    public void updateProduct(Product p) {

        String productCode = p.getProductCode();
        String newProductName = p.getProductName();
        double newPrice = p.getProductPrice();
        int newStock = p.getProductStock();
        String newDescription = p.getProductDescription();
        
        String sql = "UPDATE Products SET product_name = ?, price = ?, product_stock = ?, description = ? WHERE product_code = ?";

        try (Connection conn = koneksi.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newProductName);
            stmt.setDouble(2, newPrice);
            stmt.setInt(3, newStock);
            stmt.setString(4, newDescription);
            stmt.setString(5, productCode);
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("\nProduk berhasil diperbarui!");
            } else {
                System.out.println("\nProduk dengan kode " + productCode + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Terjadi kesalahan saat memperbarui produk.");
        }
    }

    // Menghapus produk berdasarkan kode produk
    @Override
    public void removeProduct(Product p) {
        String productCode = p.getProductCode();

        String sql = "DELETE FROM Products WHERE product_code = ?";

        try (Connection conn = koneksi.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, productCode);
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("\nProduk dengan kode " + productCode + " telah dihapus.");
            } else {
                System.out.println("\nProduk dengan kode " + productCode + " tidak ditemukan.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Terjadi kesalahan saat menghapus produk.");
        }
    }
}
