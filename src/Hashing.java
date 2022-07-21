import BCrypt.BCrypt;

public class Hashing {
    
    public static void main(String[] args) {
        String plain_password = "abcdefg";
        String pw_hash = BCrypt.hashpw(plain_password, BCrypt.gensalt());
        System.out.println(pw_hash);

        if (BCrypt.checkpw("abcdefg", pw_hash))
            System.out.println("It matches");
        else
            System.out.println("It does not match");
    }
    
}
