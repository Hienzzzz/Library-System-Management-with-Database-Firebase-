package project.Admin_Screen.Studentmanagement;

import java.awt.Color;
import java.awt.Font;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import project.Firebase_backend.User_backend.User;
import project.Firebase_backend.User_backend.UserService;

public class AddStudentPanel extends JPanel {

    private final StudentManagement parent;

    public AddStudentPanel(StudentManagement parent) {
        this.parent = parent;

        setLayout(null);
        setBounds(439, 308, 762, 587);
        setOpaque(false);

        JLabel background = new JLabel(
            new ImageIcon(getClass().getResource("/Images/Admin_addStudent.png"))
        );
        background.setBounds(0, 0, 762, 587);
        background.setLayout(null);

        Color fieldColor = new Color(241, 243, 246);
        Font fieldFont = new Font("Poppins", Font.PLAIN, 18);


        // ================= FIRST NAME =================
        JTextField firstName = createField(230, 123, fieldColor, fieldFont);
        firstName.setSize(400, 35);
        
        background.add(firstName);

        // ================= LAST NAME =================
        JTextField lastName = createField(232, 207, fieldColor, fieldFont);
        lastName.setSize(400, 35);
        background.add(lastName);

        // ================= EMAIL =================
        JTextField email = createField(232, 291, fieldColor, fieldFont);
        email.setSize(400, 35);
        background.add(email);

        // ================= STUDENT ID =================
        JTextField studentId = createField(232, 380, fieldColor, fieldFont);
        studentId.setSize(400, 33);
        background.add(studentId);

        applyStudentIdFormatter(studentId);

        // ================= PASSWORD =================
        JPasswordField password = new JPasswordField();
        password.setBounds(232, 461, 400, 33);
        password.setBackground(fieldColor);
        password.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        password.setFont(fieldFont);
        background.add(password);

        // ================= ADD BUTTON =================
        JButton addBtn = new JButton();
        addBtn.setBounds(555, 539, 183, 38);
        styleButton(addBtn);

        // ================= CANCEL BUTTON =================
        JButton cancelBtn = new JButton();
        cancelBtn.setBounds(27, 538, 130, 38);
        styleButton(cancelBtn);

        cancelBtn.addActionListener(e -> parent.closeAddStudent());

        addBtn.addActionListener(e -> {

    Color errorRed = new Color(220, 80, 80);
    Color normalColor = Color.BLACK;

    String fn = firstName.getText().trim();
    String ln = lastName.getText().trim();
    String em = email.getText().trim();
    String sid = studentId.getText().trim();
    String pw = new String(password.getPassword());

    // ===== EMPTY CHECK =====
    if (fn.isEmpty() || ln.isEmpty() || em.isEmpty() || sid.isEmpty() || pw.isEmpty()) {
        JOptionPane.showMessageDialog(
            this,
            "Please fill out all required fields.",
            "Required Fields",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    // ===== FIRST NAME =====
    if (!fn.matches("^[A-Za-z ]+$")) {
        firstName.setForeground(errorRed);
        JOptionPane.showMessageDialog(
            this,
            "Enter a valid first name.",
            "Invalid Input",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    } else if (!fn.matches("^(?=(?:.*[A-Za-z]){2,})[A-Za-z ]*$")) {
        firstName.setForeground(errorRed);
        JOptionPane.showMessageDialog(
            this,
            "First name must have at least 2 characters.",
            "Invalid Input",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    } else {
        firstName.setForeground(normalColor);
    }

    // ===== LAST NAME =====
    if (!ln.matches("^[A-Za-z ]+$")) {
        lastName.setForeground(errorRed);
        JOptionPane.showMessageDialog(
            this,
            "Enter a valid last name.",
            "Invalid Input",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    } else if (!ln.matches("^(?=(?:.*[A-Za-z]){2,})[A-Za-z ]*$")) {
        lastName.setForeground(errorRed);
        JOptionPane.showMessageDialog(
            this,
            "Last name must have at least 2 characters.",
            "Invalid Input",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    } else {
        lastName.setForeground(normalColor);
    }

    // ===== EMAIL =====
    if (!em.matches("^[A-Za-z0-9._%+-]+@students\\.nu-moa\\.edu\\.ph$")) {
        email.setForeground(errorRed);
        JOptionPane.showMessageDialog(
            this,
            "Please enter a valid NU MOA student email address.",
            "Invalid Email",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    } else {
        email.setForeground(normalColor);
    }


    // ===== STUDENT ID =====
    if (!sid.matches("^\\d{4}-\\d{7}$")) {
        studentId.setForeground(errorRed);
        JOptionPane.showMessageDialog(
            this,
            "Student ID must follow the format: YYYY-XXXXXXX",
            "Invalid Student ID",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    } else {
        studentId.setForeground(normalColor);
    }

    // ===== PASSWORD =====
    if (!pw.matches("^\\S+$")) {
        JOptionPane.showMessageDialog(
            this,
            "Password must not contain spaces.",
            "Invalid Password",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    if (pw.length() < 8) {
        JOptionPane.showMessageDialog(
            this,
            "Password must be at least 8 characters long.",
            "Weak Password",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    // ===== DUPLICATE CHECK =====
    if (UserService.userExists(em)) {
        JOptionPane.showMessageDialog(
            this,
            "Student account already exists.",
            "Duplicate Account",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    // ===== CREATE USER =====
    User user = new User(fn, ln, em, sid, pw);

    boolean success = UserService.registerUser(user);

    if (!success) {
        JOptionPane.showMessageDialog(
            this,
            "Failed to create student account. Please try again.",
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    JOptionPane.showMessageDialog(
        this,
        "Student account created successfully!\n\n" +
        "Name: " + firstName.getText().trim() + " " + lastName.getText().trim() + "\n" +
        "Role: STUDENT",
        "Success",
        JOptionPane.INFORMATION_MESSAGE
    );


    Arrays.fill(password.getPassword(), '\0');
    parent.closeAddStudent();
});


        background.add(addBtn);
        background.add(cancelBtn);
        add(background);
    }

    // ================= HELPERS =================

    private JTextField createField(int x, int y, Color bg, Font f) {
        JTextField tf = new JTextField();
        tf.setBounds(x, y, 500, 33);
        tf.setBackground(bg);
        tf.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        tf.setFont(f);
        return tf;
    }

    private void styleButton(JButton b) {
        b.setBorder(null);
        b.setContentAreaFilled(false);
        b.setFocusPainted(false);
        b.setOpaque(false);
    }

    private void applyStudentIdFormatter(JTextField field) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(
            new DocumentFilter() {
                @Override
                public void replace(FilterBypass fb, int offset, int length,
                        String text, AttributeSet attrs)
                        throws BadLocationException {

                    String digits = (fb.getDocument()
                            .getText(0, fb.getDocument().getLength()) + text)
                            .replaceAll("\\D", "");

                    if (digits.length() > 11)
                        digits = digits.substring(0, 11);

                    String formatted =
                        digits.length() > 4
                            ? digits.substring(0, 4) + "-" + digits.substring(4)
                            : digits;

                    fb.replace(0, fb.getDocument().getLength(), formatted, attrs);
                }
            }
        );
    }
}
