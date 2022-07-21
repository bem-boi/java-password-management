import java.util.Stack;


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
        Application app = new Application();
        app.runApp();
    }
}

