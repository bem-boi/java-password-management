import javax.swing.*;

public class PasswordGenerator extends Page{
    public PasswordGenerator(int w, int h){
        super(w,h);
        this.frame = super.frame;
        this.panel = super.panel;
        this.back = super.back;
    }

    public void show(){
        frame.setTitle("Password Generator");
        panel.setLayout(null);

        back.addActionListener(new PopOutActionListener(frame));

        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
