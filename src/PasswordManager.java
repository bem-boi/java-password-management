import javax.swing.*;

public class PasswordManager extends Page{
    public PasswordManager(int w, int h){
        super(w,h);
        this.frame = super.frame;
        this.panel = super.panel;
        this.back = super.back;
    }   

    public void show(){
        frame.setTitle("Password Manager");
        panel.setLayout(null);

        back.addActionListener(new PopOutActionListener(frame));

        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
