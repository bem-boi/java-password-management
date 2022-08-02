package Util;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.LinkedList;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import java.util.Arrays;
import java.util.HashMap;

public final class DatabaseUtil {
    
    public static Connection connectDB(String url) throws SQLException{
        Connection con = DriverManager.getConnection(url,"Beam","12345");
        return con;
    }

    // check if there's any user in the database (IF TRUE THEN REDIRECT TO REGISTER PAGE, IF FALSE THEN REDIRECT TO LOGIN PAGE)
    public static boolean firstTime(Connection con){ 
        String sql = "SELECT * FROM users LIMIT 1";
        try (PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            return !s.next(); //check if you can get a new row 
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    /* -------------------------------------------LOGIN AND REGISTRATION---------------------------------------------- */

    public static boolean checkUsername(Connection con, String username){
        String sql = "SELECT * FROM users WHERE username='"+username+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            return s.next();
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // if true means there's user in database, false if there is no user in database   https://stackoverflow.com/questions/17506762/how-to-handle-if-a-sql-query-finds-nothing-using-resultset-in-java
    public static String checkUsernamePasswordLogin(Connection con, String username){
        String sql = "SELECT * FROM users WHERE username='"+username+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            s.next();
            return s.getString("hashPW");
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // https://www.tutorialsfield.com/registration-form-in-java-with-database-connectivity/     
    public static void insertUsernamePasswordRegister(Connection con, String username, String hashPW, String cipherKey){ 
        String sql = "INSERT INTO users (username, hashPW, encryption_key) VALUES('"+username+"' , '"+hashPW+"' , '"+cipherKey+"')";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // retrieves the hashPW of the user from the database
    public static String getHashPW(Connection con, String user){
        String sql = "SELECT hashPW FROM users WHERE username='"+user+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            s.next();
            return s.getString(1);
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    /* -------------------------------------------PASSWORD GENERATOR INSERT DATA---------------------------------------------- */
    
    // insert data from password generator into database
    public static void insertPasswordGen(Connection con, String user, String webname, String email, String cipherPW, String IV, String hashPW){
        String sql = "INSERT INTO password(user, webname, email, cipherPW, IV, hashPW) VALUES('"+user+"' ,'"+webname+"' , '"+email+"' , '"+cipherPW+"', '"+IV+"', '"+hashPW+"')";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ps.executeUpdate();
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    /* -------------------------------------------QUERY WEBSITES---------------------------------------------- */

    // returns all of the webname in array in alphabetical order (used with the drop down menu)      NOT CASE SENSITIVE FIND THE COLLATION AND FIND CS NOT CI     https://makandracards.com/makandra/19495-mysql-collate-searching-case-sensitive    https://serverfault.com/questions/137415/change-collation-of-a-mysql-table-to-utf8-general-cs 
    public static String[] getWebName(Connection con, String user){
        LinkedList<String> webList = new LinkedList<String>();
        String sql = "SELECT webname FROM password WHERE user='"+user+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String web = rs.getString(1);
                webList.add(web);
            }
            String[] webArray = webList.toArray(new String[0]);
            Arrays.sort(webArray);
            return webArray;
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    /* -------------------------------------------DECRYPTION PART---------------------------------------------- */

    // get AES encryption key from user
    public static String getCipherKey(Connection con, String user){
        String sql = "SELECT encryption_key FROM users WHERE username='"+user+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            s.next();
            return s.getString(1);
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // returns array of values that will be used to give to the user     
    public static String[] queryButton(Connection con, String user, String webname){
        String sql = "SELECT * FROM password WHERE webname='"+webname+"' AND user='"+user+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            s.next();
            String email = s.getString("email");
            String cipherPW = s.getString("cipherPW");
            String IV = s.getString("IV");
            String[] EmailPwIV = {email, cipherPW, IV};
            return EmailPwIV;
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    /* -------------------------------------------DELETE DATA---------------------------------------------- */

    // first checks if the password inputed is the same as the hashPW, if it's the same then it executes the delete SQL statement     
    public static void deleteButton(Connection con, String user, String webname, String hashPW){
        String sql = "SELECT * FROM password WHERE webname='"+webname+"' AND user='"+user+"'";
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (hashPW.equals(rs.getString(hashPW))){
                String sqlDelete = "DELETE FROM password WHERE webname='"+webname+"' AND user='"+user+"'";
                PreparedStatement psDelete = con.prepareStatement(sqlDelete);
                psDelete.executeUpdate();
            }else{
                System.out.println("Incorrect password");
            }
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    /* -------------------------------------------MODIFY DATA---------------------------------------------- */

    public static boolean checkPassword(Connection con, String password, String user){
        String sql = "SELECT hashPW FROM users WHERE username='"+user+"'";
        try (PreparedStatement ps = con.prepareStatement(sql)){  
            ResultSet s = ps.executeQuery(); 
            s.next();
            return HashUtil.checkHash(password, s.getString(1));
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }
    
    // update passwords in database if inputed password is the same as hashPW             
    public static void changeButton(Connection con, String user, String webname, String newCipherPW, String newIV){
        String sql = "UPDATE password SET cipherPW="+newCipherPW+" and IV="+newIV+" WHERE webname='"+webname+"' AND user='"+user+"'";
        try (PreparedStatement ps = con.prepareStatement(sql)){  
            ps.execute(); // smth wrong here i think
            System.out.println("Database updated successfully ");
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    /* -------------------------------------------CHECK PASSWORD---------------------------------------------- */

    // decrypts the password first and returns a hash table with array inside     
    public static HashMap<Integer, String[]> checkPWPMap(Connection con, String user) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException{
        HashMap<Integer, String[]> password_dict = new HashMap<Integer, String[]>();
        String key = getCipherKey(con, user);
        String sql = "SELECT * FROM password WHERE user='"+user+"'";
        int count = 1;
        try(PreparedStatement ps = con.prepareStatement(sql)){
            ResultSet s = ps.executeQuery();
            while(s.next()){
                String tempCipherPW = s.getString("cipherPW");
                String tempIV = s.getString("IV");
                String tempWebName = s.getString("webname");
                String tempPW = AesUtil.decrypt(key, tempIV, tempCipherPW);  

                String[] data = {tempWebName,tempPW};
                password_dict.put(count,data);
                count++;
            }
            return password_dict;
        }catch (SQLException e){
            throw new Error("Problem", e);
        }
    }

    // example: 
    // public static void main(String[] args) throws SQLException{
    //     String url = "jdbc:mysql://127.0.0.1/key_vault";
    //     try (Connection con = connectDB(url)){

    //         System.out.println("Got it!");

    //         //check if its the first time, so check if theres any username in the table
    //         if (firstTime(con)){
    //             System.out.println("REGISTER"); // send to register page
    //         }
    //         String sql = "SELECT id, username FROM users WHERE username = ?"; //"?" is placeholder, WHERE is under what condition
    //         PreparedStatement ps = con.prepareStatement(sql); //convert sql to be something read to execute
    //         ps.setString(1, "lol");

    //     } catch (SQLException e) {
    //         throw new Error("Problem", e);
    //     } 
    // }
}
