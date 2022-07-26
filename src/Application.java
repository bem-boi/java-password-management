import java.util.Stack;

import Util.PasswordGenUtils;

import Util.AesUtil;
import javax.crypto.*;

public class Application {

    //card layout

    public static Stack<Page> backStack;

    public Application(){
        backStack = new Stack<Page>();
    }

    public void runApp(){
        Login loginPage = new Login(350,200);
        loginPage.show();
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

        // String ciphertext = AesUtil.encrypt(encryptionCipher, text);
        // System.out.println(ciphertext);

        // String IV = AesUtil.getIV(encryptionCipher);
        // System.out.println(IV);

        // String original = AesUtil.decrypt(key, IV, ciphertext);
        // System.out.println(original);
    }
}

