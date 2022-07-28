import javax.swing.*;
import java.awt.event.*;

// https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TabbedPaneDemoProject/src/components/TabbedPaneDemo.java

public class PasswordManager extends Page{
    public PasswordManager(int w, int h){
        super(w,h);
        this.frame = super.frame;
        this.panel = super.panel;
        this.back = super.back;
    }   

    public void show(){
        frame.setTitle("Password Manager");
        panel.setLayout(null);

        back.addActionListener(new PopOutActionListener(frame));


        JTabbedPane tabbedPane = new JTabbedPane();
        JComponent panel1 = makePanel1();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Tab1", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = makePanel1();
        tabbedPane.setBounds(0,1,785,535);
        tabbedPane.addTab("Tab1", panel2);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_2);

        panel.add(tabbedPane);
        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    protected JComponent makePanel1(){ //use group layout here https://stackoverflow.com/questions/20299927/how-do-i-use-grouplayout-properly-to-move-components-panels
        JPanel panel = new JPanel();
        JLabel tabs = new JLabel("Tab 1");
        tabs.setBounds(0,0,20,40);
        JButton click = new JButton("Clik me");
        click.setBounds(3,5,40,40);
        panel.add(click);
        panel.add(tabs);
        return panel;
    }
}
