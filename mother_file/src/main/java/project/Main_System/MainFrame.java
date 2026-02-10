package project.Main_System;


import javax.swing.JFrame;

import project.Admin_Screen.Studentmanagement.StudentAccountPanel;


public class MainFrame extends javax.swing.JFrame{
    
    public MainFrame(){
        setTitle("Library System");
        setContentPane(new StudentAccountPanel(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);



    }
}