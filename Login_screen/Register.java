package Login_screen;

import Main_System.MainFrame;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        background.setIcon(new ImageIcon("Images\\Login_REGISTER PAGE.png"));
        background.setLayout(null);

        JLabel gif = new JLabel();
        gif.setIcon(new ImageIcon("Images\\\\Login_registration.gif"));
        gif.setBounds(865, 138, 624, 624);


        //Firstname===========================================================================================

        String firstname_placeHolder = "Enter Firstname";

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
        String lastname_placeHolder = "Enter Lastname";

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
        String Email_palceHolder = "Enter Email Address";

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
        String ID_placeHolder = "Enter Student ID No.";

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

        // clear button ============================================================================================
        JButton clearButton = new JButton();
        clearButton.setBounds(181, 733, 192,62);
        clearButton.setContentAreaFilled(false);
        clearButton.setBorder(null);
        clearButton.setFocusPainted(false);
        clearButton.setOpaque(false);

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                firstname.setText(firstname_placeHolder);
                firstname.setForeground(Color.GRAY);
                lastname.setText(lastname_placeHolder);
                lastname.setForeground(Color.GRAY);
                email.setText(Email_palceHolder);
                email.setForeground(Color.GRAY);
                Student_ID.setText(ID_placeHolder);
                Student_ID.setForeground(Color.GRAY);
                password.setText(password_placeHolder);
                password.setEchoChar((char) 0);
                password.setForeground(Color.GRAY);
                verify_password.setText((verify_placeHolder));
                verify_password.setEchoChar((char) 0);
                verify_password.setForeground(Color.GRAY);

            }
        });

        // Create Buttons

        JButton createButton = new JButton();
        createButton.setBounds(398, 733, 193,62);
        createButton.setContentAreaFilled(false);
        createButton.setBorder(null);
        createButton.setOpaque(false);
        createButton.setFocusPainted(false);

        createButton.addActionListener(e -> {

            Color errorRed = new Color(220, 80, 80);

            boolean firstEmpty =
                firstname.getText().trim().isEmpty() ||
                firstname.getText().equals(firstname_placeHolder);

            boolean lastEmpty =
                lastname.getText().trim().isEmpty() ||
                lastname.getText().equals(lastname_placeHolder);

            boolean emailEmpty =
                email.getText().trim().isEmpty() ||
                email.getText().equals(Email_palceHolder);

            boolean idEmpty =
                Student_ID.getText().trim().isEmpty() ||
                Student_ID.getText().equals(ID_placeHolder);

            boolean passwordEmpty =
                password.getPassword().length == 0 ||
                new String(password.getPassword()).equals(password_placeHolder);

            boolean verifyEmpty =
                verify_password.getPassword().length == 0 ||
                new String(verify_password.getPassword()).equals(verify_placeHolder);

 
            if (firstEmpty && lastEmpty && emailEmpty && idEmpty && passwordEmpty && verifyEmpty) {

                firstname.setForeground(errorRed);
                lastname.setForeground(errorRed);
                email.setForeground(errorRed);
                Student_ID.setForeground(errorRed);
                password.setForeground(errorRed);
                verify_password.setForeground(errorRed);

                JOptionPane.showMessageDialog(
                    frame,
                    "Please fill out all required fields.",
                    "Required Fields",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }


            if (firstEmpty) {
                firstname.setForeground(errorRed);
                JOptionPane.showMessageDialog(frame,
                    "Please enter your first name",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
                    return;
            }

            if (lastEmpty) {
                lastname.setForeground(errorRed);
                    JOptionPane.showMessageDialog(frame,
                    "Please enter your last name",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
                    return;
            }

            if (emailEmpty) {
                email.setForeground(errorRed);
                JOptionPane.showMessageDialog(frame,
                    "Please enter your email address",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
                    return;
            }

            if (idEmpty) {
                Student_ID.setForeground(errorRed);
                JOptionPane.showMessageDialog(frame,
                    "Please enter your student ID number",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (passwordEmpty) {
                password.setForeground(errorRed);
                JOptionPane.showMessageDialog(frame,
                    "Please create a password",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (verifyEmpty) {
                verify_password.setForeground(errorRed);
                    JOptionPane.showMessageDialog(frame,
                    "Please verify your password",
                    "Required Field",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }



                JOptionPane.showMessageDialog(
                    frame,
                    "Registration successful!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
        });



        background.add(gif);
        background.add(createButton);
        background.add(clearButton);
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
