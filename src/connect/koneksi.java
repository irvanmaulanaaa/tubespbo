package connect;
import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class koneksi {
    public static Connection getConnection() {
        MysqlDataSource dataSource = new MysqlDataSource();
        String DB_URL = "jdbc:mysql://localhost:3306/testes"; 
        String DB_USERNAME = "tugaspbw"; 
        String DB_PASSWORD = ""; 

        dataSource.setUrl(DB_URL);
        dataSource.setUser(DB_USERNAME);
        dataSource.setPassword(DB_PASSWORD);

        try {
            return dataSource.getConnection(); 
        } catch (SQLException ex) {
            System.out.println("Eksepsi akses data: " + ex.getMessage());
            return null; 
        }
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null) {
            System.out.println("Koneksi berhasil");
        } else {
            System.out.println("Koneksi gagal");
        }
    }
}
