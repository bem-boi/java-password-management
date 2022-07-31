import Util.PasswordGenUtils;
import Util.AesUtil;
import Util.DatabaseUtil;

import javax.crypto.*;
import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.event.*;
import java.security.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.awt.Color;

public class PasswordGenerator extends Page{
    
    private String password = "hold";
    private int password_length;
    private String websiteName;
    private String email;
    private String url = "jdbc:mysql://127.0.0.1/test";

    private JTextPane textPane = new JTextPane();

    public PasswordGenerator(int w, int h, String user){
        super(w,h);
        this.frame = super.frame;
        this.panel = super.panel;
        this.back = super.back;
    }

    public void show() throws SQLException{
        frame.setTitle("Password Generator");
        panel.setLayout(null);

        Connection con = DatabaseUtil.connectDB(url);

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

        // Error message
        JLabel errorLabel = new JLabel("");
        errorLabel.setBounds(70,200,165,25);
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel);

        // remove all the textfields (another action listener for the back button)
        back.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                webField.setText("");
                emailField.setText("");
                pwLengthField.setText("");
                textPane.setVisible(false);
            }

        });

        // button to confirm the textfields and add it to database
        JButton confirm = new JButton("Confirm");
        confirm.setBounds(450,240,100,50);
        confirm.setFocusPainted(false);
        confirm.setVisible(true);
        panel.add(confirm);
        
        // password text and generate button
        JButton generatePW = new JButton("Generate");
        generatePW.setBounds(255,240,100,50);
        generatePW.setFocusPainted(false);
        panel.add(generatePW);
        generatePW.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                // Jtextpane appears when I click the generate button
                textPane.setVisible(true);

                // reset password text in pane and error message
                textPane.setText("");
                errorLabel.setText("");

                // generate password
                boolean cond = true;
                websiteName = webField.getText();
                email = emailField.getText();
                if (PasswordGenUtils.isValidEmail(email)){
                    try {
                        while (cond){
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

                        confirm.setVisible(true);
                    }catch (NumberFormatException inputError){
                        textPane.setVisible(false);
                        errorLabel.setText("Put in a number");
                    }
                }else{
                    textPane.setVisible(false);
                    errorLabel.setText("Not a valid email");
                }
            }

        });

        confirm.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e) {

                // ADD THE PASSWORD, EMAIL, WEBNAME TO DATABASE   
                try{
                    SecretKey key = AesUtil.generateKey();
                    String keystring = AesUtil.keyString(key);
                    Cipher encryption = AesUtil.encryptCipher(key);
                    String cipherPW = AesUtil.encrypt(encryption, password);
                    String IV = AesUtil.getIV(encryption);
                    String hashPW = "$2a$10$PS7VPSbAJNYglI2cT.aUiOJtsyEznDoGrMrM/wp0U9D2w/ATeN522";

                    DatabaseUtil.insertPasswordGen(con, user, websiteName, email, cipherPW, IV, hashPW);
            
                    System.out.println(password); // check that latest password is added to database
                    webField.setText("");
                    emailField.setText("");
                    pwLengthField.setText("");
                    errorLabel.setText("");
                    textPane.setVisible(false);
                    confirm.setVisible(false);
                }catch (NoSuchAlgorithmException | NoSuchPaddingException| InvalidKeyException  | IllegalBlockSizeException | BadPaddingException ex){
                    throw new Error("Wrong");
                }
            }
        
        });

        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
