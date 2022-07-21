import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.*;

public class AES {
    private SecretKey key;
    private int KEY_SIZE = 128;
    private int T_LEN = 128;
    private byte[] IV;

    public void init() throws Exception{
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(KEY_SIZE);
        key = gen.generateKey();
    }

    public void init(String secretKey, String IV){
        key = new SecretKeySpec(decode(secretKey),"AES");
        this.IV = decode(IV);
    }

    public String encryptOld(String x) throws Exception{
        byte[] eX = x.getBytes();
        Cipher encryptionc = Cipher.getInstance("AES/GCM/NoPadding");
        encryptionc.init(Cipher.ENCRYPT_MODE,key);
        IV = encryptionc.getIV();
        byte[] eByte = encryptionc.doFinal(eX);
        return encode(eByte);
    }

    public String encryptNEW(String x) throws Exception{
        byte[] eX = x.getBytes();
        Cipher encryptionc = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN, IV);
        encryptionc.init(Cipher.ENCRYPT_MODE,key,spec);
        IV = encryptionc.getIV();
        byte[] eByte = encryptionc.doFinal(eX);
        return encode(eByte);
    }

    public String decrypt(String encryptedS) throws Exception{
        byte[] decodedByte = decode(encryptedS);
        Cipher decryptionc = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec spec = new GCMParameterSpec(T_LEN,IV);
        decryptionc.init(Cipher.DECRYPT_MODE,key,spec);
        byte[] decryptedByte = decryptionc.doFinal(decodedByte);
        return new String(decryptedByte);
    }

    private String encode(byte[] x){
        return Base64.getEncoder().encodeToString(x);
    }

    private byte[] decode(String x){
        return Base64.getDecoder().decode(x);
    }

    private void exportKeys(){
        System.out.println("Secret key: "+ encode(key.getEncoded()));
        System.out.println("IV: "+ encode(IV));
    }

    public static void main(String[] args) throws Exception{
        AES aes = new AES();
        aes.init("iOBFqYve1kUZjDvQrJNNCw==","3t2CTD4unesy+tV/");
        String encrypted = aes.encryptNEW("wassup");
        String decrypted = aes.decrypt(encrypted);

        System.out.println("new encrypted: "+encrypted);
        System.out.println(decrypted);
        aes.exportKeys();
    }

}
