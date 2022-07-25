import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;

public class Register extends Template{
    
    public Register(int w, int h){
        super(w, h);
        this.frame = super.frame;
        this.panel = super.panel;
    }

    public void show(){
        frame.setTitle("Register Page");
        frame.setJMenuBar(null);

        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(10,20,80,25);
        panel.add(userLabel);

        JTextField userText = new JTextField(20);
        userText.setBounds(100,20,165,25);
        panel.add(userText);

        JLabel pwLabel = new JLabel("Password");
        pwLabel.setBounds(10,50,80,25);
        panel.add(pwLabel);

        JPasswordField pwText = new JPasswordField(100);
        pwText.setBounds(100,50,165,25);
        panel.add(pwText);

        JButton signUp = new JButton("Sign up");
        signUp.setBounds(20,100,80,25);
        panel.add(signUp);

        signUp.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                MainPage MainPage = new MainPage(800,600);
                MainPage.show();
                frame.dispose();
            }

        });

        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
