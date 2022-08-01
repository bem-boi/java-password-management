import java.security.NoSuchAlgorithmException;
import java.sql.*;

import javax.swing.*;

public abstract class Template {
    protected JFrame frame;
    protected JPanel panel;
    protected int width;
    protected int height;
    protected static String user;

    protected static String UserUrl = "jdbc:mysql://127.0.0.1/test";
    protected static Connection UserDB;
    protected static String PasswordUrl = "jdbc:mysql://127.0.0.1/test";
    protected static Connection PasswordDB;

    public abstract void show() throws SQLException, NoSuchAlgorithmException;

    public Template(int w, int h){
        frame = new JFrame();
        panel = new JPanel();
        width = w;
        height = h;

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width,height);
    }

}
