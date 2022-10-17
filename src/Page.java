import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.awt.Image;

public abstract class Page extends Template{
    private JMenuBar mb;
    private JMenu help, file, edit;
    private JMenuItem exit, about, signOut;
    protected JButton back;

    public Page(int w, int h){
        super(w,h);
        this.frame = super.frame;
        this.panel = super.panel;

        mb = new JMenuBar();
        help = new JMenu("Help");
        file = new JMenu("File");
        edit = new JMenu("Edit");
        exit = new JMenuItem("Exit");
        about = new JMenuItem("About");
        signOut = new JMenuItem("Sign out");
        
        Icon icon = new ImageIcon(new ImageIcon("src\\Images\\back-arrow.PNG").getImage().getScaledInstance(17, 17, Image.SCALE_SMOOTH));
        back = new JButton(icon);
        back.setOpaque(false);
        back.setContentAreaFilled(false);
        back.setBorderPainted(false);
        back.setFocusPainted(false);

        mb.add(back);
        mb.add(file);
        mb.add(edit);
        mb.add(help);

        file.add(exit);
        help.add(about);
        help.add(signOut);

        signOut.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent e) {
                Login loginPage = new Login(350,200);
                loginPage.show();
                frame.dispose();
            }

        });

        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

        });

        frame.setJMenuBar(mb);
    }

    public JMenuBar getMb() {
        return mb;
    }

    public JMenu getEdit() {
        return edit;
    }

    public JMenu getFile() {
        return file;
    }

    public JMenu getHelp() {
        return help;
    }

    public JMenuItem getExit() {
        return exit;
    }

    public JMenuItem getAbout() {
        return about;
    }

    public JMenuItem getSignOut() {
        return signOut;
    }

    public JButton getBack() {
        return back;
    }
    
    public abstract void show() throws SQLException;
}
