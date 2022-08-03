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

}
