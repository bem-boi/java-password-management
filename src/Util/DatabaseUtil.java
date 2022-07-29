package Util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseUtil {
    
    public static boolean firstTime(Connection con){ // check if there's any user in the database
        String sql = "SELECT * FROM users LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            return !s.next(); //check if you can get a new row 
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    public static void registration(){
        System.out.println("hi");
    }
    
    public static void main(String[] args) {
        String url = "jdbc:mysql://127.0.0.1/key_vault";
        try (Connection con = DriverManager.getConnection(url,"Beam","12345")){

            System.out.println("Got it!");

            //check if its the first time, so check if theres any username in the table
            if (firstTime(con)){
                registration();
            }
            String sql = "SELECT id, username FROM users WHERE username = ?"; //"?" is placeholder, WHERE is under what condition
            PreparedStatement ps = con.prepareStatement(sql); //convert sql to be something read to execute
            ps.setString(1, "lol");

        } catch (SQLException e) {
            throw new Error("Problem", e);
        } 
    }
}
