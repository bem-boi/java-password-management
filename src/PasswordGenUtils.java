import java.util.Random;
import java.util.regex.*;

public final class PasswordGenUtils {
    // https://www.delftstack.com/howto/java/java-utility-class.java/
    // https://www.baeldung.com/java-generate-secure-password
    // https://kodejava.org/how-do-i-generate-a-random-alpha-numeric-string/
    
    // this checks for the password's conditions
    // https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-expressions-in-java/ 

    private static Random rand = new Random();

    private static char[] chars = {'a','b','c','d','e','f','g','h','i','j','k','l','m',
    'n','o','p','q','r','s','t','u','v','w','x','y','z','0','1','2','3','4','5',
    '6','7','8','9','!','@','$','%','^','&','#','-','+','='};
    
    private PasswordGenUtils(){
        throw new AssertionError("Instantiating utility class.");    
    }

    // generates the password

    public static String generatePassword(int n){
        if(n == 1){
            int num = rand.nextInt(46);
            Boolean cap = rand.nextBoolean();
            if(cap){
                return Character.toUpperCase(chars[num]) + "";
            }
            return chars[num] + "";
        }else{
            int num = rand.nextInt(46);
            return generatePassword(n-1) + chars[num];
        }
    }

    // checks for the password's condition

    public static boolean isValidPassword(String password){
        String conditions = "^(?=.*[0-9])"
                        + "(?=.*[a-z])(?=.*[A-Z])"
                        + "(?=.*[@#$%^&+=])"
                        + "(?=\\S+$).{8,20}$";

        Pattern p = Pattern.compile(conditions);


        if (password == null){
            return false;
        }

        Matcher m = p.matcher(password);

        return m.matches();
    }

    // just for check, can remove later

    public static void main(String[] args) {
        String password = "";

        while (isValidPassword(password) == false) {
            password = generatePassword(20);
        }

        System.out.println(password);
        System.out.println(isValidPassword(password));

    }

}
