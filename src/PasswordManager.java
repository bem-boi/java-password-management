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
    protected JComponent queryPane(){ //use group layout here https://stackoverflow.com/questions/20299927/how-do-i-use-grouplayout-properly-to-move-components-panels
        JPanel panel = new JPanel();
        JLabel tabs = new JLabel("Query");
        tabs.setBounds(0,0,20,40);
        JButton click = new JButton("Clik me");
        click.setBounds(3,5,40,40);
        panel.add(click);
        panel.add(tabs);
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
