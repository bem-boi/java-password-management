import Util.PasswordGenUtils;

import javax.swing.*;
import java.awt.event.*;


public class PasswordGenerator extends Page{
    
    private static int password_length = 0;
    private static String websiteName = "";
    private static String email = "";

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

        JLabel passwordtext = new JLabel("");
        passwordtext.setBounds(420,200,500,50);
        panel.add(passwordtext);

        JButton generatePW = new JButton("Generate");
        generatePW.setBounds(420,400,100,50);
        panel.add(generatePW);
        generatePW.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                boolean cond = true;
                String password = "hold";
                while (cond){
                    password = PasswordGenUtils.generatePassword(password_length);
                    if (PasswordGenUtils.isValidPassword(password)){
                        cond = false;
                        break;
                    }
                }
                passwordtext.setText(password);
            }

        });
        
        JTextField webField = new JTextField(100);
        webField.setBounds(120,50,165,25);
        panel.add(webField);

        JTextField emailField = new JTextField(200);
        emailField.setBounds(120,100,165,25);
        panel.add(emailField);

        JTextField pwLengthField = new JTextField(100);
        pwLengthField.setBounds(120,150,165,25);
        panel.add(pwLengthField);


        JButton OK = new JButton("OK");
        OK.setBounds(120,220,100,25);
        panel.add(OK);
        OK.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                websiteName = webField.getText();
                email = emailField.getText();
                password_length = Integer.parseInt(pwLengthField.getText());
            }

        });

        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
