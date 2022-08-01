import javax.swing.JFrame;
import java.awt.event.*;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class PopOutActionListener implements ActionListener{
    
    private JFrame frame;

    public PopOutActionListener(JFrame frame){
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Template page = Application.backStack.pop();
        try {
            page.show();
            this.frame.dispose();
        } catch (SQLException e1) {
            e1.printStackTrace();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
    }

}
