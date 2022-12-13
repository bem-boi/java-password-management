import Util.DatabaseUtil;

import javax.swing.*;

import java.awt.event.*;
import java.io.IOException;

import java.sql.*;

public class ExportImport extends Page{
    
    public ExportImport(int w, int h, String user){
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
        
        JComponent panel1 = exportPane();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Export", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = importPane();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Import", panel2);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        panel.add(tabbedPane);
        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public JComponent exportPane(){
        JFrame confirmDiaglog = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(null);

        
        JButton confirmExport = new JButton("Export");
        confirmExport.setBounds(470,50,100,50);
        confirmExport.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                String passwordCheck = JOptionPane.showInputDialog("Type in your password to export all passwords:");
                if (DatabaseUtil.checkPassword(UserDB, passwordCheck, user)){
                    try {
                        DatabaseUtil.exportPassword(PasswordDB, user);
                        System.out.println("export");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    JOptionPane.showMessageDialog(confirmDiaglog, "Wrong password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        panel.add(confirmExport);
        return panel;
    }

    public JComponent importPane(){
        JFrame dialog = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JTextField fileName = new JTextField(100);
        fileName.setBounds(150,100,225,25);
        panel.add(fileName);

        JLabel fileLabel = new JLabel("File Name");
        fileLabel.setBounds(70,100,165,25);
        panel.add(fileLabel);

        JButton importButton = new JButton("Import");
        importButton.setBounds(450,100,100,50);
        importButton.setFocusPainted(false);
        importButton.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                String passwordCheck = JOptionPane.showInputDialog("Type in your password to import passwords:");
                if (DatabaseUtil.checkPassword(UserDB, passwordCheck, user)){
                    String filename = fileName.getText().strip();
                    try {
                        DatabaseUtil.importPassword(PasswordDB, user, filename);
                        System.out.println("imported");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }else{
                    JOptionPane.showMessageDialog(dialog, "Wrong password", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            }

        });
        panel.add(importButton);
        
        return panel;
    }
}
