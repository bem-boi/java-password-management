import java.util.Stack;

import javax.crypto.*;

import Util.AesUtil;

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
        // Application app = new Application();
        // app.runApp();

        String text = "welcome back my name is beam";

        SecretKey key = AesUtil.generateKey();
        System.out.println(AesUtil.keyString(key));

        Cipher encryptionCipher = AesUtil.encryptCipher(key);

        String ciphertext = AesUtil.encrypt(encryptionCipher, text);
        System.out.println(ciphertext);

        String IV = AesUtil.getIV(encryptionCipher);
        System.out.println(IV);

        String original = AesUtil.decrypt(key, IV, ciphertext);
        System.out.println(original);
    }
}

