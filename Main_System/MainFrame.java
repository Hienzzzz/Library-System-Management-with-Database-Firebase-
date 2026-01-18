package Main_System;

import java.awt.*;
import javax.swing.*;
import Admin_Screen.Dashboard;

public class MainFrame extends javax.swing.JFrame{
    public MainFrame(){
        setContentPane(new Dashboard(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pack();
        setLocationRelative(null);
        setResizable(false);
        setVisible(true);

    }
}