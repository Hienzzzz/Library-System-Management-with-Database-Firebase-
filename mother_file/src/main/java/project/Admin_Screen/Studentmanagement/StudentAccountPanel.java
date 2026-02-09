package project.Admin_Screen.Studentmanagement;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import project.Firebase_backend.Studnet_backend.Student;
import project.Firebase_backend.Studnet_backend.StudentService;
import project.Main_System.MainFrame;

public class StudentAccountPanel extends JPanel {

    private MainFrame frame;

    private JLayeredPane layeredPane;
    private JPanel dimOverlay;
    private AddStudentPanel addStudent;

    private JTextField searchField;
    private JComboBox<String> sortBox;
    private JButton addButton;

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    public StudentAccountPanel(MainFrame frame) {
        this.frame = frame;
        initUI();
        loadStudents();
    }

    private void initUI() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        // ================= LAYERED PANE =================
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1512, 982);
        add(layeredPane);

        // ================= BACKGROUND =================
        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_Student management_Account.png")
        );

        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        // ================= SEARCH FIELD =================
        String searchPlaceholder = "Search Book...";

        searchField = new JTextField(searchPlaceholder);
        searchField.setBounds(440, 284, 222, 27);
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Poppins", Font.PLAIN, 15));
        searchField.setBorder(null);
        background.add(searchField);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals(searchPlaceholder)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(searchPlaceholder);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        // ================= SORT BOX =================
        String[] sortOption = {
                "Default",
                "Newest",
                "Oldest",
                "A to Z",
                "Student ID",
                "Active",
                "Restricted",
                "Blocked"
        };

        sortBox = new JComboBox<>(sortOption);
        sortBox.setBounds(689, 286, 145, 24);
        sortBox.setFont(new Font("Sanchez", Font.PLAIN, 13));
        sortBox.setBackground(Color.WHITE);
        sortBox.setFocusable(false);
        sortBox.setSelectedItem(null);
        background.add(sortBox);

        sortBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 0),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        sortBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);

                if (index == -1 && value == null) {
                    setText("Sort by:");
                    setForeground(new Color(150, 150, 150));
                } else {
                    setText(value.toString());
                    setForeground(new Color(60, 60, 60));
                }
                return this;
            }
        });

        // ================= ADD BUTTON =================
        addButton = new JButton();
        addButton.setBounds(1335, 277, 36, 38);
        addButton.setBorder(null);
        addButton.setContentAreaFilled(false);
        background.add(addButton);

        // ================= TABLE =================
        String[] columns = {
                "Name",
                "Student ID",
                "Email",
                "Status",
                "Action"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setTableHeader(null);
        table.setBackground(Color.WHITE);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(300);
        columnModel.getColumn(1).setPreferredWidth(140);
        columnModel.getColumn(2).setPreferredWidth(250);
        columnModel.getColumn(3).setPreferredWidth(150);
        columnModel.getColumn(4).setPreferredWidth(150);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(371, 378, 1030, 550);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        background.add(scrollPane);

        // ================= DIM OVERLAY (BookManagement-style) =================
        dimOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.12f));
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };

        dimOverlay.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
        dimOverlay.setOpaque(false);
        dimOverlay.setVisible(false);
        dimOverlay.addMouseListener(new java.awt.event.MouseAdapter() {});
        layeredPane.add(dimOverlay, JLayeredPane.MODAL_LAYER);

        // ================= ADD STUDENT PANEL =================
        addStudent = new AddStudentPanel(this);
        addStudent.setSize(862, 678);;
        addStudent.setVisible(false);
        layeredPane.add(addStudent, JLayeredPane.POPUP_LAYER);

        SwingUtilities.invokeLater(() -> {
            int x = (layeredPane.getWidth() - addStudent.getWidth()) / 2;
            int y = (layeredPane.getHeight() - addStudent.getHeight()) / 2;
            addStudent.setLocation(x, y);
        });


        int panelWidth = 862;
        int panelHeight = 678;

        int x = (layeredPane.getWidth() - panelWidth) / 2;
        int y = (layeredPane.getHeight() - panelHeight) / 2;

        addStudent.setBounds(x, y, panelWidth, panelHeight);


        // ================= ADD BUTTON ACTION =================
        addButton.addActionListener(e -> {
            if (addStudent.isVisible()) return;

            searchField.setEnabled(false);
            sortBox.setEnabled(false);
            addButton.setEnabled(false);
            sortBox.hidePopup();

            dimOverlay.setVisible(true);
            addStudent.setVisible(true);
        });

        // ================= SEARCH FILTER =================
        searchField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {

                    private void filter() {
                        String text = searchField.getText().trim();
                        if (text.isEmpty() || text.equalsIgnoreCase(searchPlaceholder)) {
                            sorter.setRowFilter(null);
                        } else {
                            sorter.setRowFilter(RowFilter.regexFilter(
                                    "(?i)" + text, 0, 1, 2));
                        }
                    }

                    public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
                    public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
                    public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }
                }
        );
    }

    // ================= FIREBASE LOAD =================
    private void loadStudents() {

        StudentService.getRef()
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        SwingUtilities.invokeLater(() -> {
                            model.setRowCount(0);

                            for (DataSnapshot data : snapshot.getChildren()) {
                                Student s = data.getValue(Student.class);
                                if (s == null) continue;

                                model.addRow(new Object[]{
                                        s.getFirstName() + " " + s.getSurname(),
                                        s.getId(),
                                        s.getEmail(),
                                        s.getStatus(),
                                        "Remove"
                                });
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.out.println("Student load error: " + error.getMessage());
                    }
                });
    }

    // ================= CLOSE MODAL =================
    public void closeAddStudent() {
        addStudent.setVisible(false);
        dimOverlay.setVisible(false);

        searchField.setEnabled(true);
        sortBox.setEnabled(true);
        addButton.setEnabled(true);
    }
}
