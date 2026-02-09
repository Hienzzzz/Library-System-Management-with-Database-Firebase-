package project.Admin_Screen.Studentmanagement;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import project.Firebase_backend.Studnet_backend.Student;
import project.Firebase_backend.Studnet_backend.StudentService;
import project.Main_System.MainFrame;

public class StudentManagement extends JPanel {

    private MainFrame frame;
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;
    private JLayeredPane layeredPane;
    private AddStudentPanel addStudent;
    private JComboBox<String> sortBox;
    private JPanel dimOverlay;
    private int hoveredRow = -1;
    private boolean tableHasFocus = false;
    private TableRowSorter<DefaultTableModel> sorter;

    public StudentManagement(MainFrame frame) {
        this.frame = frame;
        initUI();
        loadStudentsFromFirebase();
    }

    private void initUI() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1512, 982);

        JLabel background = new JLabel(
            new ImageIcon(getClass().getResource("/Images/Admin_Student management.png"))
        );
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        // ================= DIM OVERLAY =================
        dimOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.15f));
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        dimOverlay.setBounds(0, 0, 1512, 982);
        dimOverlay.setOpaque(false);
        dimOverlay.setVisible(false);

        // ================= TABLE =================
        String[] columns = {
            "Name",
            "Student ID",
            "Email",
            "Penalty",
            "Borrowed",
            "Status"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBackground(Color.WHITE);

        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(300);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(260);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        table.getColumnModel().getColumn(5).setPreferredWidth(90);

        // Center numeric + status
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(center);
        table.getColumnModel().getColumn(3).setCellRenderer(center);
        table.getColumnModel().getColumn(4).setCellRenderer(center);
        table.getColumnModel().getColumn(5).setCellRenderer(center);

        table.setDefaultRenderer(Object.class, new CustomCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(371, 510, 1030, 412);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        background.add(scrollPane);

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(180, 180, 180);
                trackColor = new Color(245, 245, 245);
            }
        });

        // ================= SEARCH =================
        searchField = new JTextField("Search student...");
        searchField.setBounds(436, 415, 228, 27);
        searchField.setForeground(Color.GRAY);
        searchField.setBorder(null);
        background.add(searchField);

        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search student...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search student...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String txt = searchField.getText().trim();
                if (txt.isEmpty() || txt.equalsIgnoreCase("Search student...")) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(
                        "(?i)" + Pattern.quote(txt), 0, 1, 2
                    ));
                }
            }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });

        // ================= ADD STUDENT =================
        addStudent = new AddStudentPanel(this);
        addStudent.setBounds(439, 270, 762, 587);
        addStudent.setVisible(false);

        JButton addBtn = new JButton();
        addBtn.setBounds(1335, 409, 36, 40);
        addBtn.setContentAreaFilled(false);
        addBtn.setBorder(null);
        addBtn.addActionListener(e -> {
            dimOverlay.setVisible(true);
            addStudent.setVisible(true);
            layeredPane.moveToFront(addStudent);
        });

        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(dimOverlay, JLayeredPane.MODAL_LAYER);
        layeredPane.add(addStudent, JLayeredPane.POPUP_LAYER);
        layeredPane.add(addBtn, JLayeredPane.PALETTE_LAYER);

        add(layeredPane);
    }

    public void closeAddStudent() {
        addStudent.setVisible(false);
        dimOverlay.setVisible(false);
    }

    // ================= FIREBASE =================
   private void loadStudentsFromFirebase() {

    StudentService.getRef().addValueEventListener(new ValueEventListener() {

        @Override
        public void onDataChange(DataSnapshot snapshot) {

            SwingUtilities.invokeLater(() -> {
                model.setRowCount(0);

                for (DataSnapshot child : snapshot.getChildren()) {

                    Student student = child.getValue(Student.class);
                    if (student == null) continue;

                    String fullName =
                        student.getFirstname() + " " + student.getSurname();

                    String status = student.getStatus();
                    if (status == null || status.isEmpty()) {
                        status = Student.STATUS_ACTIVE;
                    }

                    model.addRow(new Object[] {
                        fullName,
                        student.getId(),
                        student.getEmail(),
                        student.getPenaltyAmount(),     // ✅ correct
                        student.getBorrowedCount(),     // ✅ correct
                        status.toLowerCase()            // for renderer
                    });
                }
            });
        }

        @Override
        public void onCancelled(DatabaseError error) {
            JOptionPane.showMessageDialog(
                StudentManagement.this,
                "Failed to load students:\n" + error.getMessage(),
                "Firebase Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    });
}


    // ================= CELL RENDERER =================
    class CustomCellRenderer extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean sel,
                boolean focus, int row, int column) {

            super.getTableCellRendererComponent(table, value, false, false, row, column);

            setBackground(row % 2 == 0 ? new Color(245,245,245) : Color.WHITE);

            if (column== 5 && value != null) {
                switch (value.toString()) {
                    case "active":
                        setForeground(new Color(46, 160, 67)); // green
                        break;
                    case "restricted":
                        setForeground(new Color(255, 140, 0)); // orange
                        break;
                    case "blocked":
                        setForeground(new Color(220, 53, 69)); // red
                        break;
                    default:
                        setForeground(Color.GRAY);
                }
            }


            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setVerticalAlignment(SwingConstants.CENTER);
            return this;
        }
    }
}
