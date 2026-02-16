package project.Login_screen;

/* =========================================================
 * ========================== IMPORTS ======================
 * ========================================================= */

// ================= AWT =================
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

// ================= SWING =================
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// ================= JSON =================
import org.json.JSONObject;

// ================= PROJECT SCREENS =================
import project.Admin_Screen.Dashboard.AdminDashboard;
import project.Librarian_screen.Dashboard.Librarian_dashboard;
import project.Student_Screen.Student_dashboard.Student_Dashboard;

// ================= FIREBASE SERVICES =================
import project.Firebase_backend.User_backend.FirebaseAuthService;
import project.Firebase_backend.User_backend.UserService;

// ================= MAIN FRAME =================
import project.Main_System.MainFrame;



/* =========================================================
 * ===================== CLASS DECLARATION =================
 * ========================================================= */

public class Login extends javax.swing.JPanel {



    /* =====================================================
     * ===================== CLASS FIELDS ===================
     * ===================================================== */

    private MainFrame frame;



    /* =====================================================
     * ===================== CONSTRUCTOR ====================
     * ===================================================== */

    public Login(MainFrame frame) {
        this.frame = frame;
        panel();
    }



    /* =====================================================
     * ===================== MAIN PANEL =====================
     * ===================================================== */

    public void panel() {

        /* =====================================================
         * ===================== BASE PANEL =====================
         * ===================================================== */

        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));

        JLabel background = new JLabel();
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Login_screen.png")
        );



        /* =====================================================
         * ===================== USERNAME FIELD ================
         * ===================================================== */

        String nameText_placeholder =
                "Enter your Email / Username";

        JTextField username =
                new JTextField(nameText_placeholder);

        username.setBounds(195, 398, 373, 62);
        username.setBackground(Color.WHITE);
        username.setBorder(null);
        username.setFont(new Font("Sanchez", Font.PLAIN, 20));
        username.setForeground(Color.GRAY);

        username.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent e) {
                if (username.getText()
                        .equals(nameText_placeholder)) {

                    username.setText("");
                    username.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (username.getText().isEmpty()) {
                    username.setText(nameText_placeholder);
                    username.setForeground(Color.GRAY);
                }
            }
        });



        /* =====================================================
         * ===================== PASSWORD FIELD ================
         * ===================================================== */

        String passText_placeholder =
                "Enter your password";

        JPasswordField password =
                new JPasswordField(passText_placeholder);

        password.setBounds(195, 480, 373, 62);
        password.setBorder(null);
        password.setFont(new Font("Sanchez", Font.PLAIN, 20));
        password.setForeground(Color.GRAY);
        password.setEchoChar((char) 0);



        /* =====================================================
         * ===================== SHOW / HIDE PASSWORD ==========
         * ===================================================== */

        ImageIcon openEye = new ImageIcon(
                new ImageIcon(
                        getClass()
                                .getResource("/Images/Login_show (2).png")
                ).getImage()
                        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)
        );

        ImageIcon closedEye = new ImageIcon(
                new ImageIcon(
                        getClass()
                                .getResource("/Images/Login_hide.png")
                ).getImage()
                        .getScaledInstance(30, 30, Image.SCALE_SMOOTH)
        );

        JCheckBox showPassword = new JCheckBox();
        showPassword.setBounds(522, 480, 50, 60);
        showPassword.setContentAreaFilled(false);
        showPassword.setOpaque(false);
        showPassword.setFocusPainted(false);
        showPassword.setIcon(closedEye);
        showPassword.setSelectedIcon(openEye);

        showPassword.addActionListener(e -> {

            String text =
                    new String(password.getPassword());

            if (!text.equals(passText_placeholder)) {

                if (showPassword.isSelected()) {
                    password.setEchoChar((char) 0);
                } else {
                    password.setEchoChar('•');
                }
            }
        });



        password.addFocusListener(new FocusAdapter() {

            public void focusGained(FocusEvent e) {

                String current =
                        new String(password.getPassword());

                if (current.equals(passText_placeholder)) {

                    password.setText("");
                    password.setForeground(Color.BLACK);

                    if (!showPassword.isSelected()) {
                        password.setEchoChar('•');
                    }
                }
            }

            public void focusLost(FocusEvent e) {

                if (password.getPassword().length == 0) {

                    password.setText(passText_placeholder);
                    password.setForeground(Color.GRAY);
                    password.setEchoChar((char) 0);
                }
            }
        });



        /* =====================================================
         * ===================== CLEAR BUTTON ==================
         * ===================================================== */

        JButton clearButton = new JButton();
        clearButton.setBounds(136, 612, 199, 60);
        clearButton.setOpaque(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setBorderPainted(false);
        clearButton.setFocusPainted(false);

        clearButton.addActionListener(e -> {

            username.setText(nameText_placeholder);
            password.setText(passText_placeholder);
            password.setEchoChar((char) 0);

            username.setForeground(Color.GRAY);
            password.setForeground(Color.GRAY);
        });



        /* =====================================================
         * ===================== LOGIN BUTTON ==================
         * ===================================================== */

        JButton loginButton = new JButton();
        loginButton.setBounds(376, 616, 199, 60);
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);

        loginButton.addActionListener(e -> {

            Color errorRed = new Color(220, 80, 80);
            Color normalColor = Color.BLACK;

            boolean username_empty =
                    username.getText().trim().isEmpty() ||
                    username.getText()
                            .equals(nameText_placeholder);

            boolean password_empty =
                    password.getPassword().length == 0 ||
                    new String(password.getPassword())
                            .equals(passText_placeholder);

            if (username_empty || password_empty) {

                JOptionPane.showMessageDialog(
                        frame,
                        "Please fill out all required fields",
                        "Required Fields",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String loginInput =
                    username.getText().trim();

            String rawPassword =
                    new String(password.getPassword());

            loginWithFirebase(loginInput, rawPassword);
        });



        /* =====================================================
         * ===================== FORGOT PASSWORD ===============
         * ===================================================== */

        JButton forgotPassword =
                new JButton("Forgot Password?");

        forgotPassword.setBounds(195, 560, 200, 30);
        forgotPassword.setBorderPainted(false);
        forgotPassword.setContentAreaFilled(false);
        forgotPassword.setFocusPainted(false);

        forgotPassword.addActionListener(e -> {

            String emailInput =
                    JOptionPane.showInputDialog(
                            frame,
                            "Enter your registered email:"
                    );

            if (emailInput != null &&
                    !emailInput.trim().isEmpty()) {

                try {
                    FirebaseAuthService
                            .sendPasswordReset(emailInput.trim());

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



        /* =====================================================
         * ===================== REGISTER BUTTON ===============
         * ===================================================== */

        JButton register = new JButton();
        register.setBounds(279, 759, 165, 30);
        register.setOpaque(false);
        register.setContentAreaFilled(false);
        register.setBorderPainted(false);

        register.addActionListener(e -> {

            frame.setContentPane(new Register(frame));
            frame.revalidate();
        });



        /* =====================================================
         * ===================== ADD COMPONENTS ================
         * ===================================================== */

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



    /* =====================================================
     * ===================== FIREBASE LOGIN =================
     * ===================================================== */

    private void loginWithFirebase(
            String email,
            String password) {

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

            String idToken =
                    loginResult.getString("idToken");

            boolean verified =
                    FirebaseAuthService
                            .isEmailVerified(idToken);

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

                    if ("DELETED"
                            .equalsIgnoreCase(user.getStatus())) {

                        JOptionPane.showMessageDialog(
                                frame,
                                "This account has been removed by the administrator.",
                                "Access Denied",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    switch (user.getRole()) {
                        case "ADMIN":
                            frame.setContentPane(
                                    new AdminDashboard(frame));
                            break;

                        case "STUDENT":
                            frame.setContentPane(
                                    new Student_Dashboard(frame));
                            break;

                        case "LIBRARIAN":
                            frame.setContentPane(
                                    new Librarian_dashboard(frame));
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
