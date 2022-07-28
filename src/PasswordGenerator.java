import Util.PasswordGenUtils;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.event.*;


public class PasswordGenerator extends Page{
    
    private static int password_length = 0;
    private static String websiteName = "";
    private static String email = "";

    private static JTextPane textPane = new JTextPane();

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

        // Buttons and Labels

        JTextField webField = new JTextField(100);
        webField.setBounds(190,50,165,25);
        panel.add(webField);

        JLabel webLabel = new JLabel("Website Name");
        webLabel.setBounds(70,50,165,25);
        panel.add(webLabel);

        JTextField emailField = new JTextField(100);
        emailField.setBounds(130,100,225,25);
        panel.add(emailField);

        JLabel emailLabel = new JLabel("Email");
        emailLabel.setBounds(70,100,165,25);
        panel.add(emailLabel);

        JTextField pwLengthField = new JTextField(100);
        pwLengthField.setBounds(190,150,165,25);
        panel.add(pwLengthField);

        JLabel pwlengthLabel = new JLabel("Password Length");
        pwlengthLabel.setBounds(70,150,165,25);
        panel.add(pwlengthLabel);

        // password text and generate button

        JLabel passwordtext = new JLabel("");
        passwordtext.setBounds(420,200,500,50);
        panel.add(passwordtext);

        JButton generatePW = new JButton("Generate");
        generatePW.setBounds(420,400,100,50);
        panel.add(generatePW);
        generatePW.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                
                // reset password text in pane
                textPane.setText("");

                // generate password
                
                boolean cond = true;
                String password = "hold";
                while (cond){
                    websiteName = webField.getText();
                    email = emailField.getText();
                    password_length = Integer.parseInt(pwLengthField.getText());
                    password = PasswordGenUtils.generatePassword(password_length);
                    if (PasswordGenUtils.isValidPassword(password)){
                        cond = false;
                        break;
                    }
                }
                passwordtext.setText(password);
                
                // Jtextpane appears when I click the generate button
                
                textPane.setEditable(false);
                // textPane.setText(passwordtext.getText());
                StyledDocument doc = textPane.getStyledDocument();

                SimpleAttributeSet keyWord = new SimpleAttributeSet();
                StyleConstants.setForeground(keyWord, Color.RED);
                StyleConstants.setBackground(keyWord, Color.YELLOW);
                StyleConstants.setBold(keyWord, true);

                try{
                    doc.insertString(0, "Password: " + password, null );
                    doc.insertString(doc.getLength(), "\nEnd of text", keyWord );
                }
                catch (Exception E){
                    System.out.println(E);
                }
                textPane.setBounds(400,50,400,100);
                panel.add(textPane);
            }

        });

        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
