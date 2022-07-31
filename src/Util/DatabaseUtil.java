package Util;

import java.sql.*;
import java.util.LinkedList;
import java.util.Arrays;

public final class DatabaseUtil {
    
    public static Connection connectDB(String url) throws SQLException{
        Connection con = DriverManager.getConnection(url,"Beam","12345");
        return con;
    }

    public static boolean firstTime(Connection con){ // check if there's any user in the database (IF TRUE THEN REDIRECT TO REGISTER PAGE, IF FALSE THEN REDIRECT TO LOGIN PAGE)
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

    // https://www.tutorialsfield.com/registration-form-in-java-with-database-connectivity/     
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

    // returns all of the webname in array in alphabetical order (used with the drop down menu)
    public static String[] getWebName(Connection con){
        LinkedList<String> webList = new LinkedList<String>();
        String sql = "SELECT webname FROM password";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String web = rs.getString("webname");
                webList.add(web);
            }
            String[] webArray = webList.toArray(new String[0]);
            Arrays.sort(webArray);
            return webArray;
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // returns array of values that will be used to give to the user
    public static String[] queryButton(Connection con, String webname){
        String sql = "SELECT * FROM password WHERE webname='"+webname+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            s.next();
            String username = s.getString("username");
            String email = s.getString("email");
            String cipherPW = s.getString("cipherPW");
            String IV = s.getString("IV");
            String[] UserEmailPwIV = {username, email, cipherPW, IV};
            return UserEmailPwIV;
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // first checks if the password inputed is the same as the hashPW, if it's the same then it executes the delete SQL statement
    public static void deleteButton(Connection con, String webname, String hashPW){
        String sql = "SELECT * FROM password WHERE webname='"+webname+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (hashPW.equals(rs.getString(hashPW))){
                String sqlDelete = "DELETE FROM password WHERE webname='"+webname+"'";
                PreparedStatement psDelete = con.prepareStatement(sqlDelete);
                psDelete.executeUpdate();
            }else{
                System.out.println("Incorrect password");
            }
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // update passwords in database if inputed password is the same as hashPW
    public static void changeButton(Connection con, String webname, String hashPW, String newCipherPW, String newIV){
        String sql = "SELECT * FROM password WHERE webname='"+webname+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (hashPW.equals(rs.getString(hashPW))){
                String sqlUpdate = "UPDATE password SET cipherPW='"+newCipherPW+"' and IV = '"+newIV+"' WHERE webname='"+webname+"'";
                PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
                psUpdate.executeUpdate();
            }else{
                System.out.println("Incorrect password");
            }
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
