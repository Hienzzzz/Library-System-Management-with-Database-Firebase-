package project.Admin_Screen.Studentmanagement;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.Admin_Screen.Admin_accountManagement.Admin_AccountManagement;
import project.Admin_Screen.Bookmanagement.BookManagement;
import project.Admin_Screen.Dashboard.AdminDashboard;
import project.Admin_Screen.Report_screen.Reports;
import project.Firebase_backend.User_backend.User;
import project.Main_System.MainFrame;

public class StudentAccountPanel extends JPanel {

    private final MainFrame frame;

    private JLayeredPane layeredPane;
    private JPanel dimOverlay;
    private AddStudentPanel addStudent;

    private JTextField searchField;
    private JComboBox<String> sortBox;
    private JButton addButton;
    private int hoveredRow = -1;
    private boolean tableHasFocus = false;


    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    

    private RowFilter<DefaultTableModel, Object> searchFilter;

    public StudentAccountPanel(MainFrame frame) {
        this.frame = frame;
        initUI();
        loadStudents();
    }

    private void initUI() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1512, 982);
        add(layeredPane);

        ImageIcon icon = new ImageIcon(
            getClass().getResource("/Images/Admin_Student_accountM.png")

            );
        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);

        
        // ============================= buttons ====================================
         TButton dashboard = new TButton("Dashboard");
        dashboard.setBounds(12, 240, 238, 49);
        dashboard.setFont(MainFrame.loadSanchez(15f));
        dashboard.setForeground(new Color(93, 93, 93));
        dashboard.setHorizontalAlignment(SwingConstants.LEFT);
        dashboard.setMargin(new Insets(0, 60, 0, 0));
        dashboard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dashboard.addActionListener(e -> {
            frame.setContentPane(new AdminDashboard(frame));
            frame.revalidate();
        });

        TButton reports = new TButton("Reports");
        reports.setBounds(12, 297, 238, 49);
        reports.setFont(MainFrame.loadSanchez(15f));
        reports.setForeground(new Color(93, 93, 93));
        reports.setHorizontalAlignment(SwingConstants.LEFT);
        reports.setMargin(new Insets(0, 60, 0, 0));
        reports.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reports.addActionListener(e -> {
            frame.setContentPane(new Reports(frame));
            frame.revalidate();
        });

        TButton bookManagement = new TButton("Book Management");
        bookManagement.setBounds(12, 350, 238, 49);
        bookManagement.setFont(MainFrame.loadSanchez(15f));
        bookManagement.setForeground(new Color(93, 93, 93));
        bookManagement.setHorizontalAlignment(SwingConstants.LEFT);
        bookManagement.setMargin(new Insets(0, 60, 0, 0));
        bookManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookManagement.addActionListener(e -> {
            frame.setContentPane(new BookManagement(frame));
            frame.revalidate();
        });

        TButton studentM = new TButton("Student Management");
        studentM.setBounds(12, 403, 238, 49);
        studentM.setFont(MainFrame.loadSanchez(15f));
        studentM.setForeground(new Color(93, 93, 93));
        studentM.setHorizontalAlignment(SwingConstants.LEFT);
        studentM.setMargin(new Insets(0, 60, 0, 0));
        studentM.setCursor(new Cursor(Cursor.HAND_CURSOR));
        studentM.addActionListener(e -> {
            frame.setContentPane(new StudentManagement(frame));
            frame.revalidate();
        });

        TButton accountM = new TButton("Admin Management");
        accountM.setBounds(12, 592, 238, 49);
        accountM.setFont(MainFrame.loadSanchez(15f));
        accountM.setForeground(new Color(93, 93, 93));
        accountM.setHorizontalAlignment(SwingConstants.LEFT);
        accountM.setMargin(new Insets(0, 60, 0, 0));
        accountM.setCursor(new Cursor(Cursor.HAND_CURSOR));
        accountM.addActionListener(e -> {
            frame.setContentPane(new Admin_AccountManagement(frame));
            frame.revalidate();
        });

        // ================= STUDENT MANAGEMENT SUB BUTTONS =================

        TButton smAccount = new TButton("Account");
        smAccount.setBounds(40, 468, 220, 35);
        smAccount.setFont(MainFrame.loadSanchez(13f));
        smAccount.setForeground(new Color(93, 93, 93));
        smAccount.setHorizontalAlignment(SwingConstants.LEFT);
        smAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        smAccount.setMargin(new Insets(0, 58, 0, 0));
        smAccount.addActionListener(e -> {
            frame.setContentPane(new StudentAccountPanel(frame));
            frame.revalidate();
        });

        TButton smActiveBorrower = new TButton("Active Borrower");
        smActiveBorrower.setBounds(40, 503, 220, 35);
        smActiveBorrower.setFont(MainFrame.loadSanchez(13f));
        smActiveBorrower.setForeground(new Color(93, 93, 93));
        smActiveBorrower.setHorizontalAlignment(SwingConstants.LEFT);
        smActiveBorrower.setMargin(new Insets(0, 58, 0, 0));
        smActiveBorrower.setCursor(new Cursor(Cursor.HAND_CURSOR));
        smActiveBorrower.addActionListener(e -> {
            frame.setContentPane(new Activeborrower(frame));
            frame.revalidate();
        });
        TButton Offense = new TButton("Offenses");
        Offense.setBounds(40, 540, 220, 35);
        Offense.setFont(MainFrame.loadSanchez(13f));
        Offense.setForeground(new Color(93, 93, 93));
        Offense.setHorizontalAlignment(SwingConstants.LEFT);
        Offense.setMargin(new Insets(0, 58, 0, 0));
        Offense.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Offense.addActionListener(e -> {
            frame.setContentPane(new Offenses(frame));
            frame.revalidate();
        });

        background.add(dashboard);
        background.add(reports);
        background.add(bookManagement);
        background.add(studentM);
        background.add(accountM);
        background.add(Offense);
        background.add(smAccount);
        background.add(smActiveBorrower);
        // ============================= buttons ====================================
       
       
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

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String text = searchField.getText().trim();
                if (text.isEmpty() || text.equalsIgnoreCase(searchPlaceholder)) {
                    searchFilter = null;
                } else {
                    searchFilter = RowFilter.regexFilter("(?i)" + text, 0, 1, 2);
                }
                applyFilters();
            }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });

        sortBox = new JComboBox<>(new String[]{
            "Default",
            "Newest",
            "Oldest",
            "A to Z",
            "Student ID",
            "Active",
            "Restricted",
            "Blocked"
        });
        sortBox.setBounds(685, 285, 150, 24);
        sortBox.setFont(new Font("Sanchez", Font.PLAIN, 13));
        sortBox.setBackground(Color.WHITE);
        sortBox.setForeground(new Color(60, 60, 60));
        sortBox.setFocusable(false);
        sortBox.setOpaque(false);
        sortBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 0),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        sortBox.setSelectedItem(null);

        sortBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (index == -1 && value == null) {
                    setText("Sort by:");
                    setForeground(new Color(150, 150, 150));
                } else if (index == -1) {
                    setText("Sort by: " + value);
                    setForeground(new Color(60, 60, 60));
                } else {
                    setText(value.toString());
                    setForeground(new Color(60, 60, 60));
                }
                return this;
            }
        });

        sortBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton b = new JButton();
                b.setContentAreaFilled(false);
                b.setBorder(null);
                b.setFocusPainted(false);
                b.setOpaque(false);
                ImageIcon icon = new ImageIcon(
                    getClass().getResource("/Images/down-chevron.png")
                );
                Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                b.setIcon(new ImageIcon(img));
                return b;
            }
        });

        sortBox.addActionListener(e -> applySorting());

        sortBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JComboBox<?> box = (JComboBox<?>) e.getSource();
                    JPopupMenu popup =
                        (JPopupMenu) box.getAccessibleContext().getAccessibleChild(0);
                    if (popup != null && popup.getComponentCount() > 0) {
                        JScrollPane sp = (JScrollPane) popup.getComponent(0);
                        JScrollBar vBar = sp.getVerticalScrollBar();
                        vBar.setUI(new ModernScrollBarUI());
                        vBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));


                    }
                });
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {}
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
        });

        background.add(sortBox);

        model = new DefaultTableModel(
            new String[]{"Name", "Student ID", "Email", "Status", "Action"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return c == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setBackground(Color.WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFocusable(true);

        table.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row != hoveredRow) {
                    hoveredRow = row;
                    table.repaint();
                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            public void mouseExited(MouseEvent e) {
                hoveredRow = -1;
                table.repaint();
            }
        });

        table.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                tableHasFocus = true;
                table.repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                tableHasFocus = false;
                hoveredRow = -1;
                table.clearSelection();
                table.repaint();
            }
        });



        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        TableColumnModel cols = table.getColumnModel();
        cols.getColumn(0).setPreferredWidth(300);
        cols.getColumn(1).setPreferredWidth(140);
        cols.getColumn(2).setPreferredWidth(250);
        cols.getColumn(3).setPreferredWidth(150);
        cols.getColumn(4).setPreferredWidth(150);

        table.getColumnModel().getColumn(4)
            .setCellRenderer(new ActionButtonRenderer());

        table.getColumnModel().getColumn(4)
            .setCellEditor(new ActionButtonEditor(this));

        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            private final Color HOVER_COLOR = new Color(230, 240, 255);
            private final Color SELECT_COLOR = new Color(200, 220, 255);
            private final Color EVEN_ROW = new Color(245, 245, 245);
            private final Color ODD_ROW = Color.WHITE;

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int column) {

                super.getTableCellRendererComponent(
                        table, value, false, false, row, column
                );

                int modelRow = table.convertRowIndexToModel(row);

                setBackground(modelRow % 2 == 0 ? EVEN_ROW : ODD_ROW);
                setForeground(Color.BLACK);

                if (row == hoveredRow && tableHasFocus) {
                    setBackground(HOVER_COLOR);
                }

                if (table.getSelectedRow() == row && tableHasFocus) {
                    setBackground(SELECT_COLOR);
                }

                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
                setVerticalAlignment(SwingConstants.CENTER);

                return this;
            }
        });


        for (int i = 0; i < table.getColumnCount(); i++) {
            int w = table.getColumnModel().getColumn(i).getPreferredWidth();
            table.getColumnModel().getColumn(i).setMinWidth(w);
            table.getColumnModel().getColumn(i).setMaxWidth(w);
        }



       

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(371, 378, 1030, 550);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        // Apply BookManagement scrollbar style
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setUI(new ModernScrollBarUI());
        vBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));

        background.add(scrollPane);

        // ================= HEADER (HIDDEN LIKE BOOK MANAGEMENT) =================
        table.setTableHeader(null);
        scrollPane.setColumnHeaderView(null);



        addButton = new JButton();
        addButton.setBounds(1335, 277, 36, 38);
        addButton.setBorder(null);
        addButton.setContentAreaFilled(false);
        background.add(addButton);

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

        dimOverlay.setBounds(0, 0, 1512, 982);
        dimOverlay.setOpaque(false);
        dimOverlay.setVisible(false);
        dimOverlay.addMouseListener(new MouseAdapter() {});


        layeredPane.add(dimOverlay, JLayeredPane.MODAL_LAYER);


        addStudent = new AddStudentPanel(this);
        addStudent.setSize(862, 678);
        addStudent.setVisible(false);
        layeredPane.add(addStudent, JLayeredPane.POPUP_LAYER);

        addButton.addActionListener(e -> {
            if (addStudent.isVisible()) return;

            setBackgroundEnabled(false);

            dimOverlay.setVisible(true);
            addStudent.setLocation(
                (layeredPane.getWidth() - addStudent.getWidth()) / 2,
                (layeredPane.getHeight() - addStudent.getHeight()) / 2
            );
            addStudent.setVisible(true);
        });

    }

    private void applySorting() {

    Object selected = sortBox.getSelectedItem();

    // Reset sorting if nothing selected
    if (selected == null) {
        sorter.setSortKeys(null);
        sorter.sort();
        applyFilters();
        return;
    }

    String option = selected.toString();

    // STATUS = FILTER ONLY
    if (option.equals("Active")
            || option.equals("Restricted")
            || option.equals("Blocked")) {

        sorter.setSortKeys(null);
        sorter.sort();
        applyFilters();
        return;
    }

    List<RowSorter.SortKey> keys = new ArrayList<>();

    switch (option) {
        case "Default":
            sorter.setSortKeys(null);
            sorter.sort();
            break;

        case "Newest":
            keys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
            sorter.setSortKeys(keys);
            break;

        case "Oldest":
            keys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
            sorter.setSortKeys(keys);
            break;

        case "A to Z":
            keys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
            sorter.setSortKeys(keys);
            break;

        case "Student ID":
            keys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
            sorter.setSortKeys(keys);
            break;
    }

    sorter.sort();       // 🔑 force refresh
    applyFilters();      // 🔑 reapply filters
}


    // ===== FIX: replace applyFilters() with this EXACT version =====

private void applyFilters() {

    List<RowFilter<DefaultTableModel, Object>> filters = new ArrayList<>();

    // Search filter
    if (searchFilter != null) {
        filters.add(searchFilter);
    }

    // Status filter
    Object selected = sortBox.getSelectedItem();
    if (selected != null) {
        String opt = selected.toString();

        if (opt.equals("Active") || opt.equals("Restricted") || opt.equals("Blocked")) {
            filters.add(
                RowFilter.regexFilter("(?i)^\\s*" + opt + "\\s*$", 3)
            );
        }
    }

    sorter.setRowFilter(
        filters.isEmpty()
            ? null
            : RowFilter.andFilter(filters)
    );

    sorter.sort(); // 🔑 force refresh
}




        private void loadStudents() {

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

                            // ✅ Only show students
                           if (!"STUDENT".equals(user.getRole())) continue;

                            if ("DELETED".equalsIgnoreCase(user.getStatus())) continue;


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
                    System.err.println("Failed to load students: " + error.getMessage());
                }
            });
        }


    public void closeAddStudent() {

        addStudent.setVisible(false);
        dimOverlay.setVisible(false);

        layeredPane.remove(addStudent);
        layeredPane.repaint();
        layeredPane.revalidate();

        // recreate panel fresh (prevents stale state)
        addStudent = new AddStudentPanel(this);
        addStudent.setSize(862, 678);
        addStudent.setVisible(false);
        layeredPane.add(addStudent, JLayeredPane.POPUP_LAYER);

        setBackgroundEnabled(true);
    }
    public void reloadStudents() {
        loadStudents();
    }




    private static class ModernScrollBarUI extends BasicScrollBarUI {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(180, 180, 180);
            trackColor = new Color(245, 245, 245);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createZeroButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createZeroButton();
        }

        private JButton createZeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            b.setMinimumSize(new Dimension(0, 0));
            b.setMaximumSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(
                    r.x + 2, r.y + 2,
                    r.width - 4, r.height - 4,
                    10, 10
            );
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(trackColor);
            g.fillRect(r.x, r.y, r.width, r.height);
        }
    }
    


    private void setBackgroundEnabled(boolean enabled) {
        searchField.setEnabled(enabled);
        sortBox.setEnabled(enabled);
        table.setEnabled(enabled);
        addButton.setEnabled(enabled);

        if (!enabled) {
            sortBox.hidePopup(); // close dropdown if open
            table.clearSelection();
        }
    }

            // =================button renderer ==============================

    class ActionButtonRenderer extends JButton
            implements javax.swing.table.TableCellRenderer {

        public ActionButtonRenderer() {
            setText("•••");
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(true);
            setOpaque(true);
            setFont(new Font("Poppins", Font.BOLD, 18));
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            int modelRow = table.convertRowIndexToModel(row);

            Color even = new Color(245, 245, 245);
            Color odd = Color.WHITE;
            Color hover = new Color(230, 240, 255);
            Color select = new Color(200, 220, 255);

            setBackground(modelRow % 2 == 0 ? even : odd);
            setForeground(Color.BLACK);

            if (table.getSelectedRow() == row && table.isEnabled()) {
                setBackground(select);
            }

            return this;
        }

        
    }

            // =================button editor ==============================

    class ActionButtonEditor extends javax.swing.DefaultCellEditor {

        private JButton button;
        private int selectedRow;
        private StudentAccountPanel parent;

        public ActionButtonEditor(StudentAccountPanel parent) {
            super(new JTextField());
            this.parent = parent;

            button = new JButton("•••");
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(true);
            button.setOpaque(true);
            button.setFont(new Font("Poppins", Font.BOLD, 18));

            button.addActionListener(e -> {
                fireEditingStopped();
                parent.showStudentDetails(selectedRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected, int viewRow, int column) {

            selectedRow = table.convertRowIndexToModel(viewRow);
            button.setBackground(viewRow % 2 == 0
                    ? new Color(245, 245, 245)
                    : Color.WHITE);

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "•••";
        }
    }

        // =================show book details ==============================
        public void showStudentDetails(int row) {

            String studentId = model.getValueAt(row, 1).toString();

            DatabaseReference userRef =
                    FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(studentId);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    User user = snapshot.getValue(User.class);
                    if (user == null) return;

                    SwingUtilities.invokeLater(() -> {

                        setBackgroundEnabled(false);
                        dimOverlay.setVisible(true);
                        dimOverlay.requestFocusInWindow();

                        final StudentDetailsPanel[] holder =
                                new StudentDetailsPanel[1];

                        holder[0] = new StudentDetailsPanel(
                                StudentAccountPanel.this,
                                user, // 🔥 PASS USER INSTEAD OF STUDENT
                                () -> {
                                    layeredPane.remove(holder[0]);
                                    dimOverlay.setVisible(false);
                                    setBackgroundEnabled(true);
                                    layeredPane.repaint();
                                }
                        );

                        holder[0].setBounds(
                                (layeredPane.getWidth() - holder[0].getWidth()) / 2,
                                (layeredPane.getHeight() - holder[0].getHeight()) / 2,
                                holder[0].getWidth(),
                                holder[0].getHeight()
                        );

                        layeredPane.add(holder[0], JLayeredPane.POPUP_LAYER);
                        layeredPane.repaint();
                    });
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.err.println("Failed to load student: " + error.getMessage());
                }
            });
        }


    //============================buttons methods================================

    class TButton extends JButton {

        private float alpha = 0f;
        private final float SPEED = 0.08f;
        private Timer fadeIn;
        private Timer fadeOut;

        public TButton(String text) {
            super(text);

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);

            fadeIn = new Timer(16, e -> {
                alpha = Math.min(alpha + SPEED, 1f);
                repaint();
                if (alpha >= 1f) ((Timer) e.getSource()).stop();
            });

            fadeOut = new Timer(16, e -> {
                alpha = Math.max(alpha - SPEED, 0f);
                repaint();
                if (alpha <= 0f) ((Timer) e.getSource()).stop();
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    fadeOut.stop();
                    fadeIn.start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    fadeIn.stop();
                    fadeOut.start();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (alpha > 0f) {
                int a = (int) (alpha * 77);

                for (int i = 4; i >= 1; i--) {
                    g2.setColor(new Color(205, 205, 205, (int) (a * (i / 4f))));
                    g2.fillRoundRect(
                            -i, -i,
                            getWidth() + i * 2,
                            getHeight() + i * 2,
                            30, 30
                    );
                }

                g2.setColor(new Color(205, 205, 205, a));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }

            super.paintComponent(g);
        }
    }
    
    //============================buttons methods================================
    

}