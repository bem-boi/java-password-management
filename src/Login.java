import Util.DatabaseUtil;
import Util.HashUtil;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.awt.Color;

public class Login extends Template{
    public Login(int w, int h){
        super(w, h);
        this.frame = super.frame;
        this.panel = super.panel;
    }

    public void show(){
        try {
            UserDB = DatabaseUtil.connectDB(UserUrl);

            frame.setTitle("Login Page");
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
    
            JButton login = new JButton("Login");
            login.setBounds(20,100,80,25);
            panel.add(login);
    
            JButton register = new JButton("Register");
            register.setBounds(110,100,100,25);
            panel.add(register);
    
            JButton cancel = new JButton("Cancel");
            cancel.setBounds(220, 100, 100, 25);
            panel.add(cancel);
    
            JLabel wrong = new JLabel("");
            wrong.setBounds(20,75,200,25);
            panel.add(wrong);
    
            login.addActionListener(new ActionListener(){
    
                @Override
                public void actionPerformed(ActionEvent e) {
                    user = userText.getText(); // check if user is in database first else return wrong
                    if (DatabaseUtil.checkUsername(UserDB,user)){
                        char[] passwordchar = pwText.getPassword();
                        String password = new String(passwordchar);
                        String hashPW = DatabaseUtil.checkUsernamePasswordLogin(UserDB, user);
                        if(HashUtil.checkHash(password, hashPW)){
                            MainPage MainPage = new MainPage(800,600, user);
                            MainPage.show();
                            frame.dispose();
                        }else{
                            wrong.setText("Wrong username or password");
                            wrong.setForeground(Color.RED);
                        }
                    }else{
                        wrong.setText("Wrong username or password");
                        wrong.setForeground(Color.RED);
                    }
                    
                }
    
            });
    
            register.addActionListener(new ActionListener(){
    
                @Override
                public void actionPerformed(ActionEvent e) {
                    Register registerPage = new Register(350,200);
                    registerPage.show();
                    frame.dispose();
                }
    
            });
    
            cancel.addActionListener(new ActionListener(){
    
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
    
            });
    
            frame.setResizable(false);
    
            panel.revalidate();
            panel.repaint();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        
    }
}
