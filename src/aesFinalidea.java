import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.*;

public class aesFinalidea {
    public static void main(String[] args) throws Exception {
        
        //generate secret key

        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(128);
        SecretKey key = gen.generateKey(); //each user gets different key

      
        //encryption and getting IV
        // 1
        Cipher encryption1 = Cipher.getInstance("AES/GCM/NoPadding");
        encryption1.init(Cipher.ENCRYPT_MODE,key); //each encryption method is encrypted with their own key
        byte[] IV1 = encryption1.getIV(); //IV is obtained from initializing encryption method, so everytime I encrypt, I just use the new IV
        String IV1String = Base64.getEncoder().encodeToString(IV1);
        byte[] eStringByte1 = encryption1.doFinal("hello".getBytes());
        String estring1 = Base64.getEncoder().encodeToString(eStringByte1);
      
        // 2
        Cipher encryption2 = Cipher.getInstance("AES/GCM/NoPadding");
        encryption2.init(Cipher.ENCRYPT_MODE,key);
        byte[] IV2 = encryption2.getIV(); //IV is obtained from initializing encryption method
        String IV2String = Base64.getEncoder().encodeToString(IV2);
        byte[] eStringByte2 = encryption2.doFinal("hello".getBytes());
        String estring2 = Base64.getEncoder().encodeToString(eStringByte2);

      
        //decryption with respective IV
        // 1
        Cipher decryption1 = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec1 = new GCMParameterSpec(128,IV1);
        decryption1.init(Cipher.DECRYPT_MODE,key,spec1); //everytime I decrypt, i use the IV from that specific encryption method. maybe I keep IV in the same database as cipher text too. The user will also use their own respective key to decrypt
        String originalString1 = new String(decryption1.doFinal(eStringByte1));

        // 2
        Cipher decryption2 = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec2 = new GCMParameterSpec(128,IV2);
        decryption2.init(Cipher.DECRYPT_MODE,key,spec2);
        String originalString2 = new String(decryption2.doFinal(eStringByte2));
        
      
        //printing out all results

        System.out.println("key for both is: " + Base64.getEncoder().encodeToString(key.getEncoded()));
        System.out.println();
        System.out.println("encrypted text 1 is: " + estring1);
        System.out.println("encrypted text 2 is: " + estring2);
        System.out.println();
        System.out.println("IV 1 is: " + IV1String);
        System.out.println("IV 2 is: " + IV2String);
        System.out.println();
        System.out.println("original text 1 is: " + originalString1);
        System.out.println("original text 2 is: " + originalString2);
      
    }
}
