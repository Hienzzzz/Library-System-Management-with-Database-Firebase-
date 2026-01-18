package Main_System;

import Login_screen.*;
import javax.swing.*;

public class MainFrame extends javax.swing.JFrame{
    
    public MainFrame(){
        setTitle("Library System");
        setContentPane(new Register(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

    }
}