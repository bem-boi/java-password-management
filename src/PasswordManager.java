import Util.AesUtil;
import Util.DatabaseUtil;
import Util.PasswordGenUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.StyledDocument;
import java.awt.Color;
import java.awt.event.*;

import javax.crypto.*;
import java.security.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class PasswordManager extends Page{
    
    private String password;
    private String websiteName;
    private String email;
    private String passwordRetype;
    
    public PasswordManager(int w, int h, String user){
        super(w,h);
        this.frame = super.frame;
        this.panel = super.panel;
        this.back = super.back;
    }   

    public void show() throws SQLException{
        PasswordDB = DatabaseUtil.connectDB(PasswordUrl, System.getenv("USER_PWVAULT"), System.getenv("PASSWORD_PWVAULT"));
        UserDB = DatabaseUtil.connectDB(UserUrl,System.getenv("USER_KEYVAULT"),System.getenv("PASSWORD_KEYVAULT"));
        
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
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = editPane();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Edit", panel3);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        JComponent panel4 = deletePane();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Delete", panel4);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        JComponent panel5 = checkPane();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Check", panel5);
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);

        tabbedPane.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                if(tabbedPane.getSelectedIndex() != 1){
                    String[] websiteNames = DatabaseUtil.getWebName(PasswordDB, user);
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(websiteNames);
                    ((JComboBox<String>) panel1.getComponent(0)).setModel(model);
                    ((JComboBox<String>) panel3.getComponent(0)).setModel(model);
                    ((JComboBox<String>) panel4.getComponent(0)).setModel(model);
                }
            }

        });
        tabbedPane.revalidate();
        panel.add(tabbedPane);
        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /* -------------------------------------------QUERY PANE---------------------------------------------- */
    protected JComponent queryPane(){ 
        JFrame dialog = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        String[] websiteNames = DatabaseUtil.getWebName(PasswordDB, user); //this gets updated everytime database is updated
        JComboBox<String> websiteNamesBoxQuery = new JComboBox<>(websiteNames);
        
        System.out.println(Arrays.toString(websiteNames));
        
        websiteNamesBoxQuery.setBounds(250, 50, 140, 20);
        panel.add(websiteNamesBoxQuery);
        
        JLabel choose = new JLabel("Choose a Website: ");
        choose.setBounds(100,50,120,20);
        panel.add(choose); 
        
        JButton confirmQuery = new JButton("Confirm");
        confirmQuery.setBounds(470,50,100,50);
        confirmQuery.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedWebsite = websiteNamesBoxQuery.getItemAt(websiteNamesBoxQuery.getSelectedIndex());
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
        panel.add(confirmQuery);
        return panel;

    }

    /* -------------------------------------------ADD PANE---------------------------------------------- */
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
        JButton confirmAdd = new JButton("Confirm");
        confirmAdd.setBounds(450,240,100,50);
        confirmAdd.setFocusPainted(false);
        confirmAdd.setVisible(false);
        panel.add(confirmAdd);
        
        
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
                        if (PasswordGenUtils.isValidPassword(password)){ // allows user to still input whatever password they want, it just helps notify them that their password is not secure
                            textPane.setVisible(true);
                            errorLabel.setText("");
                        }else{
                            textPane.setVisible(true);
                            errorLabel.setText("Weak password");
                        }

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
                        
                        confirmAdd.setVisible(true);
    
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

        confirmAdd.addActionListener(new ActionListener(){
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
                    confirmAdd.setVisible(false);
                }catch (NoSuchAlgorithmException | NoSuchPaddingException| InvalidKeyException  | IllegalBlockSizeException | BadPaddingException ex){
                    throw new Error("Wrong");
                }
            }
        });

        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                webField.setText("");
                emailField.setText("");
                pwField.setText("");
                errorLabel.setText("");
                textPane.setVisible(false);
                confirmAdd.setVisible(false);
            }
        });

        return panel;
    }

    /* -------------------------------------------EDIT PANE---------------------------------------------- */
    protected JComponent editPane(){ 
        JFrame errorDialog = new JFrame();
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        String[] websiteNames = DatabaseUtil.getWebName(PasswordDB, user); //this gets updated everytime database is updated
        JComboBox<String> websiteNamesBoxEdit = new JComboBox<>(websiteNames);
        
        websiteNamesBoxEdit.setBounds(250, 50, 140, 20);
        panel.add(websiteNamesBoxEdit);

        JLabel choose = new JLabel("Choose a Website: ");
        choose.setBounds(100,50,120,20);
        panel.add(choose);

        JTextField pwField = new JTextField(100);
        pwField.setBounds(250,100,225,25);
        panel.add(pwField);

        JLabel pwLabel = new JLabel("New Password: ");
        pwLabel.setBounds(100,100,165,25);
        panel.add(pwLabel);

        JTextField pwRetypeField = new JTextField(100);
        pwRetypeField.setBounds(250,150,225,25);
        panel.add(pwRetypeField);

        JLabel pwRetypeLabel = new JLabel("Retype your Password: ");
        pwRetypeLabel.setBounds(100,150,165,25);
        panel.add(pwRetypeLabel);
        
        JLabel errorMessage = new JLabel("");
        errorMessage.setBounds(100,200,165,25);
        panel.add(errorMessage);

        
        JButton confirmEdit = new JButton("Modify");
        confirmEdit.setBounds(550,75,100,50);
        confirmEdit.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedWebsite = websiteNamesBoxEdit.getItemAt(websiteNamesBoxEdit.getSelectedIndex());
                passwordRetype = pwRetypeField.getText();
                password = pwField.getText();
                if (password.equals(passwordRetype)){
                    if(!PasswordGenUtils.isValidPassword(password)){
                        errorMessage.setText("Password is weak");
                        errorMessage.setForeground(Color.RED);
                    }
                    try{
                        String key = DatabaseUtil.getCipherKey(UserDB, user);
                        Cipher newencryption = AesUtil.encryptCipher(key);
                        String newCipherPW = AesUtil.encrypt(newencryption, password);
                        String newIV = AesUtil.getIV(newencryption);
        
                        String passwordCheck = JOptionPane.showInputDialog("Type in your password to modify password:");
                        if (DatabaseUtil.checkPassword(UserDB, passwordCheck, user)){
                            pwField.setText("");
                            pwRetypeField.setText("");
                            errorMessage.setText("");
                            DatabaseUtil.changeButton(PasswordDB, user, selectedWebsite, newCipherPW, newIV);
                        }else{
                            JOptionPane.showMessageDialog(errorDialog, "Wrong password", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }catch (NoSuchAlgorithmException | NoSuchPaddingException| InvalidKeyException  | IllegalBlockSizeException | BadPaddingException ex){
                        throw new Error("Wrong");
                    }
                }else{
                    errorMessage.setText("Invalid password");
                    errorMessage.setForeground(Color.RED);
                }
            }
        });
        panel.add(confirmEdit);

        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                pwField.setText("");
                pwRetypeField.setText("");
            }
        });

        return panel;
    }

    /* -------------------------------------------DELETE PANE---------------------------------------------- */
    protected JComponent deletePane(){
        JFrame errorDialog = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        
        String[] websiteNames = DatabaseUtil.getWebName(PasswordDB, user); //this gets updated everytime database is updated
        JComboBox<String> websiteNamesBoxDelete = new JComboBox<>(websiteNames);
        
        websiteNamesBoxDelete.setBounds(250, 50, 140, 20);
        panel.add(websiteNamesBoxDelete);
        
        JLabel choose = new JLabel("Choose a Website: ");
        choose.setBounds(100,50,120,20);
        panel.add(choose);
        
        JLabel errorMessage = new JLabel("");
        errorMessage.setBounds(100,200,165,25);
        panel.add(errorMessage);
        
        JButton confirmDelete = new JButton("Remove");
        confirmDelete.setBounds(470,50,100,50);
        confirmDelete.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedWebsite = websiteNamesBoxDelete.getItemAt(websiteNamesBoxDelete.getSelectedIndex());
                errorMessage.setText("");
                String passwordCheck = JOptionPane.showInputDialog("Type in your password to delete this record:");
                if (DatabaseUtil.checkPassword(UserDB, passwordCheck, user)){
                    DatabaseUtil.deleteButton(PasswordDB, user, selectedWebsite);
                    String[] websiteNames = DatabaseUtil.getWebName(PasswordDB, user);
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(websiteNames);
                    websiteNamesBoxDelete.setModel(model);
                }else{
                    JOptionPane.showMessageDialog(errorDialog, "Wrong password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(confirmDelete);

        return panel;
    }

    /* -------------------------------------------CHECK PANE---------------------------------------------- */
    protected JComponent checkPane(){
        JFrame dialog = new JFrame();
        
        JTextPane textPane = new JTextPane();
        textPane.setVisible(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JTextField pwLengthField = new JTextField(100);
        pwLengthField.setBounds(190,200,165,25);
        pwLengthField.setVisible(false);
        panel.add(pwLengthField);

        JLabel pwlengthLabel = new JLabel("Password Length");
        pwlengthLabel.setBounds(70,200,165,25);
        pwlengthLabel.setVisible(false);
        panel.add(pwlengthLabel);
        
        JButton queryWeakPassword = new JButton("Query Password");
        queryWeakPassword.setBounds(200,300,150,50);
        queryWeakPassword.setVisible(false);
        panel.add(queryWeakPassword);
        
        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setBounds(400,300,150,50);
        changePasswordButton.setVisible(false);
        panel.add(changePasswordButton);

        JLabel errorMessage = new JLabel("");
        errorMessage.setBounds(70,250,300,25);
        errorMessage.setForeground(Color.RED);
        errorMessage.setVisible(false);
        panel.add(errorMessage);
        
        JButton confirmChange = new JButton("Confirm");
        confirmChange.setBounds(300,400,150,50);
        confirmChange.setVisible(false);
        panel.add(confirmChange);
        
        JComboBox<String> weakpasswordsBox = new JComboBox<>();
        weakpasswordsBox.setBounds(190, 150, 140, 20);
        weakpasswordsBox.setVisible(false);
        panel.add(weakpasswordsBox);
        
        JLabel select = new JLabel("Select Website: ");
        select.setBounds(70,150,165,25);
        select.setVisible(false);
        panel.add(select);

        JButton checkButton = new JButton("Check Passwords");
        checkButton.setBounds(300,50,150,50);
        checkButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    HashMap<String, String> password_dict = new HashMap<>();
                    HashMap<String, String> WEAKpassword_dict = new HashMap<>();
                    
                    String cipherKey = DatabaseUtil.getCipherKey(UserDB, user);
                    password_dict = DatabaseUtil.checkPWPMap(PasswordDB, user, cipherKey);
                    // check first if password database is empty or not
                    System.out.println(Arrays.asList(password_dict));
                    if(!password_dict.isEmpty()){
                        ArrayList<String> weak_password_list = new ArrayList<String>();
                        // https://www.programiz.com/java-programming/examples/iterate-over-hashmap
                        for(String key: password_dict.keySet()) {
                            if (!PasswordGenUtils.isValidPassword(password_dict.get(key))){
                                WEAKpassword_dict.put(key, password_dict.get(key));
                            }
                        }
                        // check if there are any weak passwords
                        if (!WEAKpassword_dict.isEmpty()){
                            for(String key: WEAKpassword_dict.keySet()){
                                weak_password_list.add(key);
                                System.out.println(key + " is not secure");
                            }
                            String[] websiteArray = new String[weak_password_list.size()];
                            websiteArray = weak_password_list.toArray(websiteArray);
                            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(websiteArray);
                            weakpasswordsBox.setModel(model);
                            
                            weakpasswordsBox.setVisible(true);
                            queryWeakPassword.setVisible(true);
                            changePasswordButton.setVisible(true);
                            select.setVisible(true);
                            pwLengthField.setVisible(true);
                            pwlengthLabel.setVisible(true);
                            checkButton.setVisible(false);
                        }else{
                            weakpasswordsBox.setVisible(false);
                            queryWeakPassword.setVisible(false);
                            changePasswordButton.setVisible(false);
                            select.setVisible(false);
                            pwLengthField.setVisible(false);
                            pwlengthLabel.setVisible(false);
                            JOptionPane.showMessageDialog(dialog,"No Weak Password");
                        }
                    }else{
                        JOptionPane.showMessageDialog(dialog,"No Password in Database");
                    }
                } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException
                        | InvalidAlgorithmParameterException | NoSuchAlgorithmException | NoSuchPaddingException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panel.add(checkButton);

        // lets the user see their weak password lol
        queryWeakPassword.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedWebsite = weakpasswordsBox.getItemAt(weakpasswordsBox.getSelectedIndex());
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

        // lets the user change their password with the desired length with the password generator function
        changePasswordButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                textPane.setText("");
                errorMessage.setText("");
                try {
                    String selectedWebsite = weakpasswordsBox.getItemAt(weakpasswordsBox.getSelectedIndex());
                    boolean cond = true;
                    while (cond){
                        int password_length = Integer.parseInt(pwLengthField.getText());
                        password = PasswordGenUtils.generatePassword(password_length);
                        if (PasswordGenUtils.isValidPassword(password)){
                            cond = false;
                            break;
                        }
                    }

                    textPane.setEditable(false);
                    textPane.setVisible(true);
                    StyledDocument doc = textPane.getStyledDocument();
                    try{
                        doc.insertString(doc.getLength(), "Website: " + selectedWebsite + "\n", null );
                        doc.insertString(doc.getLength(), "New Password: " + password + "\n", null );
                    }
                    catch (Exception E){
                        System.out.println(E);
                    }
                    textPane.setBounds(400,150,300,80);
                    panel.add(textPane);
                    confirmChange.setVisible(true);
                }catch (NumberFormatException E){
                    textPane.setVisible(false);
                    errorMessage.setVisible(true);
                    errorMessage.setText("Put in a number");
                }
            }
        });
        
        confirmChange.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String selectedWebsite = weakpasswordsBox.getItemAt(weakpasswordsBox.getSelectedIndex());
                    String passwordCheck = JOptionPane.showInputDialog("Type in your password to change this password:");
                    if (DatabaseUtil.checkPassword(UserDB, passwordCheck, user)){
                        String key = DatabaseUtil.getCipherKey(UserDB, user);
                        Cipher encryption = AesUtil.encryptCipher(key);
                        String cipherPW = AesUtil.encrypt(encryption, password);
                        String IV = AesUtil.getIV(encryption);
            
                        DatabaseUtil.ChangePasswordCheck(PasswordDB, user, selectedWebsite, cipherPW, IV); // insert to database
            
                        System.out.println("Password is " + password); // password that should be added to database
            
                        weakpasswordsBox.setVisible(false);
                        queryWeakPassword.setVisible(false);
                        changePasswordButton.setVisible(false);
                        pwLengthField.setVisible(false);
                        pwlengthLabel.setVisible(false);
                        select.setVisible(false);
                        confirmChange.setVisible(false);
                        pwLengthField.setText("");
                        errorMessage.setText("");
                        textPane.setVisible(false);
                        checkButton.setVisible(true);
                    }else{
                        JOptionPane.showMessageDialog(dialog, "Wrong password", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }catch (NoSuchAlgorithmException | NoSuchPaddingException| InvalidKeyException  | IllegalBlockSizeException | BadPaddingException ex){
                    throw new Error("Wrong");
                }
            }
        });

        back.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                errorMessage.setText("");
                weakpasswordsBox.setVisible(false);
                queryWeakPassword.setVisible(false);
                changePasswordButton.setVisible(false);
                pwLengthField.setVisible(false);
                pwlengthLabel.setVisible(false);
                select.setVisible(false);
                confirmChange.setVisible(false);
                textPane.setVisible(false);
                checkButton.setVisible(true);
                pwLengthField.setText("");
                errorMessage.setText("");
            }
        });

        return panel;
    }
}
