package User;

import Login.DataUser;
import Pembayaran.Purchase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Customer extends DataUser implements Purchase {

    String url = "jdbc:mysql://localhost:3306/tokomadura";
    String user = "tugaspbw";
    String pass = "";

    public Customer(String username, String password) {
        super(username, password);
    }

    public boolean register() {
        String Query = "INSERT INTO user (username, password) VALUES (?, ?)";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(Query)) {

            statement.setString(1, username);
            statement.setString(2, password);

            int rowCount = statement.executeUpdate();

            return rowCount > 0;
        } catch (SQLException e) {
            System.out.println("Pesan Eksepsi : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean login() {

        String Query = "SELECT * FROM user WHERE username = ? AND password = ?";

        try (Connection conn = getConnection();
                PreparedStatement statement = conn.prepareStatement(Query)) {

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
    public void processPayment(String productCode, int quantityToBuy) {

        String stockQuery = "SELECT producStock FROM product WHERE productCode = ?";
        String updateStock = "UPDATE product SET productStock = ? WHERE productCode = ?";
        String purchase = "INSERT INTO purchase (productCode, quantityToBuy, totalAmount, username) VALUES (?, ?, ?, ?)";

        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);
            PreparedStatement stockStatement = conn.prepareStatement(stockQuery);
            ResultSet stockResult = stockStatement.executeQuery();

            if (stockResult.next()) {
                int currentStock = stockResult.getInt("productStock");
                double productPrice = stockResult.getDouble("productPrice");

                if (currentStock >= quantityToBuy) {

                    int afterBuy = currentStock - quantityToBuy;
                    PreparedStatement updateStatement = conn.prepareStatement(updateStock);

                    updateStatement.setInt(1, afterBuy);
                    updateStatement.setString(2, productCode);
                    updateStatement.executeUpdate();

                    double totalAmount = productPrice * quantityToBuy;

                    PreparedStatement purchaseStatement = conn.prepareStatement(purchase);
                    purchaseStatement.setString(1, productCode);
                    purchaseStatement.setDouble(2, totalAmount);
                    purchaseStatement.setInt(3, quantityToBuy);
                    purchaseStatement.setString(4, username);
                    purchaseStatement.executeUpdate();

                    conn.commit();
                    System.out.println("Produk berhasil dibeli");
                } else {
                    System.out.println("Stok tidak mencukupi");
                }
            } else {
                System.out.println("Produk tidak ditemukan");
            }
        } catch (SQLException e) {
            System.out.println("Tejadi Kesalahan saat memproses pembayaran :" + e.getMessage());
            try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Pesan Eksepsi : " + ex.getMessage());
            } finally {
                try (Connection conn = DriverManager.getConnection(url, user, pass)) {
                    conn.setAutoCommit(true);
                } catch (SQLException ex) {
                    System.out.println("Gagal Autocommit  : " + ex.getMessage());
                }
            }
        }
    }

    @Override
    public boolean payWithCOD(double totalAmount) {
        String query = "SELECT totalAmount, username FROM purchase WHERE totalAmount = ? AND username = ?";

        try {
            Connection conn = getConnection();
            PreparedStatement statement = conn.prepareStatement(query);

            statement.setDouble(1, totalAmount);
            statement.setString(2, username);

            ResultSet result = statement.executeQuery();
            while (result.next()) {

                double total = result.getDouble("totalAmount");
                String username = result.getString("username");

                System.out.println("Total Pembayaran : " + total);
                System.out.println("Username         : " + username);

            }
            return true;
        } catch (SQLException e) {
            System.out.println("Pesan Eksepsi : " + e.getMessage());
        }
        return false;
    }
}
