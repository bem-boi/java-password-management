import java.awt.event.*;
import java.sql.SQLException;

import javax.swing.JFrame;


public class PushInActionListener implements ActionListener{

    private Page nextPage;
    private Page currentPage;
    private JFrame frame;

    public PushInActionListener (Page currentPage, Page nextPage, JFrame frame) {
            this.nextPage = nextPage;
            this.currentPage = currentPage;
            this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Application.backStack.push(this.currentPage);
        try {
            nextPage.show();
            this.frame.dispose();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

}
