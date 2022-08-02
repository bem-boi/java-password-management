import Util.AesUtil;
import Util.DatabaseUtil;
import Util.PasswordGenUtils;

import javax.swing.*;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.event.*;

import javax.crypto.*;
import java.security.*;
import java.sql.*;
import java.util.Arrays;

public class PasswordManager extends Page{
    
    private String password = "";
    private String websiteName = "";
    private String email = "";

    private JComboBox<String> websiteNamesBox;
    
    public PasswordManager(int w, int h, String user){
        super(w,h);
        this.frame = super.frame;
        this.panel = super.panel;
        this.back = super.back;
    }   

    public void show() throws SQLException{
        PasswordDB = DatabaseUtil.connectDB(PasswordUrl);
        UserDB = DatabaseUtil.connectDB(UserUrl);
        
        frame.setTitle("Password Manager");
        panel.setLayout(null);

        back.addActionListener(new PopOutActionListener(frame));
        // add another actionlistener for back button to reset the dropdown menu
        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    UserDB.close();
                    PasswordDB.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JTabbedPane tabbedPane = new JTabbedPane();
        JComponent panel1 = queryPane();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Query", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = addPane();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Add", panel2);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);

        JComponent panel3 = editPane();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Edit", panel3);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_3);

        JComponent panel4 = deletePane();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Delete", panel4);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_4);

        JComponent panel5 = checkPane();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Check", panel5);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_5);

        panel.add(tabbedPane);
        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Query pane
    protected JComponent queryPane(){ 
        JFrame dialog = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        JLabel choose = new JLabel("Choose a Website: ");
        choose.setBounds(100,50,120,20);
        panel.add(choose);
        
        String[] websiteNames = DatabaseUtil.getWebName(PasswordDB, user); //this gets updated everytime database is updated
        websiteNamesBox = new JComboBox<>(websiteNames);
        
        System.out.println(Arrays.toString(websiteNames));
        
        websiteNamesBox.setBounds(250, 50, 140, 20);
        panel.add(websiteNamesBox);
        
        JButton confirm = new JButton("Confirm");
        confirm.setBounds(500,50,100,50);
        confirm.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedWebsite = websiteNamesBox.getItemAt(websiteNamesBox.getSelectedIndex());
                String[] EmailPwIV = DatabaseUtil.queryButton(PasswordDB, user, selectedWebsite);
                String key = DatabaseUtil.getCipherKey(UserDB, user);
                String ogpassword;
                try {
                    ogpassword = AesUtil.decrypt(key, EmailPwIV[2], EmailPwIV[1]);
                    JOptionPane.showMessageDialog(dialog,"Password: " + ogpassword);  
                } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
                        | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panel.add(confirm);
        return panel;
    }

    // add pane
    protected JComponent addPane(){
        JTextPane textPane = new JTextPane();
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
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

        JTextField pwField = new JTextField(100);
        pwField.setBounds(190,150,165,25);
        panel.add(pwField);

        JLabel pwLabel = new JLabel("Password");
        pwLabel.setBounds(70,150,165,25);
        panel.add(pwLabel);

        // Error message
        JLabel errorLabel = new JLabel("");
        errorLabel.setBounds(70,200,165,25);
        errorLabel.setForeground(Color.RED);
        panel.add(errorLabel);

        // button to confirm the textfields and add it to database
        JButton confirm = new JButton("Confirm");
        confirm.setBounds(450,240,100,50);
        confirm.setFocusPainted(false);
        confirm.setVisible(false);
        panel.add(confirm);
        
        
        // password text and generate button
        JButton addPW = new JButton("Add");
        addPW.setBounds(255,240,100,50);
        addPW.setFocusPainted(false);
        addPW.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) { // add function to check if website for this user already exists or not
                textPane.setVisible(true);

                // reset password text in pane and error message
                textPane.setText("");
                errorLabel.setText("");
                
                websiteName = webField.getText();
                if (!websiteName.equals("")){
                    email = emailField.getText();
                    if(PasswordGenUtils.isValidEmail(email)){
                        password = pwField.getText();
                        if (PasswordGenUtils.isValidPassword(password)){
                            // textpane
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
                        }else{
                            textPane.setVisible(false);
                            errorLabel.setText("Invalid password");
                        }
    
                    }else{
                        textPane.setVisible(false);
                        errorLabel.setText("Not a valid email");
                    }
                }else{
                    textPane.setVisible(false);
                    errorLabel.setText("Put in a website name");
                }
            }
        });
        panel.add(addPW);

        confirm.addActionListener(new ActionListener(){
        
            @Override
            public void actionPerformed(ActionEvent e) {

                // ADD THE PASSWORD, EMAIL, WEBNAME TO DATABASE   
                try{
                    String key = DatabaseUtil.getCipherKey(UserDB, user);
                    String hashPW = DatabaseUtil.getHashPW(UserDB, user);
                    Cipher encryption = AesUtil.encryptCipher(key);
                    String cipherPW = AesUtil.encrypt(encryption, password);
                    String IV = AesUtil.getIV(encryption);

                    DatabaseUtil.insertPasswordGen(PasswordDB, user, websiteName, email, cipherPW, IV, hashPW); // insert to database

                    System.out.println("Password is " + password); // password that should be added to database

                    webField.setText("");
                    emailField.setText("");
                    pwField.setText("");
                    errorLabel.setText("");
                    textPane.setVisible(false);
                    confirm.setVisible(false);
                }catch (NoSuchAlgorithmException | NoSuchPaddingException| InvalidKeyException  | IllegalBlockSizeException | BadPaddingException ex){
                    throw new Error("Wrong");
                }
            }
        
        });
        return panel;
    }

    // edit pane
    protected JComponent editPane(){
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        JLabel choose = new JLabel("Choose a Website: ");
        choose.setBounds(100,50,120,20);
        panel.add(choose);

        JTextField pwField = new JTextField(100);
        pwField.setBounds(130,100,225,25);
        panel.add(pwField);

        JLabel pwLabel = new JLabel("New Password: ");
        pwLabel.setBounds(70,100,165,25);
        panel.add(pwLabel);
        
        String[] websiteNames = DatabaseUtil.getWebName(PasswordDB, user); //this gets updated everytime database is updated
        websiteNamesBox = new JComboBox<>(websiteNames);
        
        websiteNamesBox.setBounds(250, 50, 140, 20);
        panel.add(websiteNamesBox);
        
        JButton confirm = new JButton("Modify");
        confirm.setBounds(500,50,100,50);
        confirm.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedWebsite = websiteNamesBox.getItemAt(websiteNamesBox.getSelectedIndex());
                password = pwField.getText();
                try{
                    String key = DatabaseUtil.getCipherKey(UserDB, user);
                    Cipher newencryption = AesUtil.encryptCipher(key);
                    String newCipherPW = AesUtil.encrypt(newencryption, password);
                    String newIV = AesUtil.getIV(newencryption);
    
                    String passwordCheck = JOptionPane.showInputDialog("Type in your password to modify password:");
                    if (DatabaseUtil.checkPassword(UserDB, passwordCheck, user)){
                        DatabaseUtil.changeButton(PasswordDB, user, selectedWebsite, newCipherPW, newIV);
                    }else{
                        System.out.println("wrong password");
                    }
                }catch (NoSuchAlgorithmException | NoSuchPaddingException| InvalidKeyException  | IllegalBlockSizeException | BadPaddingException ex){
                    throw new Error("Wrong");
                }
            }
        });
        panel.add(confirm);
        return panel;
    }

    // delete pane
    protected JComponent deletePane(){
        JPanel panel = new JPanel();
        JLabel tabs = new JLabel("Delete");
        tabs.setBounds(0,0,20,40);
        JButton click = new JButton("Clik me");
        click.setBounds(3,5,40,40);
        panel.add(click);
        panel.add(tabs);
        return panel;
    }

    // check pane
    protected JComponent checkPane(){ // use isvalidpassword from pwgenutil
        JPanel panel = new JPanel();
        JLabel tabs = new JLabel("Check");
        tabs.setBounds(0,0,20,40);
        JButton click = new JButton("Clik me");
        click.setBounds(3,5,40,40);
        panel.add(click);
        panel.add(tabs);
        return panel;
    }
}
