package models;

import connect.koneksi;
import java.sql.*;
import java.util.ArrayList;

public class Customer extends User {
    private String customerId;
    private ArrayList<Order> orderList;

    public Customer(String username, String password, String customerId) {
        super(username, password);
        this.customerId = customerId;
        this.orderList = new ArrayList<>();
    }

    public String getCustomerId() {
        return customerId;
    }

    public static void register(String username, String password, String customerId) {
        String userSql = "INSERT INTO users (username, password, status) VALUES (?, ?, ?)";
        String customerSql = "INSERT INTO customers (customer_id, username, password, user_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = koneksi.getConnection();
                PreparedStatement userStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement customerStmt = conn.prepareStatement(customerSql)) {

            userStmt.setString(1, username);
            userStmt.setString(2, password);
            userStmt.setString(3, "Customer");
            userStmt.executeUpdate();

            ResultSet rs = userStmt.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1); 

                customerStmt.setString(1, customerId);
                customerStmt.setString(2, username);
                customerStmt.setString(3, password);
                customerStmt.setInt(4, userId); 
                customerStmt.executeUpdate();

                System.out.println("Registrasi berhasil!");
            }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat registrasi: " + e.getMessage());
        }
    }

    @Override
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM Customers WHERE username = ? AND password = ?";

        try (Connection conn = koneksi.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                customerId = rs.getString("customer_id");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void addOrder(Product product, int quantity) {
        try {
            if (customerId == null || customerId.isEmpty()) {
                throw new IllegalArgumentException("ID Pelanggan tidak tersedia");
            }

            Order order = new Order(product, quantity);
            orderList.add(order);

            String sql = "INSERT INTO orders (customer_id, product_code, order_quantity, order_total) VALUES (?, ?, ?, ?)";
            try (Connection conn = koneksi.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, customerId); 
                stmt.setString(2, product.getProductCode());
                stmt.setInt(3, quantity);
                stmt.setDouble(4, order.getTotalAmount());
                stmt.executeUpdate();
            } catch (SQLException e) {
                System.out.println("Error saat menyimpan data order: " + e.getMessage());
            }

            System.out.println("Pesanan berhasil ditambahkan!");
        } catch (IllegalArgumentException e) {
            System.out.println("Gagal menambahkan pesanan: " + e.getMessage());
        }
    }

    public void viewCart() {
        System.out.println("\n=== Keranjang Belanja ===");
        if (orderList == null || orderList.isEmpty()) {
            System.out.println("Keranjang belanja kosong.");
        } else {
            for (Order order : orderList) {
                System.out.println(order);
            }
        }
    }

    public void removeOrder(Product product) {
        for (Order order : orderList) {
            if (order.getProduct().getProductCode().equals(product.getProductCode())) {
                orderList.remove(order);
                System.out.println("Produk " + product.getProductName() + " telah dihapus dari keranjang.");
                return;
            }
        }
        System.out.println("Produk tidak ditemukan di keranjang.");
    }
}