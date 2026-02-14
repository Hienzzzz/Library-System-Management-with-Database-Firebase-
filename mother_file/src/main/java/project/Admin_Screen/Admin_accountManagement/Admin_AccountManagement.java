package project.Admin_Screen.Admin_accountManagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.Admin_Screen.Bookmanagement.BookManagement;
import project.Admin_Screen.Dashboard.AdminDashboard;
import project.Admin_Screen.Report_screen.Reports;
import project.Admin_Screen.Studentmanagement.StudentManagement;
import project.Firebase_backend.User_backend.User;
import project.Main_System.MainFrame;

public class Admin_AccountManagement extends JPanel {

    private MainFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JButton addButton;
    private AddAdminPanel addPanel;

    public Admin_AccountManagement(MainFrame frame) {
        this.frame = frame;
        initUI();
        loadAdmins();
    }

    private void initUI() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_admin management.png")
        );

        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);
        add(background);

        // ================= SIDE BUTTONS =================

        background.add(createNavButton("Dashboard", 240,
                () -> frame.setContentPane(new AdminDashboard(frame))));

        background.add(createNavButton("Reports", 297,
                () -> frame.setContentPane(new Reports(frame))));

        background.add(createNavButton("Book Management", 350,
                () -> frame.setContentPane(new BookManagement(frame))));

        background.add(createNavButton("Student Management", 403,
                () -> frame.setContentPane(new StudentManagement(frame))));

        background.add(createNavButton("Admin Management", 457,
                () -> frame.setContentPane(new Admin_AccountManagement(frame))));

        // ================= TABLE =================

        model = new DefaultTableModel(
                new String[]{"Name", "Admin ID", "Email", "Status", "Action"}, 0) {

            public boolean isCellEditable(int r, int c) {
                return c == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Action Button Renderer
        table.getColumnModel().getColumn(4)
                .setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) {

                        JButton btn = new JButton("•••");
                        btn.setFocusPainted(false);
                        btn.setBorderPainted(false);
                        btn.setContentAreaFilled(false);
                        return btn;
                    }
                });

        // Action Button Editor
        table.getColumnModel().getColumn(4)
                .setCellEditor(new ActionButtonEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(371, 378, 1030, 550);
        scrollPane.setBorder(null);
        background.add(scrollPane);

        // ================= ADD BUTTON =================

        addButton = new JButton();
        addButton.setBounds(1335, 277, 36, 38);
        addButton.setBorder(null);
        addButton.setContentAreaFilled(false);
        addButton.setFocusPainted(false);
        background.add(addButton);

        addButton.addActionListener(e -> {

            if (addPanel != null) return; // prevent stacking

            addPanel = new AddAdminPanel(this);
            addPanel.setBounds(325, 150, 862, 678);
            add(addPanel);
            setComponentZOrder(addPanel, 0);
            repaint();
        });
    }

    // ================= LOAD ADMINS =================

    private void loadAdmins() {

        DatabaseReference usersRef =
                FirebaseDatabase.getInstance().getReference("users");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                SwingUtilities.invokeLater(() -> {

                    model.setRowCount(0);

                    for (DataSnapshot child : snapshot.getChildren()) {

                        User user = child.getValue(User.class);
                        if (user == null) continue;

                        if (!"ADMIN".equalsIgnoreCase(user.getRole())
                                && !"LIBRARIAN".equalsIgnoreCase(user.getRole()))
                            continue;

                        if ("DELETED".equalsIgnoreCase(user.getStatus()))
                            continue;

                        model.addRow(new Object[]{
                                user.getFirstName() + " " + user.getLastName(),
                                user.getId(),
                                user.getEmail(),
                                user.getStatus(),
                                "•••"
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Failed to load admins: " + error.getMessage());
            }
        });
    }

    // ================= SHOW DETAILS =================

  private void showAdminDetails(int row) {

    String adminId = model.getValueAt(row, 1).toString();

    DatabaseReference userRef =
            FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(adminId);

    userRef.addListenerForSingleValueEvent(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot snapshot) {

            User user = snapshot.getValue(User.class);
            if (user == null) return;

            // Remove existing detail panels
            for (Component comp : getComponents()) {
                if (comp instanceof AdminDetailPanel) {
                    remove(comp);
                }
            }

            final AdminDetailPanel[] holder = new AdminDetailPanel[1];

            holder[0] = new AdminDetailPanel(
                    Admin_AccountManagement.this,
                    user,
                    () -> {
                        remove(holder[0]);
                        reloadAdmins();
                        repaint();
                    }
            );

            holder[0].setBounds(325, 150, 862, 678);
            add(holder[0]);
            setComponentZOrder(holder[0], 0);
            repaint();
        }

        @Override
        public void onCancelled(DatabaseError error) {
            System.err.println("Failed loading admin");
        }
    });
}


    // ================= CELL EDITOR =================

    class ActionButtonEditor extends DefaultCellEditor {

        private JButton button;
        private int selectedRow;

        public ActionButtonEditor() {
            super(new JTextField());

            button = new JButton("•••");
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false);

            button.addActionListener(e -> {
                fireEditingStopped();
                showAdminDetails(selectedRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int row, int column) {

            selectedRow = table.convertRowIndexToModel(row);
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "•••";
        }
    }

    // ================= NAV BUTTON =================

    private JButton createNavButton(String text, int y, Runnable action) {

        JButton btn = new JButton(text);
        btn.setBounds(12, y, 238, 49);
        btn.setFont(MainFrame.loadSanchez(15f));
        btn.setForeground(new Color(93, 93, 93));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 60, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);

        btn.addActionListener(e -> {
            action.run();
            frame.revalidate();
        });

        return btn;
    }

    // ================= PUBLIC METHODS =================

    public void reloadAdmins() {
        loadAdmins();
    }

    public void closeAddAdmin() {
        if (addPanel != null) {
            remove(addPanel);
            addPanel = null;
            reloadAdmins();
            repaint();
        }
    }
}
