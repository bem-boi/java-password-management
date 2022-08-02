package Util;

import java.util.Base64;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

// http://www.cse.yorku.ca/~franck/teaching/2006-07/1030/notes/chapter1.pdf
// https://stackoverflow.com/questions/49658798/aes-gcm-encryption-with-jdk-1-8   GCM stuff

public final class AesUtil{

    private AesUtil(){
        throw new AssertionError("Instantiating utility class.");    
    }

    // generates the secret key for each user
    public static SecretKey generateKey() throws NoSuchAlgorithmException{
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(256);
        SecretKey key = gen.generateKey();
        return key;
    }

    // returns the secretkey to the user
    public static String keyString(SecretKey key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    // creates the encryption cipher 
    public static Cipher encryptCipher(String key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        byte[] decodedKey = Base64.getDecoder().decode(key);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        
        Cipher encryption = Cipher.getInstance("AES/GCM/NoPadding");
        encryption.init(Cipher.ENCRYPT_MODE,originalKey); //each encryption method is encrypted with their own key
        return encryption;
    }
    
    // returns the cipher text with its specific encryption method
    public static String encrypt(Cipher encryption, String text) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        byte[] eStringByte = encryption.doFinal(text.getBytes());
        String estring = Base64.getEncoder().encodeToString(eStringByte);
        return estring;
    }

    // returns the IV with respective specific encryption method
    public static String getIV(Cipher encryption){
        byte[] IV = encryption.getIV();
        String IVString = Base64.getEncoder().encodeToString(IV);
        return IVString;
    }

    // decrypts the cipher text with its IV and its secret key
    public static String decrypt(String key, String IV, String ciphertext) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException{
        byte[] IVByte = Base64.getDecoder().decode(IV);
        byte[] CipherTextByte = Base64.getDecoder().decode(ciphertext);
        byte[] decodedKey = Base64.getDecoder().decode(key);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        Cipher decryption = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(128,IVByte);
        decryption.init(Cipher.DECRYPT_MODE,originalKey,spec); //everytime I decrypt, i use the IV from that specific encryption method. maybe I keep IV in the same database as cipher text too. The user will also use their own respective key to decrypt
        return new String(decryption.doFinal(CipherTextByte));
    }
    
    // og should be 'Q6R#:}N@?"uijVOF"$.rLfS'
    // public static void main(String[] args) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchPaddingException {
    //     String og = decrypt("SS0S1c4ehIuVqc5B1VSi5ZNXpv+AMM0rz0cpYDx/s98=", "tTyyng60rEc+j1oe", "+haekr6m82obNJWw+tYH3EOuSEXkNuOQXpiklzw=");
    //     System.out.println(og);
    // }


}