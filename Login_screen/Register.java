package Login_screen;

import Main_System.MainFrame;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.*;

public class Register extends javax.swing.JPanel{

    private MainFrame frame;

    public Register(MainFrame frame){
        this.frame = frame;

        panel();
    }

    public void panel(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));

        JLabel background = new JLabel();
        background.setBounds(0,0,1512,982);
        background.setIcon(new ImageIcon("Images\\LoginScreen_register.png"));
        background.setLayout(null);

        //Firstname===========================================================================================

        String firstname_placeHolder = "Enter your first name";

        JTextField firstname = new JTextField(firstname_placeHolder);
        firstname.setBounds(200, 284, 370, 50);
        firstname.setBackground(new Color(245, 246, 250));
        firstname.setBorder(null);
        firstname.setFont(new Font("Sanchez", Font.PLAIN, 20));
        firstname.setForeground(Color.gray);

        firstname.addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained(FocusEvent e){
                if(firstname.getText().equals(firstname_placeHolder)){
                    firstname.setText("");
                    firstname.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e){
                if(firstname.getText().isEmpty()){
                    firstname.setText(firstname_placeHolder);
                    firstname.setForeground(Color.GRAY);
                }
            }
        });
        //lastname=============================================================================================                     
        String lastname_placeHolder = "Enter your last name";

        JTextField lastname = new JTextField(lastname_placeHolder);
        lastname.setBounds(200, 355, 370, 50);
        lastname.setBackground(new Color(245, 246, 250));
        lastname.setBorder(null);
        lastname.setFont(new Font("Sanchez", Font.PLAIN, 20));
        lastname.setForeground(Color.gray);

        lastname.addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained(FocusEvent e){
                if(lastname.getText().equals(lastname_placeHolder)){
                    lastname.setText("");
                    lastname.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e){
                if(lastname.getText().isEmpty()){
                    lastname.setText(lastname_placeHolder);
                    lastname.setForeground(Color.GRAY);
                }
            }
        });

        //Student ID No.===============================================================================================
        String ID_placeHolder = "Enter your last name";

        JTextField Student_ID = new JTextField(ID_placeHolder);
        Student_ID.setBounds(200, 426, 370, 50);
        Student_ID.setBackground(new Color(245, 246, 250));
        Student_ID.setBorder(null);
        Student_ID.setFont(new Font("Sanchez", Font.PLAIN, 20));
        Student_ID.setForeground(Color.gray);

        Student_ID.addFocusListener(new FocusAdapter(){
            @Override
            public void focusGained(FocusEvent e){
                if(Student_ID.getText().equals(ID_placeHolder)){
                    Student_ID.setText("");
                    Student_ID.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e){
                if(Student_ID.getText().isEmpty()){
                    Student_ID.setText(ID_placeHolder );
                    Student_ID.setForeground(Color.GRAY);
                }
            }
        });

        background.add(Student_ID);
        background.add(lastname);
        background.add(firstname);
        this.add(background);
    }
    
}
