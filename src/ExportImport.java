import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.SQLException;

public class ExportImport extends Page{

    public ExportImport(int w, int h, String user) {
        super(w, h);
        this.frame = super.frame;
        this.panel = super.panel;
        this.back = super.back;
    }

    @Override
    public void show() throws SQLException {
        try{
            PrintWriter pw = new PrintWriter(new File("C:\\Users\\Beam\\Desktop\\export.csv")); 
            StringBuilder sb = new StringBuilder();
            
            
        }catch (FileNotFoundException e){
            System.out.println("Wrong");
        }
    }
    
}
