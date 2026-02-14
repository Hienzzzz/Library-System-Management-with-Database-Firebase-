package project.Login_screen;
 

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.json.JSONObject;

import project.Admin_Screen.Dashboard.AdminDashboard;
import project.Firebase_backend.User_backend.FetchUserCallback;
import project.Firebase_backend.User_backend.FirebaseAuthService;
import project.Firebase_backend.User_backend.User;
import project.Firebase_backend.User_backend.UserService;
import project.Librarian_screen.Dashboard.Librarian_dashboard;
import project.Main_System.MainFrame;
import project.Student_Screen.Student_dashboard.Student_Dashboard;
 
public class Login extends javax.swing.JPanel{
 
    private MainFrame frame;
    public Login(MainFrame frame){
        this.frame = frame;
        panel();
 
        
    }
    
 
    public void panel(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));
 
       

        JLabel background = new JLabel();
        background.setBounds(0,0,1512,982);
        background.setLayout(null);

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Login_screen.png")
        );
        
        //username========================================================================
        String nameText_placaeHolder = "Enter your Email / Username";
 
        JTextField username = new JTextField(nameText_placaeHolder);
        username.setBounds(195, 398, 373, 62);
        username.setBackground(new Color(255, 255, 255));
        username.setBorder(null);
        username.setFont(new Font("Sanchez", Font.PLAIN, 20));
        username.setForeground(Color.GRAY);
 
        username.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained (FocusEvent e){
                
                if (username.getText().equals(nameText_placaeHolder)) {
                    username.setText("");
                    username.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e){
                if(username.getText().isEmpty()){
                    username.setText(nameText_placaeHolder);
                    username.setForeground(Color.GRAY);
                }
            }
        });
 
        //password========================================================================
        
        String passText_Placeholder ="Enter your password";
 
        JPasswordField password = new JPasswordField(passText_Placeholder);
        password.setBounds(195, 480, 373, 62);
        password.setBorder(null);
        password.setFont(new Font("Sanchez", Font.PLAIN, 20));
        password.setForeground(Color.GRAY);
        password.setEchoChar((char) 0 );
 
        ImageIcon openEye = new ImageIcon(
            new ImageIcon(
                    getClass().getResource("/Images/Login_show (2).png")
                ).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)
        );

 
        ImageIcon closedEye = new ImageIcon(
            new ImageIcon(
                    getClass().getResource("/Images/Login_hide.png")
                ).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)
        );
 
        JCheckBox showPassword = new JCheckBox();
        showPassword.setBounds(522, 480, 50,60 );
        showPassword.setBorder(null);
        showPassword.setContentAreaFilled(false);
        showPassword.setOpaque(false);
        showPassword.setFocusPainted(false);
        showPassword.setIcon(closedEye);
        showPassword.setSelectedIcon(openEye);
 
        showPassword.addActionListener(e ->{
            String text = new String(password.getPassword());
            if(!text.equals(passText_Placeholder)){
                if(showPassword.isSelected()){
                    password.setEchoChar((char) 0);
                }else{
                    password.setEchoChar('•');
                }
            }
        } );
 
        password.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e){
                String currentText = new String (password.getPassword());
 
                if(currentText.equals(passText_Placeholder)){
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
                    password.setText(passText_Placeholder);
                    password.setForeground(Color.GRAY);
                    password.setEchoChar((char) 0);
                }
            }
        });
 
        JButton clearButton = new JButton();
        clearButton.setBounds(136, 612, 199, 60);
        clearButton.setOpaque(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setBorderPainted(false);
        clearButton.setFocusPainted(false);
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                username.setText(nameText_placaeHolder);
                password.setText(passText_Placeholder);
                password.setEchoChar((char) 0);
                username.setForeground(Color.GRAY);
                password.setForeground(Color.GRAY);
            }
            
        });
 
        JButton loginButton = new JButton();
        loginButton.setBounds(376,616,199,60);
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
 
        loginButton.addActionListener(new ActionListener() {
           @Override
public void actionPerformed(ActionEvent e){

    Color errorRed = new Color(220, 80, 80);
    Color normalColor = Color.BLACK;

    boolean username_empty =
            username.getText().trim().isEmpty() ||
            username.getText().equals(nameText_placaeHolder);

    boolean password_empty =
            password.getPassword().length == 0 ||
            new String(password.getPassword()).equals(passText_Placeholder);

    if(username_empty && password_empty){
        username.setForeground(errorRed);
        password.setForeground(errorRed);

        JOptionPane.showMessageDialog(
                frame,
                "Please fill out all required fields",
                "Required Fields",
                JOptionPane.ERROR_MESSAGE);
        return;
    }

    if(username_empty){
        username.setForeground(errorRed);
        JOptionPane.showMessageDialog(
                frame,
                "Please enter your username",
                "Required Field",
                JOptionPane.ERROR_MESSAGE);
        return;
    } else {
        username.setForeground(normalColor);
    }

    if(password_empty){
        password.setForeground(errorRed);
        JOptionPane.showMessageDialog(
                frame,
                "Please enter your password",
                "Required Field",
                JOptionPane.ERROR_MESSAGE);
        return;
    } else {
        password.setForeground(normalColor);
    }

    String loginInput = username.getText().trim();
    String rawPassword = new String(password.getPassword());

    try {

        JSONObject loginResult =
                FirebaseAuthService.login(loginInput, rawPassword);

        if (loginResult.has("error")) {

            String errorMsg =
                    loginResult.getJSONObject("error")
                               .getString("message");

            JOptionPane.showMessageDialog(
                    frame,
                    "Login Failed:\n" + errorMsg,
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        String idToken = loginResult.getString("idToken");

        // 🔐 Check email verification
        boolean verified =
                FirebaseAuthService.isEmailVerified(idToken);

        if (!verified) {

            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Your email is not verified.\n\n" +
                    "Would you like to resend the verification email?",
                    "Email Not Verified",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                FirebaseAuthService.sendVerification(idToken);

                JOptionPane.showMessageDialog(
                        frame,
                        "Verification email has been resent.\n" +
                        "Please check your Gmail.",
                        "Email Sent",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }

            return; // 🔴 VERY IMPORTANT: STOP LOGIN
        }

        // 💾 Fetch user profile
        UserService.fetchUserByEmail(loginInput, user -> {

            javax.swing.SwingUtilities.invokeLater(() -> {

                if (user == null) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "User profile not found.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                switch (user.getRole()) {
                    case "ADMIN":
                        frame.setContentPane(new AdminDashboard(frame));
                        break;
                    case "STUDENT":
                        frame.setContentPane(new Student_Dashboard(frame));
                        break;
                    case "LIBRARIAN":
                        frame.setContentPane(new Librarian_dashboard(frame));
                        break;
                }

                frame.revalidate();
                frame.repaint();
            });
        });

    } catch (Exception ex) {

        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                frame,
                "Something went wrong.\nPlease try again.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

        });

        //=================forgot password===============================

        JButton forgotPassword = new JButton("Forgot Password?");
        forgotPassword.setBounds(195, 560, 200, 30); // adjust if needed
        forgotPassword.setBorderPainted(false);
        forgotPassword.setContentAreaFilled(false);
        forgotPassword.setFocusPainted(false);

        forgotPassword.addActionListener(e -> {

            String emailInput = JOptionPane.showInputDialog(
                    frame,
                    "Enter your registered email:"
            );

            if (emailInput != null && !emailInput.trim().isEmpty()) {

                try {
                    FirebaseAuthService.sendPasswordReset(emailInput.trim());

                    JOptionPane.showMessageDialog(
                            frame,
                            "Password reset email sent.\nCheck your Gmail.",
                            "Reset Sent",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });


        //register button=================================================================
        JButton register = new JButton();
        register.setBounds(279,759, 165, 30);
        register.setOpaque(false);
        register.setContentAreaFilled(false);
        register.setBorderPainted(false);
 
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("Switching to register page");
                frame.setContentPane(new Register(frame));
                frame.revalidate();
 
            }
        });


        background.add(forgotPassword);
        background.add(showPassword);
        background.add(loginButton);
        background.add(clearButton);
        background.add(password);
        background.add(register);
        background.add(username);
        background.setIcon(icon);
        this.add(background);
    
       
    
 
 
    }

    private void loginWithFirebase(String email, String password) {

        try {

            JSONObject loginResult =
                    FirebaseAuthService.login(email, password);

            if (loginResult.has("error")) {

                String errorMsg =
                        loginResult.getJSONObject("error")
                                .getString("message");

                JOptionPane.showMessageDialog(
                        frame,
                        "Login Failed:\n" + errorMsg,
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            String idToken = loginResult.getString("idToken");

            boolean verified =
                    FirebaseAuthService.isEmailVerified(idToken);

            if (!verified) {
                JOptionPane.showMessageDialog(
                        frame,
                        "Please verify your email before logging in.",
                        "Email Not Verified",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            UserService.fetchUserByEmail(email, user -> {

                javax.swing.SwingUtilities.invokeLater(() -> {

                    if (user == null) {
                        JOptionPane.showMessageDialog(
                                frame,
                                "User profile not found.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                     // 🔴 ADD THIS BLOCK HERE
                    if ("DELETED".equalsIgnoreCase(user.getStatus())) {
                        JOptionPane.showMessageDialog(
                                frame,
                                "This account has been removed by the administrator.",
                                "Access Denied",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }
                    // 🔴 END BLOCK

                    switch (user.getRole()) {
                        case "ADMIN":
                            frame.setContentPane(new AdminDashboard(frame));
                            break;
                        case "STUDENT":
                            frame.setContentPane(new Student_Dashboard(frame));
                            break;
                        case "LIBRARIAN":
                            frame.setContentPane(new Librarian_dashboard(frame));
                            break;
                    }

                    frame.revalidate();
                    frame.repaint();
                });


            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}