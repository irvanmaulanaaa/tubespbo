package Login;

import java.sql.*;

public abstract class DataUser {

    String url = "jdbc:mysql://localhost:3306/tokomadura";
    String user = "tugaspbw";
    String pass = "";
    
    public Connection getConnection() {
        Connection con = null;
        try {
            con = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Koneksi Gagal " + e.getMessage());
        }
        return con;
    }

    protected String username;
    protected String password;

    public DataUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public abstract boolean login();
    public abstract boolean register();
}
