import java.awt.event.*;
import javax.swing.JFrame;


public class PageActionListener implements ActionListener{

    private Page nextPage;
    private Page currentPage;
    private JFrame frame;

    public PageActionListener (Page currentPage, Page nextPage, JFrame frame) {
            this.nextPage = nextPage;
            this.currentPage = currentPage;
            this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Application.backStack.push(this.currentPage);
        nextPage.show();
        this.frame.dispose();
    }

}
