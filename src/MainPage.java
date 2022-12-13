import javax.swing.JButton;

public class MainPage extends Page{
    public MainPage(int w, int h, String user){
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
        pwGen.setFocusPainted(false);
        pwGen.addActionListener(new PushInActionListener(this, new PasswordGenerator(800,400, user), frame));
        panel.add(pwGen);

        JButton pwManage = new JButton("PASSWORD MANAGER");
        pwManage.setBounds(420, 200, 200,100);
        pwManage.setFocusPainted(false);
        pwManage.addActionListener(new PushInActionListener(this, new PasswordManager(800, 600, user), frame));
        panel.add(pwManage);

        JButton pwExportImport = new JButton("EXPORT/IMPORT");
        pwExportImport.setBounds(420, 400, 200,100);
        pwExportImport.setFocusPainted(false);
        pwExportImport.addActionListener(new PushInActionListener(this, new ExportImport(800, 600, user), frame));
        panel.add(pwExportImport);


        panel.revalidate();
        panel.repaint();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
