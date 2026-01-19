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

        String firstname_placeHolder = "Enter first name";

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
        String lastname_placeHolder = "Enter last name";

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
        // Email ======================================================================================================
        String Email_palceHolder = "Enter Email";

        JTextField email = new JTextField(Email_palceHolder);
        email.setBounds(200, 426, 370, 50);
        email.setBackground(new Color(245, 246, 250));
        email.setBorder(null);
        email.setFont(new Font("Sanchez", Font.PLAIN, 20));
        email.setForeground(Color.GRAY);

        email.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e){
                if(email.getText().equals(Email_palceHolder)){
                    email.setText("");
                    email.setForeground(Color.black);
                }
            }
            @Override
            public void focusLost(FocusEvent e){
                if(email.getText().isEmpty()){
                    email.setText(Email_palceHolder);
                    email.setForeground(Color.GRAY);
                
                }
            }
        });

        //Student ID No.===============================================================================================
        String ID_placeHolder = "Enter last name";

        JTextField Student_ID = new JTextField(ID_placeHolder);
        Student_ID.setBounds(200, 495, 370, 50);
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

        //Password ====================================================================================================
        String password_placeHolder = "Set password";

        JPasswordField password = new JPasswordField(password_placeHolder);
        password.setBounds(200, 566, 330, 50);
        password.setBackground(new Color ( 245, 246, 250));
        password.setBorder(null);
        password.setFont(new Font("Sanchez", Font.PLAIN, 20));
        password.setForeground(Color.gray);
        password.setEchoChar((char) 0);

        ImageIcon show = new ImageIcon( // open eye icon
            new ImageIcon("Images\\eye (2).png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)
        );
        ImageIcon hide = new ImageIcon(
            new ImageIcon("Images\\hidden.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)
        );

        JCheckBox showPassword = new JCheckBox();
        showPassword.setBounds(537, 581, 30 , 20);
        showPassword.setBorder(null);
        showPassword.setContentAreaFilled(false);
        showPassword.setOpaque(false);
        showPassword.setFocusPainted(false);
        showPassword.setSelectedIcon(show);
        showPassword.setIcon(hide);

        showPassword.addActionListener(e ->{
            String text = new String(password.getPassword());
            if(!text.equals(password_placeHolder)){
                if(showPassword.isSelected()){
                    password.setEchoChar((char) 0);
                }else{
                    password.setEchoChar('•');
                }
            }
        });

        password.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e){
                String CurrentText = new String(password.getPassword());

                if(CurrentText.equals(password_placeHolder)){
                    password.setText("");
                    password.setForeground(Color.BLACK);
                    if(!showPassword.isSelected()){
                    password.setEchoChar('•');
                    
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e){
                if(password.getPassword().length == 0){
                    password.setText(password_placeHolder);
                    password.setForeground(Color.gray);
                    password.setEchoChar((char) 0);
                    
                }
            }
        });

        //Verify password============================================================================================

        String verify_placeHolder = "Verify Password";

        JPasswordField verify_password = new JPasswordField(verify_placeHolder);
        verify_password.setBounds(200, 635, 330, 50);
        verify_password.setBackground(new Color ( 245, 246, 250));
        verify_password.setBorder(null);
        verify_password.setFont(new Font("Sanchez", Font.PLAIN, 20));
        verify_password.setForeground(Color.GRAY);
        verify_password.setEchoChar((char) 0);

        JCheckBox show_verifyPassword = new JCheckBox();
        show_verifyPassword.setBounds(537, 654, 30 , 20);
        show_verifyPassword.setBorder(null);
        show_verifyPassword.setContentAreaFilled(false);
        show_verifyPassword.setOpaque(false);
        show_verifyPassword.setFocusPainted(false);
        show_verifyPassword.setIcon(hide);
        show_verifyPassword.setSelectedIcon(show);

        show_verifyPassword.addActionListener(e ->{
            String text = new String (verify_password.getPassword());
            if(!text.equals(verify_placeHolder)){
                if(show_verifyPassword.isSelected()){
                    verify_password.setEchoChar((char) 0);
                }else{
                    verify_password.setEchoChar('•');
                }
            }
        });

        verify_password.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e){
                String currentText_password = new String(verify_password.getPassword());

                if(currentText_password.equals(verify_placeHolder)){
                    verify_password.setText("");
                    verify_password.setForeground(Color.black);
                    if(!show_verifyPassword.isSelected()){
                        verify_password.setEchoChar('•');
                    }
                }
            }
            @Override
            public void focusLost(FocusEvent e){
                if(verify_password.getPassword().length == 0){
                    verify_password.setText(verify_placeHolder);
                    verify_password.setForeground(Color.gray);
                    verify_password.setEchoChar((char) 0);
                }
            }
        });




        background.add(show_verifyPassword);
        background.add(verify_password);
        background.add(showPassword);
        background.add(password);
        background.add(email);
        background.add(Student_ID);
        background.add(lastname);
        background.add(firstname);
        this.add(background);
    }
    
}
