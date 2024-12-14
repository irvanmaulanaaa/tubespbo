package User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Owner extends Client{
    public Owner(String username, String password) {
        super(username, password);
    }

    @Override
    public boolean login() {
        String Query = "SELECT * FROM owner WHERE username = ? AND password = ?" ;

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
}