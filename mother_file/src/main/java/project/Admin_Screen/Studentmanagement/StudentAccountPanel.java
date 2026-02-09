package project.Admin_Screen.Studentmanagement;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.List;

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
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
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

    private final MainFrame frame;

    private JLayeredPane layeredPane;
    private JPanel dimOverlay;
    private AddStudentPanel addStudent;

    private JTextField searchField;
    private JComboBox<String> sortBox;
    private JButton addButton;

    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    private RowFilter<DefaultTableModel, Object> searchFilter;
    private RowFilter<DefaultTableModel, Object> statusFilter;

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
        JLabel background = new JLabel(
            new ImageIcon(getClass().getResource(
                "/Images/Admin_Student management_Account.png"))
        );
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        // ================= SEARCH FIELD =================
        String searchPlaceholder = "Search Student...";
        searchField = new JTextField(searchPlaceholder);
        searchField.setBounds(440, 284, 222, 27);
        searchField.setFont(new Font("Poppins", Font.PLAIN, 15));
        searchField.setForeground(Color.GRAY);
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
        String[] sortOptions = {
            "Default",
            "Newest",
            "Oldest",
            "A to Z",
            "Student ID",
            "Active",
            "Restricted",
            "Blocked"
        };

        sortBox = new JComboBox<>(sortOptions);
        sortBox.setBounds(689, 286, 145, 24);
        sortBox.setFont(new Font("Sanchez", Font.PLAIN, 13));
        sortBox.setBackground(Color.WHITE);
        sortBox.setFocusable(false);
        sortBox.setSelectedItem(null);
        background.add(sortBox);

        sortBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));

        sortBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrow = new JButton();
                arrow.setBorder(null);
                arrow.setContentAreaFilled(false);
                arrow.setFocusPainted(false);

                ImageIcon icon = new ImageIcon(
                    getClass().getResource("/Images/down-chevron.png")
                );

                arrow.setIcon(new ImageIcon(
                    icon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH)
                ));
                return arrow;
            }
        });

        sortBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);

                setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));

                if (index == -1 && value == null) {
                    setText("Sort by:");
                    setForeground(new Color(150, 150, 150));
                } else if (index == -1) {
                    setText("Sort by: " + value);
                } else {
                    setText(value.toString());
                    setBackground(isSelected
                        ? new Color(40, 70, 140)
                        : Color.WHITE);
                    setForeground(isSelected
                        ? Color.WHITE
                        : new Color(60, 60, 60));
                }
                return this;
            }
        });

        // ================= TABLE =================
        model = new DefaultTableModel(
            new String[]{"Name", "Student ID", "Email", "Status", "Action"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return col == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setTableHeader(null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setBackground(Color.WHITE);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        TableColumnModel cols = table.getColumnModel();
        cols.getColumn(0).setPreferredWidth(300);
        cols.getColumn(1).setPreferredWidth(140);
        cols.getColumn(2).setPreferredWidth(250);
        cols.getColumn(3).setPreferredWidth(150);
        cols.getColumn(4).setPreferredWidth(150);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 1; i <= 4; i++) {
            cols.getColumn(i).setCellRenderer(center);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(371, 378, 1030, 550);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        background.add(scrollPane);

        // ================= SORT LOGIC =================
        sortBox.addActionListener(e -> {
            statusFilter = null;
            String opt = (String) sortBox.getSelectedItem();
            if (opt == null) return;

            switch (opt) {
                case "Default" -> sorter.setSortKeys(null);
                case "Newest" ->
                    sorter.setSortKeys(List.of(
                        new RowSorter.SortKey(1, SortOrder.DESCENDING)));
                case "Oldest" ->
                    sorter.setSortKeys(List.of(
                        new RowSorter.SortKey(1, SortOrder.ASCENDING)));
                case "A to Z" ->
                    sorter.setSortKeys(List.of(
                        new RowSorter.SortKey(0, SortOrder.ASCENDING)));
                case "Student ID" ->
                    sorter.setSortKeys(List.of(
                        new RowSorter.SortKey(1, SortOrder.ASCENDING)));
                case "Active", "Restricted", "Blocked" ->
                    statusFilter =
                        RowFilter.regexFilter("^" + opt + "$", 3);
            }
            applyFilters();
        });

        // ================= SEARCH FILTER =================
        searchField.getDocument().addDocumentListener(
            new javax.swing.event.DocumentListener() {
                private void filter() {
                    String text = searchField.getText().trim();
                    if (text.isEmpty()
                        || text.equalsIgnoreCase(searchPlaceholder)) {
                        searchFilter = null;
                    } else {
                        searchFilter = RowFilter.regexFilter(
                            "(?i)" + text, 0, 1, 2);
                    }
                    applyFilters();
                }
                public void insertUpdate(javax.swing.event.DocumentEvent e){filter();}
                public void removeUpdate(javax.swing.event.DocumentEvent e){filter();}
                public void changedUpdate(javax.swing.event.DocumentEvent e){filter();}
            });

        // ================= ADD BUTTON =================
        addButton = new JButton();
        addButton.setBounds(1335, 277, 36, 38);
        addButton.setBorder(null);
        addButton.setContentAreaFilled(false);
        background.add(addButton);

        // ================= DIM OVERLAY =================
        dimOverlay = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.12f));
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        dimOverlay.setBounds(0, 0, 1512, 982);
        dimOverlay.setOpaque(false);
        dimOverlay.setVisible(false);
        layeredPane.add(dimOverlay, JLayeredPane.MODAL_LAYER);

        // ================= ADD STUDENT PANEL =================
        addStudent = new AddStudentPanel(this);
        addStudent.setSize(862, 678);
        addStudent.setVisible(false);
        layeredPane.add(addStudent, JLayeredPane.POPUP_LAYER);

        addButton.addActionListener(e -> {
            if (addStudent.isVisible()) return;
            searchField.setEnabled(false);
            sortBox.setEnabled(false);
            addButton.setEnabled(false);
            dimOverlay.setVisible(true);
            addStudent.setLocation(
                (layeredPane.getWidth() - addStudent.getWidth()) / 2,
                (layeredPane.getHeight() - addStudent.getHeight()) / 2
            );
            addStudent.setVisible(true);
        });
    }

    private void applyFilters() {
        if (searchFilter != null && statusFilter != null) {
            sorter.setRowFilter(
                RowFilter.andFilter(List.of(searchFilter, statusFilter)));
        } else if (searchFilter != null) {
            sorter.setRowFilter(searchFilter);
        } else {
            sorter.setRowFilter(statusFilter);
        }
    }

    private void loadStudents() {
        StudentService.getRef().addValueEventListener(
            new ValueEventListener() {
                public void onDataChange(DataSnapshot snap) {
                    SwingUtilities.invokeLater(() -> {
                        model.setRowCount(0);
                        for (DataSnapshot d : snap.getChildren()) {
                            Student s = d.getValue(Student.class);
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
                public void onCancelled(DatabaseError err) {
                    System.out.println(err.getMessage());
                }
            });
    }

    public void closeAddStudent() {
        addStudent.setVisible(false);
        dimOverlay.setVisible(false);
        searchField.setEnabled(true);
        sortBox.setEnabled(true);
        addButton.setEnabled(true);
    }
}
