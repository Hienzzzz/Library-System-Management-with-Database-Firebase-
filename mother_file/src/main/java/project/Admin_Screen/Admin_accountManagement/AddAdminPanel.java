package project.Admin_Screen.Admin_accountManagement;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.Firebase_backend.User_backend.User;
import project.Firebase_backend.User_backend.UserService;

public class AddAdminPanel extends JPanel {

    private final Admin_AccountManagement parent;
    private JTextField Title;
    private JTextField Author;
    private JTextField Genre;
    private JTextArea description;
    private JSpinner quantityButton;
    private JLabel imgPreview;
    private File[] selectedFile = new File[1];


    public AddAdminPanel(Admin_AccountManagement parent) {

        this.parent = parent;

        setLayout(null);
        setOpaque(false);

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_addAddcound.png")
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

        JTextField adminIdField = createField(243, 520, fieldColor, fieldFont);
        adminIdField.setSize(280, 33);
        background.add(adminIdField);
        adminIdField.setEditable(false);
        adminIdField.setFocusable(false);


        

        JPasswordField password = new JPasswordField();
        password.setBounds(243, 436, 225, 33);
        password.setBackground(fieldColor);
        password.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        password.setFont(fieldFont);
        applyTextLimit(password, 32);
        background.add(password);

        JPasswordField confirmPassword = new JPasswordField();
        confirmPassword.setBounds(525, 436, 225, 33);
        confirmPassword.setBackground(fieldColor);
        confirmPassword.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        confirmPassword.setFont(fieldFont);
        applyTextLimit(confirmPassword, 32);
        background.add(confirmPassword);

        // ===== ROLE SELECTION =====
        JRadioButton adminRadio = new JRadioButton("Admin");
        adminRadio.setBounds(560, 525, 120, 25);
        adminRadio.setFont(new Font("Poppins", Font.PLAIN, 15));
        adminRadio.setOpaque(false);

        JRadioButton librarianRadio = new JRadioButton("Librarian");
        librarianRadio.setBounds(635, 525, 150, 25);
        librarianRadio.setFont(new Font("Poppins", Font.PLAIN, 15));
        librarianRadio.setOpaque(false);

        ButtonGroup roleGroup = new ButtonGroup();
        roleGroup.add(adminRadio);
        roleGroup.add(librarianRadio);
        adminRadio.setSelected(true);
        generateNextAdminId("ADMIN", adminIdField);


        
        background.add(adminRadio);
        background.add(librarianRadio);

        adminRadio.addActionListener(e ->
        generateNextAdminId("ADMIN", adminIdField));

        librarianRadio.addActionListener(e ->
        generateNextAdminId("LIBRARIAN", adminIdField));
            

        JButton addBtn = new JButton();
        addBtn.setBounds(685, 625, 151, 32);
        addBtn.setBorder(null);
        addBtn.setContentAreaFilled(false);
        background.add(addBtn);

        JButton closeBtn = new JButton();
        closeBtn.setBounds(815, 15, 22, 22);
        closeBtn.setContentAreaFilled(false);
        background.add(closeBtn);

        closeBtn.addActionListener(e -> parent.closeAddAdmin());

        addBtn.addActionListener(e -> {

    String fn = firstName.getText().trim();
    String ln = lastName.getText().trim();
    String em = email.getText().trim().toLowerCase();
    String id = adminIdField.getText().trim();
    String pw = new String(password.getPassword());
    String cpw = new String(confirmPassword.getPassword());

    if (fn.isEmpty() || ln.isEmpty() || em.isEmpty()
            || id.isEmpty() || pw.isEmpty() || cpw.isEmpty()) {

        JOptionPane.showMessageDialog(
                AddAdminPanel.this,
                "Please fill out all required fields.",
                "Required Fields",
                JOptionPane.ERROR_MESSAGE
        );

        return;
    }

    // ===== PASSWORD MATCH =====
    if (!pw.equals(cpw)) {
        JOptionPane.showMessageDialog(
                AddAdminPanel.this,
                "Passwords do not match.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    // ===== ROLE & ID FORMAT =====
    String selectedRole = adminRadio.isSelected() ? "ADMIN" : "LIBRARIAN";

    String requiredPrefix = selectedRole.equals("ADMIN") ? "AD" : "LB";

    if (!id.matches(requiredPrefix + "\\d{5}")) {
        JOptionPane.showMessageDialog(
                AddAdminPanel.this,
                selectedRole + " ID must follow format: "
                        + requiredPrefix + "00000",
                "Invalid ID Format",
                JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(
            AddAdminPanel.this,
            "Add this account?\n\n"
                    + "Name: " + fn + " " + ln + "\n"
                    + "ID: " + id + "\n"
                    + "Role: " + selectedRole,
            "Confirm Add Account",
            JOptionPane.YES_NO_OPTION
    );

    if (confirm != JOptionPane.YES_OPTION) return;

    addBtn.setEnabled(false);

    DatabaseReference usersRef =
            FirebaseDatabase.getInstance().getReference("users");

    // ===== CHECK DUPLICATE ID =====
    usersRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot snapshot) {

            if (snapshot.exists()) {
                JOptionPane.showMessageDialog(
                        AddAdminPanel.this,
                        "Account ID already exists.",
                        "Duplicate ID",
                        JOptionPane.ERROR_MESSAGE
                );
                addBtn.setEnabled(true);
                return;
            }

            // ===== CHECK DUPLICATE EMAIL =====
            UserService.userExistsAsync(em, exists -> {

                if (exists) {
                    JOptionPane.showMessageDialog(
                            AddAdminPanel.this,
                            "Email already exists.",
                            "Duplicate Email",
                            JOptionPane.ERROR_MESSAGE
                    );
                    addBtn.setEnabled(true);
                    return;
                }

                try {

                    // ===== CREATE FIREBASE AUTH ACCOUNT =====
                    org.json.JSONObject authResult =
                            project.Firebase_backend.User_backend.FirebaseAuthService
                                    .register(em, pw);

                    if (authResult.has("error")) {

                        String errorMessage =
                                authResult.getJSONObject("error")
                                        .getString("message");

                        JOptionPane.showMessageDialog(
                                AddAdminPanel.this,
                                "Registration failed:\n" + errorMessage,
                                "Auth Error",
                                JOptionPane.ERROR_MESSAGE
                        );

                        addBtn.setEnabled(true);
                        return;
                    }

                    String idToken = authResult.getString("idToken");
                    String uid = authResult.getString("localId");

                    project.Firebase_backend.User_backend.FirebaseAuthService
                            .updateDisplayName(idToken, fn + " " + ln);

                    // ===== CREATE USER OBJECT =====
                    User newUser = new User(
                            id,
                            selectedRole,
                            em,
                            fn,
                            ln
                    );

                    newUser.setUid(uid);

                    // ===== SAVE TO DATABASE =====
                    UserService.registerUserAsync(newUser, success -> {

                        if (!success) {
                            JOptionPane.showMessageDialog(
                                    AddAdminPanel.this,
                                    "Failed to save profile.",
                                    "Database Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            addBtn.setEnabled(true);
                            return;
                        }

                        JOptionPane.showMessageDialog(
                                AddAdminPanel.this,
                                selectedRole + " account successfully created!",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                        parent.reloadAdmins();
                        parent.closeAddAdmin();
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            AddAdminPanel.this,
                            "Something went wrong.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    addBtn.setEnabled(true);
                }
            });
        }

        @Override
        public void onCancelled(DatabaseError error) {
            addBtn.setEnabled(true);
        }
        public void clearFields() {

    
}

    });
    
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
            adminIdField.setText("");

            // Clear password fields safely
            Arrays.fill(password.getPassword(), '\0');
            Arrays.fill(confirmPassword.getPassword(), '\0');
            password.setText("");
            confirmPassword.setText("");

            // Reset preview labels
           

            // Clear image preview
            imgPreview.setIcon(null);
            selectedFile[0] = null;

            // Re-enable add button
            addBtn.setEnabled(true);
        });

        background.add(addBtn);
        background.add(closeBtn);
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

    private void applyAccountIdFormatter(JTextField field) {
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

    private void generateNextAdminId(String role, JTextField idField) {

    DatabaseReference usersRef =
            FirebaseDatabase.getInstance().getReference("users");

    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot snapshot) {

            int maxNumber = 0;
            String prefix = role.equals("ADMIN") ? "AD" : "LB";

            for (DataSnapshot child : snapshot.getChildren()) {

                User user = child.getValue(User.class);
                if (user == null) continue;

                if (!role.equalsIgnoreCase(user.getRole())) continue;

                String id = user.getId();

                if (id != null && id.startsWith(prefix)) {

                    try {
                        int number = Integer.parseInt(id.substring(2));
                        if (number > maxNumber) {
                            maxNumber = number;
                        }
                    } catch (Exception ignored) {}
                }
            }

            int nextNumber = maxNumber + 1;

            String formatted = String.format("%s%05d", prefix, nextNumber);

            idField.setText(formatted);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            System.err.println("Failed to generate ID");
        }
    });
}

private void generateNextId(String role, JTextField idField) {

    DatabaseReference usersRef =
            FirebaseDatabase.getInstance().getReference("users");

    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot snapshot) {

            int maxNumber = 0;
            String prefix = role.equals("ADMIN") ? "AD" : "LB";

            for (DataSnapshot child : snapshot.getChildren()) {

                User user = child.getValue(User.class);
                if (user == null) continue;

                if (!role.equalsIgnoreCase(user.getRole())) continue;

                String existingId = user.getId();

                if (existingId != null && existingId.matches(prefix + "\\d{5}")) {

                    try {
                        int number = Integer.parseInt(existingId.substring(2));
                        if (number > maxNumber) {
                            maxNumber = number;
                        }
                    } catch (Exception ignored) {}
                }
            }

            int nextNumber = maxNumber + 1;

            // SAME FORMAT AS VALIDATION
            String formattedId = String.format("%s%05d", prefix, nextNumber);

            idField.setText(formattedId);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            System.err.println("Failed generating ID");
        }
    });
}




}
