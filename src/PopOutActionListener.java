import javax.swing.JFrame;
import java.awt.event.*;

public class PopOutActionListener implements ActionListener{
    
    private JFrame frame;

    public PopOutActionListener(JFrame frame){
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Template page = Application.backStack.pop();
        page.show();
        this.frame.dispose();
    }

}
