package Util;

import Util.BCrypt.BCrypt;

public final class HashUtil {
    
    private HashUtil(){
        throw new AssertionError("Instantiating utility class.");
    }

    public static String createHash(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static Boolean checkHash(String password, String hash){
        return (BCrypt.checkpw(password, hash));
    }

    // example:
    // public static void main(String[] args) {
    //     String plain_password = "abcdefg";
    //     String pw_hash = BCrypt.hashpw(plain_password, BCrypt.gensalt());
    //     System.out.println(pw_hash);

    //     if (BCrypt.checkpw("abcdefg", pw_hash))
    //         System.out.println("It matches");
    //     else
    //         System.out.println("It does not match");
    // }

}
