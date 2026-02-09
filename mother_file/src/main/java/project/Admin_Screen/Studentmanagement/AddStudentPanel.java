package project.Admin_Screen.Studentmanagement;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import project.Firebase_backend.Studnet_backend.Student;
import project.Firebase_backend.Studnet_backend.StudentService;
import project.Firebase_backend.User_backend.User;
import project.Firebase_backend.User_backend.UserService;

public class AddStudentPanel extends JPanel {

    private final StudentAccountPanel parent;

    public AddStudentPanel(StudentAccountPanel parent) {
        this.parent = parent;

        setLayout(null);

        setOpaque(false);

        // Load image
        ImageIcon icon = new ImageIcon(
            getClass().getResource("/Images/Admin_Student managemenr_addAccount.png")
        );

        // Scale image to EXACT panel size
        Image scaledImage = icon.getImage().getScaledInstance(
            862, 678, Image.SCALE_SMOOTH
        );

        // Background label
        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setBounds(0, 0, 862, 678);
        background.setLayout(null);

        Color fieldColor = new Color(241, 243, 246);
        Font fieldFont = new Font("Poppins", Font.PLAIN, 18);


        //================== display studnet name ==============

        
        // ================= FIRST NAME =================
        JTextField firstName = createField(243, 270, fieldColor, fieldFont);
        firstName.setSize(247, 33);
        applyTextLimit(firstName, 30);
        background.add(firstName);

        // ================= LAST NAME =================
        JTextField lastName = createField(510, 270, fieldColor, fieldFont);
        lastName.setSize(247, 33);
        applyTextLimit(lastName, 30);
        background.add(lastName);

        // ================= EMAIL =================
        JTextField email = createField(243, 351, fieldColor, fieldFont);
        email.setSize(512, 33);
        applyTextLimit(email, 50);
        background.add(email);

        // ================= STUDENT ID =================
        JTextField studentId = createField(243, 520, fieldColor, fieldFont);
        studentId.setSize(280, 33);
        background.add(studentId);

   
        


        // ================= PASSWORD =================
        JPasswordField password = new JPasswordField();
        password.setBounds(243, 436, 225, 33);
        password.setBackground(fieldColor);
        password.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        password.setFont(fieldFont);
        applyTextLimit(password, 32);
        background.add(password);
        
        // ================= confirm PASSWORD  =================
        JPasswordField C_password = new JPasswordField();
        C_password.setBounds(525, 436, 225, 33);
        C_password.setBackground(fieldColor);
        C_password.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        C_password.setFont(fieldFont);
        applyTextLimit(C_password, 32);
        background.add(C_password);


        

        //================== display studnet name ==============

        JLabel studentFullName = new JLabel();
        studentFullName.setBounds(241, 90, 512, 40);
        studentFullName.setFont(new Font("Sanchez", Font.BOLD, 35));
        studentFullName.setForeground(new Color(80, 80, 80));
        background.add(studentFullName);

        // Student ID preview
        JLabel student_ID = new JLabel();
        student_ID.setBounds(240, 170, 429, 30);
        student_ID.setFont(new Font("Sanchez", Font.PLAIN, 24));
        student_ID.setForeground(new Color(120, 120, 120));
        background.add(student_ID);

        DocumentListener namePreviewListener = new DocumentListener() {

            private void update() {
                String fn = firstName.getText();
                String ln = lastName.getText();

                String combined = (fn + " " + ln).trim();

                // Nothing typed yet
                if (combined.isEmpty()) {
                    studentFullName.setText("student name here");
                    studentFullName.setForeground(new Color(120, 120, 120));
                    return;
                }

                // Show what user is typing
                studentFullName.setText(combined);

                // Valid only if BOTH names have 2+ letters
                boolean valid =
                    fn.trim().matches("^[A-Za-z ]{2,}$") &&
                    ln.trim().matches("^[A-Za-z ]{2,}$");

                if (valid) {
                    studentFullName.setForeground(Color.BLACK);
                } else {
                    studentFullName.setForeground(new Color(160, 160, 160));
                }
            }

            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        };

        firstName.getDocument().addDocumentListener(namePreviewListener);
        lastName.getDocument().addDocumentListener(namePreviewListener);


        applyStudentIdFormatter(studentId);

        studentId.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                String id = studentId.getText();

                if (id.isEmpty()) {
                    student_ID.setText("student id here");
                    student_ID.setForeground(new Color(120, 120, 120));
                    return;
                }

                student_ID.setText(id);

                if (id.matches("^\\d{4}-\\d{7}$")) {
                    student_ID.setForeground(Color.BLACK); // valid
                } else {
                    student_ID.setForeground(new Color(160, 160, 160)); // typing
                }
            }

            @Override public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            @Override public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });


 
        //upload Image
        JButton uploadImg = new JButton();
        uploadImg.setBounds(31, 279, 177, 32);
        uploadImg.setContentAreaFilled(false);
        uploadImg.setBorder(null);
        background.add(uploadImg);

        // ================= ADD BUTTON =================
        JButton addBtn = new JButton();
        addBtn.setBounds(685, 625, 151, 32);
        addBtn.setBorder(null);
        addBtn.setContentAreaFilled(false);
        addBtn.setFocusPainted(false);
        addBtn.setOpaque(false);

        // ================= CANCEL BUTTON =================
        JButton cancelBtn = new JButton();
        cancelBtn.setBounds(516, 625, 117, 32);
        cancelBtn.setBorder(null);
        cancelBtn.setContentAreaFilled(false);
        cancelBtn.setFocusPainted(false);
        cancelBtn.setOpaque(false);

     

        cancelBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this, 
                "Are you sure you wan tto close?\nAll entered information will be lost",
                "Confirm Close",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if(choice == JOptionPane.YES_OPTION){
                parent.closeAddStudent();
            }
        });

        addBtn.addActionListener(e -> {

        Color errorRed = new Color(220, 80, 80);
        Color normalColor = Color.BLACK;

        String fn = firstName.getText().trim();
        String ln = lastName.getText().trim();
        String em = email.getText().trim();
        String sid = studentId.getText().trim();
        String pw = new String(password.getPassword());
        String cpw = new String(C_password.getPassword());

        // ================= EMPTY CHECK =================
        if (fn.isEmpty() || ln.isEmpty() || em.isEmpty()
                || sid.isEmpty() || pw.isEmpty() || cpw.isEmpty()) {

            JOptionPane.showMessageDialog(
                this,
                "Please fill out all required fields.",
                "Required Fields",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ================= FIRST NAME =================
        if (!fn.matches("^[A-Za-z ]+$") ||
            !fn.matches("^(?=(?:.*[A-Za-z]){2,}).*$")) {

            JOptionPane.showMessageDialog(
                this,
                "First name must contain at least 2 letters and no symbols.",
                "Invalid Name",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ================= LAST NAME =================
        if (!ln.matches("^[A-Za-z ]+$") ||
            !ln.matches("^(?=(?:.*[A-Za-z]){2,}).*$")) {

            JOptionPane.showMessageDialog(
                this,
                "Last name must contain at least 2 letters and no symbols.",
                "Invalid Name",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ================= EMAIL =================
        if (!em.matches("^[A-Za-z0-9._%+-]+@students\\.nu-moa\\.edu\\.ph$")) {
            JOptionPane.showMessageDialog(
                this,
                "Please enter a valid NU MOA student email address.",
                "Invalid Email",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ================= STUDENT ID =================
        if (!sid.matches("^\\d{4}-\\d{7}$")) {
            JOptionPane.showMessageDialog(
                this,
                "Student ID must follow the format YYYY-XXXXXXX.",
                "Invalid Student ID",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ================= PASSWORD =================
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

        // ================= CONFIRM PASSWORD =================
        if (!pw.equals(cpw)) {
            JOptionPane.showMessageDialog(
                this,
                "Passwords do not match.",
                "Password Mismatch",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ================= DUPLICATE CHECK =================
        if (UserService.userExists(em) || UserService.userExists(sid)) {
            JOptionPane.showMessageDialog(
                this,
                "A student account with this email or ID already exists.",
                "Duplicate Account",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }
        // ================= CONFIRM FIRST =================
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to add this student account?\n\n"
        + "Name: " + fn + " " + ln + "\n"
        + "Student ID: " + sid,
            "Confirm Add Account",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return; // Admin cancelled
        }

        // ================= CREATE USER =================
        User user = new User(fn, ln, em, sid, pw);
        boolean registered = UserService.registerUser(user);

        if (!registered) {
            JOptionPane.showMessageDialog(
                this,
                "Failed to create student account.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // ================= CREATE STUDENT =================
        Student student = new Student(fn, ln, em, sid, pw);
        StudentService.addStudent(student);

        // ================= SUCCESS INFO =================
        JOptionPane.showMessageDialog(
            this,
            "Student account added successfully!\n\n"
        + "Full Name: " + fn + " " + ln + "\n"
        + "Student ID: " + sid + "\n"
        + "Role: STUDENT",
            "Account Created",
            JOptionPane.INFORMATION_MESSAGE
        );

        // ================= CLEANUP =================
        Arrays.fill(password.getPassword(), '\0');
        Arrays.fill(C_password.getPassword(), '\0');

        parent.closeAddStudent();
            });



        background.add(addBtn);
        background.add(cancelBtn);
        this.add(background);
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

    private void applyTextLimit(JTextField field, int maxLength) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(
            new DocumentFilter() {

                @Override
                public void insertString(FilterBypass fb, int offset,
                                        String string, AttributeSet attr)
                        throws BadLocationException {

                    if (string == null) return;

                    if (fb.getDocument().getLength() + string.length() <= maxLength) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                @Override
                public void replace(FilterBypass fb, int offset, int length,
                                    String text, AttributeSet attrs)
                        throws BadLocationException {

                    if (text == null) return;

                    int currentLength = fb.getDocument().getLength();
                    int newLength = currentLength - length + text.length();

                    if (newLength <= maxLength) {
                        super.replace(fb, offset, length, text, attrs);
                    }
                }
            }
        );
    }


    
}