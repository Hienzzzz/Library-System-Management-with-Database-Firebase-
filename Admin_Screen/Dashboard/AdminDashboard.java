package Admin_Screen.Dashboard;


import Main_system.MainFrame;
import java.awt.Dimension;
import javax.swing.*;

public class AdminDashboard extends javax.swing.JPanel {
    private MainFrame frame;

    public AdminDashboard(MainFrame frame){
        this.frame = frame;
        panel();
    }
    public void panel(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));
        
        JLabel background = new JLabel();
        background.setBounds(0,0,1512,982);
        background.setIcon(new ImageIcon(""));
        background.setLayout(null);

        this.add(background);
    }
    
}
