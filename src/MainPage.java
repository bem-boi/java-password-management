import javax.swing.JButton;

public class MainPage extends Page{
    public MainPage(int w, int h){
        super(w, h);
        this.frame = super.frame;
        this.panel = super.panel;
        this.back = super.back;
    }

    public void show(){
        frame.setTitle("Main Page");
        panel.setLayout(null);

        back.addActionListener(new PopOutActionListener(frame));

        JButton pwGen = new JButton("PASSWORD GENERATOR");
        pwGen.setBounds(170,200,200,100);
        pwGen.addActionListener(new PageActionListener(this, new PasswordGenerator(800,600), frame));
        panel.add(pwGen);

        JButton pwManage = new JButton("PASSWORD MANAGER");
        pwManage.setBounds(420, 200, 200,100);
        pwManage.addActionListener(new PageActionListener(this, new PasswordManager(800, 600), frame));
        panel.add(pwManage);


        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
