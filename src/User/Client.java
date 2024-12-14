package User ;

import Login.DataUser ;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Client extends DataUser{

    public Client(String username, String password) {
        super(username, password);
    }

    public boolean register(){
        String Query = "INSERT INTO user (username, password) VALUES (?, ?)" ;

        try (Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(Query)){
            
            statement.setString(1, username);
            statement.setString(2, password);

            int rowCount = statement.executeUpdate();

            return rowCount > 0;
        } catch (SQLException e){
            System.out.println("Pesan Eksepsi : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean login(){

        String Query = "SELECT * FROM user WHERE username = ? AND password = ?" ;

        try (Connection conn = getConnection();
        PreparedStatement statement = conn.prepareStatement(Query)){

            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();
            return result.next();

        } catch (SQLException e){

            System.out.println("Pesan Eksepsi : " + e.getMessage());            
            return false;

        }
    }
}
