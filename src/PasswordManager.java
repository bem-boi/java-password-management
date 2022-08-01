import Util.AesUtil;
import Util.DatabaseUtil;

import javax.crypto.*;
import javax.swing.*;
import java.awt.event.*;
import java.security.*;
import java.sql.*;
import java.util.Arrays;

public class PasswordManager extends Page{
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
        JComboBox<String> websiteNamesBox = new JComboBox<>(websiteNames);
        
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
        JPanel panel = new JPanel();
        JLabel tabs = new JLabel("Add");
        tabs.setBounds(0,0,20,40);
        JButton click = new JButton("Clik me");
        click.setBounds(3,5,40,40);
        panel.add(click);
        panel.add(tabs);
        return panel;
    }

    // edit pane
    protected JComponent editPane(){
        JPanel panel = new JPanel();
        JLabel tabs = new JLabel("Edit");
        tabs.setBounds(0,0,20,40);
        JButton click = new JButton("Clik me");
        click.setBounds(3,5,40,40);
        panel.add(click);
        panel.add(tabs);
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
