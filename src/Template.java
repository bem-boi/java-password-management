import javax.swing.*;

public abstract class Template {
    protected JFrame frame;
    protected JPanel panel;
    protected int width;
    protected int height;

    public abstract void show();

    public Template(int w, int h){
        frame = new JFrame();
        panel = new JPanel();
        width = w;
        height = h;

        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width,height);
    }

}
