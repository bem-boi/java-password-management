import javax.swing.*;
import java.awt.event.*;

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
        back = new JButton("BACK");

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
                // TODO Auto-generated method stub
                Login loginPage = new Login(350,200);
                loginPage.show();
                frame.dispose();
            }

        });

        exit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                System.exit(0);
            }

        });

        frame.setJMenuBar(mb);
    }

    public abstract void show();
}
