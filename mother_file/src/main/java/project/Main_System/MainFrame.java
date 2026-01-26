package project.Main_System;


import javax.swing.JFrame;

import project.Login_screen.Login;

public class MainFrame extends javax.swing.JFrame{
    
    public MainFrame(){
        setTitle("Library System");
        setContentPane(new Login(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);

    }
}