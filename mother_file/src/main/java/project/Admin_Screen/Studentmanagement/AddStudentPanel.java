package project.Admin_Screen.Studentmanagement;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.google.firebase.database.DatabaseReference;

import project.Firebase_backend.User_backend.User;
import project.Firebase_backend.User_backend.UserService;

public class AddStudentPanel extends JPanel {

    private final StudentAccountPanel parent;
  


    public AddStudentPanel(StudentAccountPanel parent) {
        this.parent = parent;

        setLayout(null);
        setOpaque(false);

        ImageIcon icon = new ImageIcon(
            getClass().getResource("/Images/Admin_Student managemenr_addAccount.png")
        );

        Image scaledImage = icon.getImage().getScaledInstance(
            862, 678, Image.SCALE_SMOOTH
        );

        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setBounds(0, 0, 862, 678);
        background.setLayout(null);

        Color fieldColor = new Color(241, 243, 246);
        Font fieldFont = new Font("Poppins", Font.PLAIN, 18);

        JTextField firstName = createField(243, 270, fieldColor, fieldFont);
        firstName.setSize(247, 33);
        applyTextLimit(firstName, 30);
        background.add(firstName);

        JTextField lastName = createField(510, 270, fieldColor, fieldFont);
        lastName.setSize(247, 33);
        applyTextLimit(lastName, 30);
        background.add(lastName);

        JTextField email = createField(243, 351, fieldColor, fieldFont);
        email.setSize(512, 33);
        applyTextLimit(email, 50);
        background.add(email);

        JTextField studentId = createField(243, 520, fieldColor, fieldFont);
        studentId.setSize(280, 33);
        background.add(studentId);

        JPasswordField password = new JPasswordField();
        password.setBounds(243, 436, 225, 33);
        password.setBackground(fieldColor);
        password.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        password.setFont(fieldFont);
        applyTextLimit(password, 32);
        background.add(password);

        JPasswordField C_password = new JPasswordField();
        C_password.setBounds(525, 436, 225, 33);
        C_password.setBackground(fieldColor);
        C_password.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        C_password.setFont(fieldFont);
        applyTextLimit(C_password, 32);
        background.add(C_password);

        JLabel studentFullName = new JLabel();
        studentFullName.setBounds(241, 90, 512, 40);
        studentFullName.setFont(new Font("Sanchez", Font.BOLD, 35));
        studentFullName.setForeground(new Color(80, 80, 80));
        background.add(studentFullName);

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

                if (combined.isEmpty()) {
                    studentFullName.setText("student name here");
                    studentFullName.setForeground(new Color(120, 120, 120));
                    return;
                }

                studentFullName.setText(combined);

                boolean valid =
                    fn.trim().matches("^[A-Za-z ]{2,}$") &&
                    ln.trim().matches("^[A-Za-z ]{2,}$");

                studentFullName.setForeground(
                    valid ? Color.BLACK : new Color(160, 160, 160)
                );
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
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
                student_ID.setForeground(
                    id.matches("^\\d{4}-\\d{7}$")
                        ? Color.BLACK
                        : new Color(160, 160, 160)
                );
            }

            public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
        });

        JLabel imgPreview = new JLabel();
        imgPreview.setBounds(32, 85, 175, 175);
        background.add(imgPreview);

        final File[] selectedFile = new File[1];

        JButton uploadImg = new JButton();
        uploadImg.setBounds(31, 279, 177, 32);
        uploadImg.setContentAreaFilled(false);
        uploadImg.setBorder(null);
        background.add(uploadImg);

        uploadImg.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(
                new FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg")
            );

            if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                selectedFile[0] = chooser.getSelectedFile();
                ImageIcon iconImg = new ImageIcon(
                    new ImageIcon(selectedFile[0].getAbsolutePath())
                        .getImage()
                        .getScaledInstance(175, 175, Image.SCALE_SMOOTH)
                );
                imgPreview.setIcon(iconImg);
            }
        });

        JButton addBtn = new JButton();
        addBtn.setBounds(685, 625, 151, 32);
        addBtn.setBorder(null);
        addBtn.setContentAreaFilled(false);
        addBtn.setFocusPainted(false);
        addBtn.setOpaque(false);

        JButton Close = new JButton();
        Close.setBounds(815, 15, 22, 22);
        //Close.setBorder(null);
        Close.setContentAreaFilled(false);
        Close.setFocusPainted(false);
        Close.setOpaque(false);

        Close.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to close?\nAll entered information will be lost",
                "Confirm Close",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                parent.closeAddStudent();
            }
        });

        JButton clear = new JButton();
        clear.setBounds(516, 625, 117, 32);
        clear.setBorder(null);
        clear.setContentAreaFilled(false);
        clear.setFocusPainted(false);
        clear.setOpaque(false);

        clear.addActionListener(e -> {

    int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to clear all fields?",
            "Confirm Clear",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
    );

    if (confirm != JOptionPane.YES_OPTION) return;

    // Clear text fields
    firstName.setText("");
    lastName.setText("");
    email.setText("");
    studentId.setText("");

    // Clear password fields safely
    Arrays.fill(password.getPassword(), '\0');
    Arrays.fill(C_password.getPassword(), '\0');
    password.setText("");
    C_password.setText("");

    // Reset preview labels
    studentFullName.setText("student name here");
    studentFullName.setForeground(new Color(120, 120, 120));

    student_ID.setText("student id here");
    student_ID.setForeground(new Color(120, 120, 120));

    // Clear image preview
    imgPreview.setIcon(null);
    selectedFile[0] = null;

    // Re-enable add button
    addBtn.setEnabled(true);
});


        addBtn.addActionListener(e -> {

        String fn = firstName.getText().trim();
        String ln = lastName.getText().trim();
        String em = email.getText().trim().toLowerCase();
        String sid = studentId.getText().trim();
        String pw = new String(password.getPassword());
        String cpw = new String(C_password.getPassword());

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

        if (!pw.equals(cpw)) {
            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to add this student account?\n\n"
                        + "Name: " + fn + " " + ln + "\n"
                        + "Student ID: " + sid,
                "Confirm Add Account",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        // Disable button to prevent double click
        addBtn.setEnabled(false);

        DatabaseReference usersRef =
                com.google.firebase.database.FirebaseDatabase
                        .getInstance()
                        .getReference("users");

        // Check if Student ID exists
        usersRef.child(sid)
                .addListenerForSingleValueEvent(
                        new com.google.firebase.database.ValueEventListener() {

            @Override
            public void onDataChange(
                    com.google.firebase.database.DataSnapshot snapshot) {

                if (snapshot.exists()) {
                    JOptionPane.showMessageDialog(
                            AddStudentPanel.this,
                            "Student ID already exists.",
                            "Duplicate ID",
                            JOptionPane.ERROR_MESSAGE
                    );
                    addBtn.setEnabled(true);
                    return;
                }

                // Check email duplication
                UserService.userExistsAsync(em, exists -> {

                    if (exists) {
                        JOptionPane.showMessageDialog(
                                AddStudentPanel.this,
                                "Email already exists.",
                                "Duplicate Email",
                                JOptionPane.ERROR_MESSAGE
                        );
                        addBtn.setEnabled(true);
                        return;
                    }

                    try {

                        // 1️⃣ Create Firebase Auth account
                        org.json.JSONObject authResult =
                                project.Firebase_backend.User_backend.FirebaseAuthService
                                        .register(em, pw);

                        if (authResult.has("error")) {

                            String errorMessage =
                                    authResult.getJSONObject("error")
                                            .getString("message");

                            JOptionPane.showMessageDialog(
                                    AddStudentPanel.this,
                                    "Registration failed:\n" + errorMessage,
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );

                            addBtn.setEnabled(true);
                            return;
                        }

                        String idToken = authResult.getString("idToken");
                        String uid = authResult.getString("localId");

                        project.Firebase_backend.User_backend.FirebaseAuthService
                                .updateDisplayName(idToken, fn + " " + ln);

                        User newUser = new User(
                                sid,
                                "STUDENT",
                                em,
                                fn,
                                ln
                        );

                        newUser.setUid(uid);

                        
                        UserService.registerUserAsync(newUser, success -> {

                            if (!success) {
                                JOptionPane.showMessageDialog(
                                        AddStudentPanel.this,
                                        "Failed to save profile.",
                                        "Error",
                                        JOptionPane.ERROR_MESSAGE
                                );
                                addBtn.setEnabled(true);
                                return;
                            }

                            // Upload image if exists
                            if (selectedFile[0] != null) {
                                UserService.updateStudentProfileImageWithCleanup(
                                        sid,
                                        selectedFile[0],
                                        imgSuccess -> {}
                                );
                            }

                            Arrays.fill(password.getPassword(), '\0');
                            Arrays.fill(C_password.getPassword(), '\0');

                            JOptionPane.showMessageDialog(
                                    AddStudentPanel.this,
                                    "Student account successfully created!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            // ✅ CLOSE PANEL PROPERLY
                            parent.reloadStudents();
                            parent.closeAddStudent();

                        });

                    } catch (Exception ex) {

                        ex.printStackTrace();

                        JOptionPane.showMessageDialog(
                                AddStudentPanel.this,
                                "Something went wrong.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        addBtn.setEnabled(true);
                    }

                });
            }

            @Override
            public void onCancelled(
                    com.google.firebase.database.DatabaseError error) {

                addBtn.setEnabled(true);
            }
        });
    });


        background.add(addBtn);
        background.add(clear);
        background.add(Close);
        add(background);
    }

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
                public void insertString(FilterBypass fb, int offset,
                        String string, AttributeSet attr)
                        throws BadLocationException {

                    if (string != null &&
                        fb.getDocument().getLength() + string.length() <= maxLength) {
                        super.insertString(fb, offset, string, attr);
                    }
                }

                public void replace(FilterBypass fb, int offset, int length,
                        String text, AttributeSet attrs)
                        throws BadLocationException {

                    if (text != null) {
                        int newLength =
                            fb.getDocument().getLength() - length + text.length();
                        if (newLength <= maxLength) {
                            super.replace(fb, offset, length, text, attrs);
                        }
                    }
                }
            }
        );
    }
}
