import java.sql.*;
import java.util.Stack;

import Util.DatabaseUtil;

// import Util.PasswordGenUtils;

// import Util.AesUtil;
// import javax.crypto.*;

public class Application {

    public static Stack<Page> backStack;

    public Application(){
        backStack = new Stack<Page>();
    }

    public void runApp() throws SQLException{
        Connection UserDB = DatabaseUtil.connectDB("jdbc:mysql://127.0.0.1:13306/key_vault",System.getenv("USER_KEYVAULT"),System.getenv("PASSWORD_KEYVAULT"));
        if(!DatabaseUtil.firstTime(UserDB)){
            Login loginPage = new Login(350,200);
            loginPage.show();
            UserDB.close();
        }else{
            Register registerPage = new Register(350, 200);
            registerPage.show();
            UserDB.close();
        }
    }

    public static void main(String[] args) throws Exception {
        
        // run GUI app 
        Application app = new Application();
        app.runApp();

        
        // password generator
        // String password = PasswordGenUtils.generatePassword(20);

        // while (PasswordGenUtils.isValidPassword(password) == false) {
        //     password = PasswordGenUtils.generatePassword(20);
        // }

        // System.out.println(password);
        // System.out.println(PasswordGenUtils.isValidPassword(password));

        // AES 
        // String text = "welcome back my name is beam";

        // SecretKey key = AesUtil.generateKey();
        // System.out.println(AesUtil.keyString(key));

        // Cipher encryptionCipher = AesUtil.encryptCipher(key);
        
        // String stringkey = AesUtil.keyString(key);
        // System.out.println(stringkey);

        // String ciphertext = AesUtil.encrypt(encryptionCipher, text);
        // System.out.println(ciphertext);

        // String IV = AesUtil.getIV(encryptionCipher);
        // System.out.println(IV);

        // String original = AesUtil.decrypt(stringkey, IV, ciphertext);
        // System.out.println(original);
    }
}

