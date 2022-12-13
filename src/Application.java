import java.sql.*;
import java.util.Stack;

import Util.DatabaseUtil;

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
    }
}

