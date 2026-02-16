package project.Login_screen;

/* =========================================================
 * ========================= IMPORTS =======================
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

// ================= UTIL =================
import java.util.Arrays;

// ================= SWING =================
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

// ================= FIREBASE SERVICES =================
import project.Firebase_backend.User_backend.FirebaseAuthService;
import project.Firebase_backend.User_backend.User;
import project.Firebase_backend.User_backend.UserService;

// ================= MAIN FRAME =================
import project.Main_System.MainFrame;


/* =========================================================
 * ======================= REGISTER PANEL ==================
 * ========================================================= */

public class Register extends javax.swing.JPanel {

    /* =====================================================
     * ===================== VARIABLES ======================
     * ===================================================== */

    private MainFrame frame;


    /* =====================================================
     * ===================== CONSTRUCTOR ====================
     * ===================================================== */

    public Register(MainFrame frame) {
        this.frame = frame;
        panel();
    }


    /* =====================================================
     * ===================== UI SECTION =====================
     * ===================================================== */

    public void panel() {

        // ================= BASE PANEL =================
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));

        JLabel background = new JLabel();
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        ImageIcon image = new ImageIcon(
                getClass().getResource("/Images/Login_REGISTER PAGE.png")
        );

        // ================= RIGHT GIF SECTION =================
        JLabel gif = new JLabel();
        gif.setBounds(865, 138, 624, 624);

        ImageIcon gifIcon = new ImageIcon(
                getClass().getResource("/Images/Login_registration.gif")
        );
        gif.setIcon(gifIcon);

                /* =====================================================
         * ===================== FIRST NAME =====================
         * ===================================================== */

        String firstname_placeHolder = "Enter Firstname";

        JTextField firstname = new JTextField(firstname_placeHolder);
        firstname.setBounds(200, 284, 370, 50);
        firstname.setBackground(new Color(245, 246, 250));
        firstname.setBorder(null);
        firstname.setFont(new Font("Sanchez", Font.PLAIN, 20));
        firstname.setForeground(Color.GRAY);

        firstname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (firstname.getText().equals(firstname_placeHolder)) {
                    firstname.setText("");
                    firstname.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (firstname.getText().isEmpty()) {
                    firstname.setText(firstname_placeHolder);
                    firstname.setForeground(Color.GRAY);
                }
            }
        });


        /* =====================================================
         * ===================== LAST NAME ======================
         * ===================================================== */

        String lastname_placeHolder = "Enter Lastname";

        JTextField lastname = new JTextField(lastname_placeHolder);
        lastname.setBounds(200, 355, 370, 50);
        lastname.setBackground(new Color(245, 246, 250));
        lastname.setBorder(null);
        lastname.setFont(new Font("Sanchez", Font.PLAIN, 20));
        lastname.setForeground(Color.GRAY);

        lastname.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (lastname.getText().equals(lastname_placeHolder)) {
                    lastname.setText("");
                    lastname.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (lastname.getText().isEmpty()) {
                    lastname.setText(lastname_placeHolder);
                    lastname.setForeground(Color.GRAY);
                }
            }
        });


        /* =====================================================
         * ===================== EMAIL FIELD ====================
         * ===================================================== */

        String Email_palceHolder = "Enter Email Address";

        JTextField email = new JTextField(Email_palceHolder);
        email.setBounds(200, 426, 370, 50);
        email.setBackground(new Color(245, 246, 250));
        email.setBorder(null);
        email.setFont(new Font("Sanchez", Font.PLAIN, 20));
        email.setForeground(Color.GRAY);

        email.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (email.getText().equals(Email_palceHolder)) {
                    email.setText("");
                    email.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (email.getText().isEmpty()) {
                    email.setText(Email_palceHolder);
                    email.setForeground(Color.GRAY);
                }
            }
        });


        /* =====================================================
         * ===================== STUDENT ID =====================
         * ===================================================== */

        String ID_placeHolder = "Enter Student ID No.";

        JTextField Student_ID = new JTextField(ID_placeHolder);
        Student_ID.setBounds(200, 495, 370, 50);
        Student_ID.setBackground(new Color(245, 246, 250));
        Student_ID.setBorder(null);
        Student_ID.setFont(new Font("Sanchez", Font.PLAIN, 20));
        Student_ID.setForeground(Color.GRAY);

        final boolean[] isIDPlaceholder = { true };

        Student_ID.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                if (isIDPlaceholder[0]) {
                    isIDPlaceholder[0] = false;
                    Student_ID.setText("");
                    Student_ID.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (Student_ID.getText().isEmpty()) {
                    isIDPlaceholder[0] = true;
                    Student_ID.setText(ID_placeHolder);
                    Student_ID.setForeground(Color.GRAY);
                }
            }
        });

        ((AbstractDocument) Student_ID.getDocument())
                .setDocumentFilter(new DocumentFilter() {

                    @Override
                    public void insertString(FilterBypass fb, int offset,
                                             String string, AttributeSet attr)
                            throws BadLocationException {
                        replace(fb, offset, 0, string, attr);
                    }

                    @Override
                    public void replace(FilterBypass fb, int offset,
                                        int length, String text,
                                        AttributeSet attrs)
                            throws BadLocationException {

                        if (isIDPlaceholder[0]) {
                            fb.replace(0, fb.getDocument().getLength(), text, attrs);
                            return;
                        }

                        String currentText =
                                fb.getDocument().getText(0, fb.getDocument().getLength());

                        String newText = currentText.substring(0, offset)
                                + text
                                + currentText.substring(offset + length);

                        String digits = newText.replaceAll("\\D", "");

                        if (digits.length() > 11) {
                            digits = digits.substring(0, 11);
                        }

                        String formatted;

                        if (digits.length() > 4) {
                            formatted = digits.substring(0, 4)
                                    + "-" + digits.substring(4);
                        } else {
                            formatted = digits;
                        }

                        fb.replace(0, fb.getDocument().getLength(),
                                formatted, attrs);
                    }

                    @Override
                    public void remove(FilterBypass fb, int offset,
                                       int length)
                            throws BadLocationException {
                        replace(fb, offset, length, "", null);
                    }
                });

                        /* =====================================================
         * ===================== PASSWORD FIELD ================
         * ===================================================== */

        String password_placeHolder = "Set password";

        JPasswordField password = new JPasswordField(password_placeHolder);
        password.setBounds(200, 566, 330, 50);
        password.setBackground(new Color(245, 246, 250));
        password.setBorder(null);
        password.setFont(new Font("Sanchez", Font.PLAIN, 20));
        password.setForeground(Color.GRAY);
        password.setEchoChar((char) 0);

        // ================= EYE ICONS =================
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

        // ================= SHOW PASSWORD CHECKBOX =================
        JCheckBox showPassword = new JCheckBox();
        showPassword.setBounds(537, 578, 40, 30);
        showPassword.setBorder(null);
        showPassword.setContentAreaFilled(false);
        showPassword.setOpaque(false);
        showPassword.setFocusPainted(false);
        showPassword.setSelectedIcon(openEye);
        showPassword.setIcon(closedEye);

        showPassword.addActionListener(e -> {
            String text = new String(password.getPassword());

            if (!text.equals(password_placeHolder)) {
                if (showPassword.isSelected()) {
                    password.setEchoChar((char) 0);
                } else {
                    password.setEchoChar('•');
                }
            }
        });

        // ================= PASSWORD PLACEHOLDER LOGIC =================
        password.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                String currentText = new String(password.getPassword());

                if (currentText.equals(password_placeHolder)) {
                    password.setText("");
                    password.setForeground(Color.BLACK);

                    if (!showPassword.isSelected()) {
                        password.setEchoChar('•');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (password.getPassword().length == 0) {
                    password.setText(password_placeHolder);
                    password.setForeground(Color.GRAY);
                    password.setEchoChar((char) 0);
                }
            }
        });


        /* =====================================================
         * ================= VERIFY PASSWORD FIELD =============
         * ===================================================== */

        String verify_placeHolder = "Verify Password";

        JPasswordField verify_password = new JPasswordField(verify_placeHolder);
        verify_password.setBounds(200, 635, 330, 50);
        verify_password.setBackground(new Color(245, 246, 250));
        verify_password.setBorder(null);
        verify_password.setFont(new Font("Sanchez", Font.PLAIN, 20));
        verify_password.setForeground(Color.GRAY);
        verify_password.setEchoChar((char) 0);

        // ================= SHOW VERIFY PASSWORD =================
        JCheckBox show_verifyPassword = new JCheckBox();
        show_verifyPassword.setBounds(537, 654, 30, 20);
        show_verifyPassword.setBorder(null);
        show_verifyPassword.setContentAreaFilled(false);
        show_verifyPassword.setOpaque(false);
        show_verifyPassword.setFocusPainted(false);
        show_verifyPassword.setIcon(closedEye);
        show_verifyPassword.setSelectedIcon(openEye);

        show_verifyPassword.addActionListener(e -> {

            String text = new String(verify_password.getPassword());

            if (!text.equals(verify_placeHolder)) {
                if (show_verifyPassword.isSelected()) {
                    verify_password.setEchoChar((char) 0);
                } else {
                    verify_password.setEchoChar('•');
                }
            }
        });

        // ================= VERIFY PLACEHOLDER =================
        verify_password.addFocusListener(new FocusAdapter() {

            @Override
            public void focusGained(FocusEvent e) {
                String currentText = new String(verify_password.getPassword());

                if (currentText.equals(verify_placeHolder)) {
                    verify_password.setText("");
                    verify_password.setForeground(Color.BLACK);

                    if (!show_verifyPassword.isSelected()) {
                        verify_password.setEchoChar('•');
                    }
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (verify_password.getPassword().length == 0) {
                    verify_password.setText(verify_placeHolder);
                    verify_password.setForeground(Color.GRAY);
                    verify_password.setEchoChar((char) 0);
                }
            }
        });


        /* =====================================================
         * ===================== CLEAR BUTTON ===================
         * ===================================================== */

        JButton clearButton = new JButton();
        clearButton.setBounds(181, 733, 192, 62);
        clearButton.setContentAreaFilled(false);
        clearButton.setBorder(null);
        clearButton.setFocusPainted(false);
        clearButton.setOpaque(false);

        clearButton.addActionListener(e -> {

            firstname.setText(firstname_placeHolder);
            firstname.setForeground(Color.GRAY);

            lastname.setText(lastname_placeHolder);
            lastname.setForeground(Color.GRAY);

            email.setText(Email_palceHolder);
            email.setForeground(Color.GRAY);

            isIDPlaceholder[0] = true;
            Student_ID.setText(ID_placeHolder);
            Student_ID.setForeground(Color.GRAY);

            password.setText(password_placeHolder);
            password.setEchoChar((char) 0);
            password.setForeground(Color.GRAY);

            verify_password.setText(verify_placeHolder);
            verify_password.setEchoChar((char) 0);
            verify_password.setForeground(Color.GRAY);
        });

                /* =====================================================
         * ================= CREATE ACCOUNT BUTTON =============
         * ===================================================== */

        JButton createButton = new JButton();
        createButton.setBounds(398, 733, 193, 62);
        createButton.setContentAreaFilled(false);
        createButton.setBorder(null);
        createButton.setOpaque(false);
        createButton.setFocusPainted(false);

        createButton.addActionListener(e -> {

            /* =====================================================
             * ================= VALIDATION SECTION ================
             * ===================================================== */

            Color errorRed = new Color(220, 80, 80);
            Color normalColor = Color.BLACK;

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

            // ================= CHECK ALL EMPTY =================
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
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            /* =====================================================
             * ================= FIELD VALIDATIONS =================
             * ===================================================== */

            // ===== FIRST NAME =====
            if (firstEmpty) {
                firstname.setForeground(errorRed);
                JOptionPane.showMessageDialog(frame,
                        "Please enter your first name",
                        "Required Fields",
                        JOptionPane.ERROR_MESSAGE);
                return;

            } else if (!firstname.getText().trim().matches("^[A-Za-z ]+$")) {

                firstname.setForeground(errorRed);
                JOptionPane.showMessageDialog(
                        frame,
                        "Enter a valid first name",
                        "Invalid Name",
                        JOptionPane.ERROR_MESSAGE
                );
                return;

            } else if (!firstname.getText().trim().matches("^(?=(?:.*[A-Za-z]){2,})[A-Za-z ]*$")) {

                firstname.setForeground(errorRed);
                JOptionPane.showMessageDialog(
                        frame,
                        "Firstname must have 2 or more characters",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;

            } else {
                firstname.setForeground(normalColor);
            }

            // ===== LAST NAME =====
            if (lastEmpty) {

                lastname.setForeground(errorRed);
                JOptionPane.showMessageDialog(frame,
                        "Please enter your last name",
                        "Required Fields",
                        JOptionPane.ERROR_MESSAGE);
                return;

            } else if (!lastname.getText().trim().matches("^[A-Za-z]+( [A-Za-z]+)*$")) {

                lastname.setForeground(errorRed);
                JOptionPane.showMessageDialog(
                        frame,
                        "Enter a valid Lastname",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;

            } else if (!lastname.getText().trim().matches("^(?=(?:.*[A-Za-z]){2,})[A-Za-z ]*$")) {

                lastname.setForeground(errorRed);
                JOptionPane.showMessageDialog(
                        frame,
                        "Lastname must have 2 or more characters",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;

            } else {
                lastname.setForeground(normalColor);
            }

            // ===== EMAIL =====
            if (emailEmpty) {

                email.setForeground(errorRed);
                JOptionPane.showMessageDialog(frame,
                        "Please enter your email address",
                        "Required Fields",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.getText().trim()
                    .matches("^[A-Za-z0-9._%+-]+@students\\.nu-moa\\.edu\\.ph$")) {

                email.setForeground(errorRed);
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter a valid Email Address",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;

            } else {
                email.setForeground(normalColor);
            }

            // ===== STUDENT ID =====
            if (idEmpty) {

                Student_ID.setForeground(errorRed);
                JOptionPane.showMessageDialog(frame,
                        "Please enter your student ID number",
                        "Required Fields",
                        JOptionPane.ERROR_MESSAGE);
                return;

            } else if (!Student_ID.getText().matches("^\\d{4}-\\d{7}$")) {

                Student_ID.setForeground(errorRed);
                JOptionPane.showMessageDialog(
                        frame,
                        "Please enter a valid Student ID No.",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;

            } else {
                Student_ID.setForeground(normalColor);
            }

            // ===== PASSWORD =====
            if (passwordEmpty) {

                password.setForeground(errorRed);
                JOptionPane.showMessageDialog(frame,
                        "Please create a password",
                        "Required Fields",
                        JOptionPane.ERROR_MESSAGE);
                return;

            } else if (!password.getText().trim().matches("^\\S+$")) {

                password.setForeground(errorRed);
                JOptionPane.showMessageDialog(
                        frame,
                        "Password must not contain whitespace",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;

            } else if (!password.getText().trim().matches("^.{8,}$")) {

                password.setForeground(errorRed);
                JOptionPane.showMessageDialog(
                        frame,
                        "Password must have 8 or more characters",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;

            } else {
                password.setForeground(normalColor);
            }

            // ===== VERIFY PASSWORD =====
            if (verifyEmpty) {

                verify_password.setForeground(errorRed);
                JOptionPane.showMessageDialog(frame,
                        "Please verify your password",
                        "Required Fields",
                        JOptionPane.ERROR_MESSAGE);
                return;

            } else if (!Arrays.equals(password.getPassword(), verify_password.getPassword())) {

                verify_password.setForeground(errorRed);
                JOptionPane.showMessageDialog(
                        frame,
                        "Passwords do not match. Please try again",
                        "Invalid Input",
                        JOptionPane.ERROR_MESSAGE
                );
                return;

            } else {
                verify_password.setForeground(normalColor);
            }


            /* =====================================================
             * ================= FIREBASE SECTION ==================
             * ===================================================== */

            String firstName = capitalizeWords(firstname.getText().trim());
            String surname = capitalizeWords(lastname.getText().trim());
            String Email = email.getText().trim().toLowerCase();
            String id = Student_ID.getText().trim();
            String rawPassword = new String(password.getPassword());

            User newUser = new User(
                    id,
                    "STUDENT",
                    Email,
                    firstName,
                    surname
            );

            try {

                org.json.JSONObject authResult =
                        FirebaseAuthService.register(Email, rawPassword);

                if (authResult.has("error")) {

                    String errorMessage =
                            authResult.getJSONObject("error")
                                    .getString("message");

                    JOptionPane.showMessageDialog(
                            frame,
                            "Registration failed:\n" + errorMessage,
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                String idToken = authResult.getString("idToken");
                String uid = authResult.getString("localId");
                newUser.setUid(uid);

                FirebaseAuthService.updateDisplayName(
                        idToken,
                        firstName + " " + surname
                );

                FirebaseAuthService.sendVerification(idToken);

                UserService.registerUserAsync(newUser, success -> {

                    if (!success) {
                        JOptionPane.showMessageDialog(
                                frame,
                                "Failed to save user profile in database.",
                                "Database Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                        return;
                    }

                    JOptionPane.showMessageDialog(
                            frame,
                            "Account created successfully!\n\n" +
                                    "Please check your Gmail and verify your email before logging in.",
                            "Registration Successful",
                            JOptionPane.INFORMATION_MESSAGE
                    );

                    Arrays.fill(password.getPassword(), '\0');
                    Arrays.fill(verify_password.getPassword(), '\0');

                    frame.setContentPane(new Login(frame));
                    frame.revalidate();
                    frame.repaint();
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
        });


        /* =====================================================
         * ================= LOGIN SWITCH BUTTON ===============
         * ===================================================== */

        JButton login = new JButton();
        login.setBounds(425, 823, 84, 17);
        login.setContentAreaFilled(false);
        login.setOpaque(false);
        login.setFocusPainted(false);
        login.setBorderPainted(false);

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Switching to Login page");
                frame.setContentPane(new Login(frame));
                frame.revalidate();
            }
        });


        /* =====================================================
         * ================= ADD COMPONENTS =====================
         * ===================================================== */

        background.add(login);
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
        background.add(gif);

        background.setIcon(image);
        this.add(background);
    }


    /* =====================================================
     * ================= HELPER METHODS =====================
     * ===================================================== */

    private String capitalizeWords(String input) {

        if (input == null || input.isEmpty()) return input;

        String[] words = input.trim().toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            result.append(Character.toUpperCase(word.charAt(0)))
                    .append(word.substring(1))
                    .append(" ");
        }

        return result.toString().trim();
    }

}



