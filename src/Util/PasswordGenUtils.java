package Util;

import java.security.SecureRandom;
import java.util.regex.*;

// https://www.delftstack.com/howto/java/java-utility-class.java/
// https://www.baeldung.com/java-generate-secure-password
// https://kodejava.org/how-do-i-generate-a-random-alpha-numeric-string/

// this checks for the password's conditions
// https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-expressions-in-java/ 

public final class PasswordGenUtils {

    private static SecureRandom rand = new SecureRandom();
    
    private PasswordGenUtils(){
        throw new AssertionError("Instantiating utility class.");    
    }

    // generates the password
    public static String generatePassword(int n){
        int num = rand.nextInt(126-33) + 33; // convert in to ascii
        if(n == 1){
            return (char)num + "";
        }else{
            return generatePassword(n-1) + (char)num;
        }
    }

    // checks for the password's condition
    public static boolean isValidPassword(String password){
        if (password == null){
            return false;
        }

        String conditions = "^(?=.*[0-9])"
                        + "(?=.*[a-z])(?=.*[A-Z])"
                        + "(?=.*[@#$%^&+-=(),/:;_~<>{}|])" // special characters password need to have
                        + "(?=\\S+$).{12,25}$";

        Pattern patt = Pattern.compile(conditions);
        Matcher match = patt.matcher(password);
        return match.matches();
    }

}
