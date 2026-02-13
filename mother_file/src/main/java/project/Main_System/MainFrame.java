package project.Main_System;


import java.awt.Font;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import project.Admin_Screen.Bookmanagement.BookManagement;


public class MainFrame extends javax.swing.JFrame{
    
    public MainFrame(){
        setTitle("Library System");
        setContentPane(new BookManagement(this));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);


    }

    private static Font SANCHEZ_FONT;
 
    public static Font loadSanchez(float size) {
        if (SANCHEZ_FONT == null) {
            try {
                SANCHEZ_FONT = Font.createFont(
                    Font.TRUETYPE_FONT,
                    MainFrame.class.getResourceAsStream("/Fonts/Poppins/Sanchez-Regular.ttf")
                );
    
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .registerFont(SANCHEZ_FONT);
    
            } catch (Exception e) {
                System.err.println("Failed to load Sanchez font");
                return new Font("Serif", Font.PLAIN, (int) size);
            }
        }
        return SANCHEZ_FONT.deriveFont(size);
    }

}