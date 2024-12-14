import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.*;

public class konektor {
    public static void main(String[] args) {
        MysqlDataSource dataSc = new MysqlDataSource();

        String url = "jdbc:mysql://localhost:3306/tokomadura";
        String user = "tugaspbw";
        String pass = "";

        dataSc.setURL(url);
        dataSc.setUser(user);
        dataSc.setPassword(pass);

        try {
            Connection con = dataSc.getConnection();
            System.out.println("Koneksi Berhasil");
        } catch (SQLException e) {
            System.out.println("Koneksi Gagal " + e.getMessage());
        }
    }
}
