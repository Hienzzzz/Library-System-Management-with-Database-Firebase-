package Student_Screen.Student_dashboard;

import Main_system.MainFrame;
import java.awt.Dimension;
import javax.swing.*;


public class Student_dashboard extends javax.swing.JPanel {
    private MainFrame frame;

    public Student_dashboard(MainFrame frame){
        this.frame = frame;
        panel();
    }
    public void panel(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));
        
        JLabel background = new JLabel();
        background.setBounds(0,0,1512,982);
        background.setIcon(new ImageIcon("Images\\Studnet_dashbaord.png"));
        background.setLayout(null);

        this.add(background);
    }
    
}
