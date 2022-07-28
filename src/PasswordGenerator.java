import Util.PasswordGenUtils;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.event.*;


public class PasswordGenerator extends Page{
    
    private String password = "hold";
    private int password_length;
    private String websiteName;
    private String email;

    private JTextPane textPane = new JTextPane();

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

        // remove all the textfields (another action listener for the back button)
        back.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                webField.setText("");
                emailField.setText("");
                pwLengthField.setText("");
                textPane.hide();
            }

        });

        // password text and generate button
        JButton generatePW = new JButton("Generate");
        generatePW.setBounds(255,220,100,50);
        generatePW.setFocusPainted(false);
        panel.add(generatePW);
        generatePW.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // Jtextpane appears when I click the generate button
                textPane.show();

                // reset password text in pane
                textPane.setText("");

                // generate password
                boolean cond = true;
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
                
                textPane.setEditable(false);
                StyledDocument doc = textPane.getStyledDocument();
                try{
                    doc.insertString(doc.getLength(), "Website: " + websiteName + "\n", null);
                    doc.insertString(doc.getLength(), "Email: " + email + "\n", null );
                    doc.insertString(doc.getLength(), "Password: " + password + "\n", null );
                }
                catch (Exception E){
                    System.out.println(E);
                }
                textPane.setBounds(400,50,300,100);
                panel.add(textPane);
            }

        });

        // button to confirm the textfields and add it to database
        JButton confirm = new JButton("Confirm");
        confirm.setBounds(450,220,100,50);
        confirm.setFocusPainted(false);
        panel.add(confirm);
        confirm.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // ADD THE PASSWORD, EMAIL, WEBNAME TO DATABASE
                System.out.println("added to database");
                webField.setText("");
                emailField.setText("");
                pwLengthField.setText("");
                textPane.hide();
            }

        });

        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
