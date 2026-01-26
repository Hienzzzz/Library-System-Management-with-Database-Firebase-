package project.Librarian_screen.Dashboard;


import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import project.Main_System.MainFrame;


public class Librarian_dashboard extends javax.swing.JPanel {
    private MainFrame frame;

    public Librarian_dashboard(MainFrame frame){
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
