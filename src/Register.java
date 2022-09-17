import javax.swing.*;

import Util.AesUtil;
import Util.DatabaseUtil;
import Util.HashUtil;

import java.awt.event.*;
import java.awt.Color;

import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class Register extends Template{
    
    public Register(int w, int h){
        super(w, h);
        this.frame = super.frame;
        this.panel = super.panel;
    }

    public void show(){
        try{
            UserDB = DatabaseUtil.connectDB(UserUrl,System.getenv("USER_KEYVAULT"),System.getenv("PASSWORD_KEYVAULT"));

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

            JLabel wrong = new JLabel("");
            wrong.setBounds(20,75,200,25);
            panel.add(wrong);

            JButton signUp = new JButton("Sign up");
            signUp.setBounds(20,100,80,25);
            panel.add(signUp);

            signUp.addActionListener(new ActionListener(){

                @Override
                public void actionPerformed(ActionEvent e) {
                    user = userText.getText();
                    if (!DatabaseUtil.checkUsername(UserDB, user)){
                        char[] pw = pwText.getPassword();
                        String password = new String(pw);
                        String hashPW = HashUtil.createHash(password); 
                        MainPage MainPage = new MainPage(800,600, user);
                        String cipherKey;
                        try {
                            cipherKey = AesUtil.keyString(AesUtil.generateKey());
                            DatabaseUtil.insertUsernamePasswordRegister(UserDB, user, hashPW, cipherKey);
                            UserDB.close();
                        } catch (NoSuchAlgorithmException | SQLException e1) {
                            e1.printStackTrace();
                        }
                        MainPage.show();
                        frame.dispose(); // check if user is in database first
                    }else{
                        wrong.setText("User already exists");
                        wrong.setForeground(Color.RED);
                    }
                }

            });

            panel.revalidate();
            panel.repaint();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        }catch (SQLException e){
            throw new Error(e);
        }
    }
}
