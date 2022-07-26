import Util.PasswordGenUtils;

import javax.swing.*;
import java.awt.event.*;


public class PasswordGenerator extends Page{
    
    private static int PASSWORD_LENGTH = 0;

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
                    password = PasswordGenUtils.generatePassword(PASSWORD_LENGTH);
                    if (PasswordGenUtils.isValidPassword(password)){
                        cond = false;
                        break;
                    }
                }
                passwordtext.setText(password);
            }

        });

        JTextField pwLength = new JTextField(100);
        pwLength.setBounds(100,50,165,25);
        panel.add(pwLength);
        

        JButton pwLengthClick = new JButton("OK");
        pwLengthClick.setBounds(110,100,100,25);
        panel.add(pwLengthClick);
        pwLengthClick.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                PASSWORD_LENGTH = Integer.parseInt(pwLength.getText());
            }

        });

        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
