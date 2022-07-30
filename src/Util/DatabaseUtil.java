package Util;

import java.sql.*;

public final class DatabaseUtil {
    
    public static Connection connectDB(String url) throws SQLException{
        Connection con = DriverManager.getConnection(url,"Beam","12345");
        return con;
    }

    public static boolean firstTime(Connection con){ // check if there's any user in the database
        String sql = "SELECT * FROM users LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            return !s.next(); //check if you can get a new row 
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // if true means there's user in database, false if there is no user in database   https://stackoverflow.com/questions/17506762/how-to-handle-if-a-sql-query-finds-nothing-using-resultset-in-java
    public static boolean checkUsernamePasswordLogin(Connection con, String username, String hashPW){
        String sql = "SELECT * FROM users WHERE username='"+username+"' AND hash_pw='"+hashPW+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            return s.next();
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // https://www.tutorialsfield.com/registration-form-in-java-with-database-connectivity/   what to do with the id?  
    public static void insertUsernamePasswordRegister(Connection con, String username, String hashPW, String cipherKey){ 
        String sql = "INSERT INTO users (username, hash_pw, encryption_key) VALUES('"+username+"' , '"+hashPW+"' , '"+cipherKey+"')";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // check later
    public static void insertPasswordGen(Connection con, String webname, String email, String cipherPW, String IV, String hashPW){
        String sql = "INSERT INTO password (username, webname, email, cipherPW, IV, hashPW) VALUES('"+webname+"' , '"+email+"' , '"+cipherPW+"', '"+IV+"', '"+hashPW+"')";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    public static void main(String[] args) throws SQLException{
        String url = "jdbc:mysql://127.0.0.1/key_vault";
        try (Connection con = connectDB(url)){

            System.out.println("Got it!");

            //check if its the first time, so check if theres any username in the table
            if (firstTime(con)){
                System.out.println("REGISTER"); // send to register page
            }
            String sql = "SELECT id, username FROM users WHERE username = ?"; //"?" is placeholder, WHERE is under what condition
            PreparedStatement ps = con.prepareStatement(sql); //convert sql to be something read to execute
            ps.setString(1, "lol");

        } catch (SQLException e) {
            throw new Error("Problem", e);
        } 
    }
}
