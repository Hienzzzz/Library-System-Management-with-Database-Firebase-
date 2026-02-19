package project.Admin_Screen.Bookmanagement;

/* =========================================================
 * ========================= IMPORTS =======================
 * ========================================================= */

// ================= AWT =================
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
import java.util.regex.Pattern;

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
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import project.Admin_Screen.Admin_accountManagement.Admin_AccountManagement;
import project.Admin_Screen.Dashboard.AdminDashboard;
import project.Admin_Screen.Report_screen.Reports;
import project.Admin_Screen.Studentmanagement.StudentManagement;
import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;
import project.Main_System.MainFrame;



/* =========================================================
 * ======================= MAIN PANEL ======================
 * ========================================================= */

public class BookManagement extends JPanel {

    /* =====================================================
     * ===================== VARIABLES ======================
     * ===================================================== */

    // ===== Frame Reference =====
    private MainFrame frame;

    // ===== Table Components =====
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;

    // ===== Layering & Overlay =====
    private JLayeredPane layeredPane;
    private JPanel dimOverlay;

    // ===== Panels =====
    private AddBookPanel addBook;

    // ===== Filters =====
    private JTextField searchField;
    private JComboBox<String> categoryBox;
    private JComboBox<String> sortBox;

    // ===== UI State =====
    private int hoveredRow = -1;
    private boolean tableHasFocus = false;

    



    /* =====================================================
     * ===================== CONSTRUCTOR ====================
     * ===================================================== */

    public BookManagement(MainFrame frame) {
        this.frame = frame;
        initUI();
        loadBooks();

        
    }



    /* =====================================================
     * ===================== UI SECTION =====================
     * ===================================================== */

    private void initUI() {

        // ================= BASE PANEL =================
        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        // ================= LAYERED PANE =================
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1512, 982);

        // ================= BACKGROUND =================
        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_BookManagement.png")
        );

        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        /* =====================================================
         * ================= SIDEBAR BUTTONS ===================
         * ===================================================== */

        // ================= DASHBOARD BUTTON =================
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

        // ================= REPORTS BUTTON =================
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

        // ================= BOOK MANAGEMENT BUTTON =================
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

        // ================= STUDENT MANAGEMENT =================
        TButton studentM = new TButton("Student Management");
        studentM.setBounds(12, 564, 238, 49);
        studentM.setFont(MainFrame.loadSanchez(15f));
        studentM.setForeground(new Color(93, 93, 93));
        studentM.setHorizontalAlignment(SwingConstants.LEFT);
        studentM.setMargin(new Insets(0, 60, 0, 0));
        studentM.setCursor(new Cursor(Cursor.HAND_CURSOR));
        studentM.addActionListener(e -> {
            frame.setContentPane(new StudentManagement(frame));
            frame.revalidate();
        });

        // ================= ADMIN MANAGEMENT =================
        TButton accountM = new TButton("Admin Management");
        accountM.setBounds(12, 615, 238, 49);
        accountM.setFont(MainFrame.loadSanchez(15f));
        accountM.setForeground(new Color(93, 93, 93));
        accountM.setHorizontalAlignment(SwingConstants.LEFT);
        accountM.setMargin(new Insets(0, 60, 0, 0));
        accountM.setCursor(new Cursor(Cursor.HAND_CURSOR));
        accountM.addActionListener(e -> {
            frame.setContentPane(new Admin_AccountManagement(frame));
            frame.revalidate();
        });

        /* =====================================================
         * ================= BOOK SUB TABS =====================
         * ===================================================== */

        TButton availableBooks = new TButton("Available Books");
        availableBooks.setBounds(55, 405, 200, 35);
        availableBooks.setFont(MainFrame.loadSanchez(13f));
        availableBooks.setForeground(new Color(93, 93, 93));
        availableBooks.setCursor(new Cursor(Cursor.HAND_CURSOR));
        availableBooks.addActionListener(e -> {
            frame.setContentPane(new AvailabaleBooks(frame));
            frame.revalidate();
        });

        TButton borrowedBooks = new TButton("Borrowed Books");
        borrowedBooks.setBounds(55, 442, 200, 35);
        borrowedBooks.setFont(MainFrame.loadSanchez(13f));
        borrowedBooks.setForeground(new Color(93, 93, 93));
        borrowedBooks.setCursor(new Cursor(Cursor.HAND_CURSOR));
        borrowedBooks.addActionListener(e -> {
            frame.setContentPane(new BorrowedBook(frame));
            frame.revalidate();
        });

        TButton pendingRequest = new TButton("Pending Request");
        pendingRequest.setBounds(55, 477, 200, 35);
        pendingRequest.setFont(MainFrame.loadSanchez(13f));
        pendingRequest.setForeground(new Color(93, 93, 93));
        pendingRequest.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pendingRequest.addActionListener(e -> {
            frame.setContentPane(new Pendingreq(frame));
            frame.revalidate();
        });

        TButton overdue = new TButton("Overdue");
        overdue.setBounds(55, 514, 200, 35);
        overdue.setFont(MainFrame.loadSanchez(13f));
        overdue.setForeground(new Color(93, 93, 93));
        overdue.setCursor(new Cursor(Cursor.HAND_CURSOR));
        overdue.addActionListener(e -> {
            frame.setContentPane(new Overdue(frame));
            frame.revalidate();
        });

        // Add Sidebar Buttons to Background
        background.add(dashboard);
        background.add(reports);
        background.add(bookManagement);
        background.add(studentM);
        background.add(accountM);
        background.add(availableBooks);
        background.add(borrowedBooks);
        background.add(pendingRequest);
        background.add(overdue);

                /* =====================================================
         * ===================== DIM OVERLAY ====================
         * ===================================================== */

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
        dimOverlay.addMouseListener(new java.awt.event.MouseAdapter() {});


         /* =====================================================
         * =================== TOTAL BOOK OVERVIEW ================
         * ===================================================== */

        JLabel totalBookOverview = new JLabel("25");
        totalBookOverview.setBounds(475, 240, 200, 60); // adjust to match your UI card
        totalBookOverview.setFont(new Font("Sanchez", Font.PLAIN, 40));
        totalBookOverview.setForeground(Color.WHITE);
        //totalBookOverview.setHorizontalAlignment(SwingConstants.LEFT);

        background.add(totalBookOverview);


        /* =====================================================
         * ================= AVAILABLE BOOK OVERVIEW ==============
         * ===================================================== */

        JLabel availableBookOverview = new JLabel("50");
        availableBookOverview.setBounds(730, 240, 200, 60);
        availableBookOverview.setFont(new Font("Poppins", Font.PLAIN, 40));
        availableBookOverview.setForeground(Color.white);

        background.add(availableBookOverview);


        /* =====================================================
         * ================= BORROWED BOOK OVERVIEW ==============
         * ===================================================== */

        JLabel borrowedBookOverview = new JLabel("50");
        borrowedBookOverview.setBounds(1000, 240, 200, 60);
        borrowedBookOverview.setFont(new Font("Poppins", Font.PLAIN, 40));
        borrowedBookOverview.setForeground(Color.white);

        background.add(borrowedBookOverview);


        /* =====================================================
         * ================= OVERDUE BOOK OVERVIEW ==============
         * ===================================================== */

        JLabel overdueBookOverview = new JLabel("50");
        overdueBookOverview.setBounds(1275, 240, 200, 60);
        overdueBookOverview.setFont(new Font("Poppins", Font.PLAIN, 40));
        overdueBookOverview.setForeground(Color.white);

        background.add(overdueBookOverview);




        /* =====================================================
         * ====================== TABLE SETUP ===================
         * ===================================================== */

        // ================= TABLE MODEL =================
        String[] columns = {"Title", "Book ID", "Author", "Quantity", "Action", "Genre"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        // ================= TABLE COMPONENT =================
        table = new JTable(model);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setRowHeight(28);
        table.setBackground(Color.WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));


        //================== HEADER ADJUSTMENT =============
        DefaultTableCellRenderer headerRenderer =
        (DefaultTableCellRenderer) table.getTableHeader().getDefaultRenderer();

        headerRenderer.setBorder(BorderFactory.createEmptyBorder());
        table.getTableHeader().setFont(new Font("Sanchez", Font.PLAIN, 17));
        table.getTableHeader().setBackground(new Color(241, 243, 246));
        table.getTableHeader().setForeground(new Color(60, 60, 60));
        table.getTableHeader().setFont(MainFrame.loadSanchez(14f).deriveFont(Font.BOLD));
        table.getTableHeader().setOpaque(true);
        table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
        table.getTableHeader().setBorder(
            BorderFactory.createMatteBorder(0, 0, 3, 0, new Color(0xb4b4b4))
        );

        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {

                JLabel label = (JLabel) super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setBackground(new Color(241, 243, 246));
                label.setForeground(new Color(60, 60, 60));
                label.setFont(MainFrame.loadSanchez(14f).deriveFont(Font.BOLD));

                // 🔹 REMOVE BORDER
                label.setBorder(BorderFactory.createEmptyBorder());

                return label;
            }
        });


        // ================= COLUMN WIDTHS =================
        table.getColumnModel().getColumn(0).setPreferredWidth(410);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(217);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(170);

        // Hide Genre column
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);
        table.getColumnModel().getColumn(5).setWidth(0);

        // ================= TABLE RENDERERS =================
        table.setDefaultRenderer(Object.class, new CustomCellRenderer());
        table.getColumnModel().getColumn(4).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ActionButtonEditor(this));

        // ================= TABLE SELECTION =================
        table.setRowSelectionAllowed(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFocusable(true);


        /* =====================================================
         * ===================== SCROLL PANE ====================
         * ===================================================== */

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(367, 467, 1038, 412);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        // ================= CUSTOM VERTICAL SCROLLBAR =================
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setUI(new ModernScrollBarUI());
        vBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));


        /* =====================================================
         * ===================== SEARCH FIELD ===================
         * ===================================================== */

        String search_placeHolder = "Search Book...";

        searchField = new JTextField();
        searchField.setBounds(436, 415, 228, 27);
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Poppins", Font.PLAIN, 15));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(null);
        searchField.setText(search_placeHolder);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals(search_placeHolder)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(search_placeHolder);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        background.add(searchField);


        /* =====================================================
         * ===================== CATEGORY FILTER ================
         * ===================================================== */

        String[] genres = {
                "Default",
                "Education",
                "Fiction",
                "History",
                "Non-Fiction",
                "Science",
                "Technology"
        };

        categoryBox = new JComboBox<>(genres);
        categoryBox.setBounds(680, 414, 150, 26);
        categoryBox.setFont(new Font("Poppins", Font.PLAIN, 13));
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setForeground(new Color(60, 60, 60));
        categoryBox.setFocusable(false);
        categoryBox.setOpaque(false);
        categoryBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color (218, 221, 225), 0),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        categoryBox.setSelectedItem(null);

        categoryBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellhasFocus){
                    super.getListCellRendererComponent(list, value, index, isSelected, cellhasFocus);

                    if(index == -1 && value == null){
                        setText("Category:");
                        setForeground(new Color(150, 150, 150));
                    }else if(index == -1){
                        setText("Category: " + value);
                        setForeground(new Color(60, 60, 60));
                    }else{
                        setText(value.toString());
                        setForeground(new Color(60, 60, 60));
                    }
                    return this;
                }
        });

        categoryBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI(){
            @Override
            protected JButton createArrowButton(){
                JButton b = new JButton();
                b.setContentAreaFilled(false);
                b.setBorder(null);
                b.setFocusable(false);
                b.setOpaque(false);
                ImageIcon icon = new ImageIcon(
                    getClass().getResource("/Images/down-chevron.png")
                );
                Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                b.setIcon(new ImageIcon(img));
                return b;
            }
        });

        categoryBox.addActionListener( e -> applySorting());

        categoryBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JComboBox<?> box = (JComboBox<?>) e.getSource();
                    JPopupMenu popup =
                        (JPopupMenu) box.getAccessibleContext().getAccessibleChild(0);

                    if (popup != null) {

                        popup.setBorder(BorderFactory.createLineBorder(
                            new Color(218, 221, 225), 1
                        ));

                        if (popup.getComponentCount() > 0) {
                            JScrollPane sp = (JScrollPane) popup.getComponent(0);
                            JScrollBar vBar = sp.getVerticalScrollBar();
                            vBar.setUI(new ModernScrollBarUI());
                            vBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
                        }
                    }
                });
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {}
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
        });


        background.add(categoryBox);

   /*     String[] genres = {
                "All Categories",
                "Fiction",
                "Non-Fiction",
                "Science",
                "History",
                "Technology",
                "Education"
        };

        categoryBox = new JComboBox<>(genres);
        categoryBox.setBounds(684, 414, 145, 26);
        categoryBox.setFont(new Font("Sanchez", Font.PLAIN, 13));
        categoryBox.setBackground(Color.WHITE);
        categoryBox.setForeground(new Color(60, 60, 60));
        categoryBox.setFocusable(false);

        background.add(categoryBox);
*/ 

        /* =====================================================
         * ===================== SORT FILTER ====================
         * ===================================================== */

        sortBox = new JComboBox<>(new String[] {
            "Default",
            "A to Z",
            "Newest",
            "Low Quantity",
            "Oldest",
            "Out of Stock"

        });

        sortBox.setBounds(848, 417, 158, 24);
        sortBox.setFont(new Font("Poppins", Font.PLAIN, 13));
        sortBox.setBackground(Color.WHITE);
        sortBox.setForeground(new Color(60, 60, 60));
        sortBox.setFocusable(false);
        sortBox.setOpaque(false);
        sortBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color (218, 221, 225), 0),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        sortBox.setSelectedItem(null);

        sortBox.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellhasFocus){
                    super.getListCellRendererComponent(list, value, index, isSelected, cellhasFocus);

                    if(index == -1 && value == null){
                        setText("Sort by:");
                        setForeground(new Color(150, 150, 150));
                    }else if(index == -1){
                        setText("Sort by: " + value);
                        setForeground(new Color(60, 60, 60));
                    }else{
                        setText(value.toString());
                        setForeground(new Color(60, 60, 60));
                    }
                    return this;
                }
        });

        sortBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI(){
            @Override
            protected JButton createArrowButton(){
                JButton b = new JButton();
                b.setContentAreaFilled(false);
                b.setBorder(null);
                b.setFocusable(false);
                b.setOpaque(false);
                ImageIcon icon = new ImageIcon(
                    getClass().getResource("/Images/down-chevron.png")
                );
                Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                b.setIcon(new ImageIcon(img));
                return b;
            }
        });

        sortBox.addActionListener( e -> applySorting());

        sortBox.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                SwingUtilities.invokeLater(() -> {
                    JComboBox<?> box = (JComboBox<?>) e.getSource();
                    JPopupMenu popup =
                        (JPopupMenu) box.getAccessibleContext().getAccessibleChild(0);

                    if (popup != null) {

                        popup.setBorder(BorderFactory.createLineBorder(
                            new Color(218, 221, 225), 1
                        ));

                        if (popup.getComponentCount() > 0) {
                            JScrollPane sp = (JScrollPane) popup.getComponent(0);
                            JScrollBar vBar = sp.getVerticalScrollBar();
                            vBar.setUI(new ModernScrollBarUI()); 
                            vBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));
                        }
                    }
                });
            }

            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {}
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
        });


        background.add(sortBox);


        /* =====================================================
         * ===================== TABLE SORTER ===================
         * ===================================================== */

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);


        /* =====================================================
         * ===================== TABLE EVENTS ===================
         * ===================================================== */

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


        /* =====================================================
         * ===================== FILTER LISTENERS ==============
         * ===================================================== */

        searchField.getDocument().addDocumentListener(new DocumentListener() {

            private void filter() {
                applyFilters();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filter();
            }
        });

        categoryBox.addActionListener(e -> applyFilters());
        sortBox.addActionListener(e -> applySorting());


        /* =====================================================
         * ===================== ADD TO LAYERS ==================
         * ===================================================== */

        background.add(scrollPane);

        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(dimOverlay, JLayeredPane.MODAL_LAYER);

        this.add(layeredPane);
    }

        /* =====================================================
     * ===================== FIREBASE SECTION ===============
     * ===================================================== */

    // ================= OPTIONAL FILTER HOOK =================
    protected boolean includeBook(Books book) {
        return true;
    }

    // ================= LOAD BOOKS FROM FIREBASE =================
    private void loadBooks() {

        BookService.getRef().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                SwingUtilities.invokeLater(() -> {
                    model.setRowCount(0);

                    for (DataSnapshot data : snapshot.getChildren()) {

                        Books book = data.getValue(Books.class);
                        if (book == null) continue;

                        book.setBookId(data.getKey());

                        model.addRow(new Object[]{
                                book.getTitle(),
                                book.getBookId(),
                                book.getAuthor(),
                                book.getQuantity(),
                                "•••",
                                book.getGenre()
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.out.println("Firebase error: " + error.getMessage());
            }
        });
    }


    /* =====================================================
     * ===================== BOOK DETAILS ===================
     * ===================================================== */

    public void showBookDetails(int row) {

        int modelRow = table.convertRowIndexToModel(row);
        String bookId = model.getValueAt(modelRow, 1).toString();

        BookService.getRef()
                .child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        Books book = snapshot.getValue(Books.class);
                        if (book == null) return;

                        SwingUtilities.invokeLater(() -> {

                            dimOverlay.setVisible(true);

                            final BookDetailsPanel[] holder =
                                    new BookDetailsPanel[1];

                            holder[0] = new BookDetailsPanel(
                                    BookManagement.this,
                                    book,
                                    () -> {
                                        layeredPane.remove(holder[0]);
                                        hideDimOverlay();
                                        layeredPane.repaint();
                                    }
                            );

                            layeredPane.add(holder[0], JLayeredPane.POPUP_LAYER);
                            layeredPane.repaint();
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.out.println(
                                "Failed to load book details: " +
                                        error.getMessage()
                        );
                    }
                });
    }


    /* =====================================================
     * ===================== FILTER SECTION =================
     * ===================================================== */

    private void applyFilters() {

        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        // ================= SEARCH FILTER =================
        String text = searchField.getText().trim();
        if (!text.isEmpty() && !text.equalsIgnoreCase("Search Book...")) {
            filters.add(RowFilter.regexFilter(
                    "(?i)" + Pattern.quote(text),
                    0, 1, 2
            ));
        }

        // ================= CATEGORY FILTER =================
        String genre = categoryBox.getSelectedItem().toString().trim();
        if (!genre.equalsIgnoreCase("All Categories")) {
            filters.add(RowFilter.regexFilter(
                    "(?i).*" + Pattern.quote(genre) + ".*",
                    5
            ));
        }

        // ================= STOCK FILTER =================
        Object selectedSort = sortBox.getSelectedItem();
        if (selectedSort != null) {
            String option = selectedSort.toString();

            if (option.equals("Available Books")
                    || option.equals("Low Quantity")
                    || option.equals("Out of Stock")) {

                filters.add(getStockFilter(option));
            }
        }

        sorter.setRowFilter(
                filters.isEmpty()
                        ? null
                        : RowFilter.andFilter(filters)
        );
    }


    private RowFilter<Object, Object> getStockFilter(String option) {

        return new RowFilter<>() {
            @Override
            public boolean include(Entry<?, ?> entry) {

                int quantity = Integer.parseInt(entry.getStringValue(3));

                switch (option) {
                    case "Available Books":
                        return quantity >= 1;

                    case "Low Quantity":
                        return quantity > 0 && quantity <= 5;

                    case "Out of Stock":
                        return quantity == 0;

                    default:
                        return true;
                }
            }
        };
    }


    /* =====================================================
     * ===================== SORTING SECTION ================
     * ===================================================== */

    private void applySorting() {

        Object selected = sortBox.getSelectedItem();

        if (selected == null) {
            sorter.setSortKeys(null);
            applyFilters();
            return;
        }

        String option = selected.toString();

        // Stock options = filtering only
        if (option.equals("Available Books")
                || option.equals("Low Quantity")
                || option.equals("Out of Stock")) {

            sorter.setSortKeys(null);
            applyFilters();
            return;
        }

        List<RowSorter.SortKey> keys = new ArrayList<>();

        switch (option) {

            case "Default":
                sorter.setSortKeys(null);
                return;

            case "Newest":
                keys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
                break;

            case "Oldest":
                keys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                break;

            case "A to Z":
            case "Title":
                keys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                break;

            case "Author":
                keys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
                break;

            case "Book ID":
                keys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                break;

            default:
                sorter.setSortKeys(null);
                return;
        }

        sorter.setSortKeys(keys);
    }


    /* =====================================================
     * ================= CUSTOM SCROLLBAR UI ===============
     * ===================================================== */

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

        /* =====================================================
     * ================= CUSTOM CELL RENDERER ===============
     * ===================================================== */

    class CustomCellRenderer extends DefaultTableCellRenderer {

        private final Color HOVER_COLOR = new Color(230, 240, 255);
        private final Color SELECT_COLOR = new Color(200, 220, 255);
        private final Color EVEN_ROW = new Color(245, 245, 245);
        private final Color ODD_ROW = Color.WHITE;

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(
                    table, value, false, false, row, column
            );

            int modelRow = table.convertRowIndexToModel(row);

            // ===== Alternate Row Colors =====
            setBackground(modelRow % 2 == 0 ? EVEN_ROW : ODD_ROW);
            setForeground(Color.BLACK);

            // ===== Quantity Coloring =====
            if (column == 3 && value != null) {
                int qty = Integer.parseInt(value.toString());

                if (qty == 0) {
                    setForeground(Color.RED);
                } else if (qty <= 5) {
                    setForeground(new Color(255, 140, 0));
                } else {
                    setForeground(new Color(0, 128, 0));
                }
            }

            // ===== Hover Effect =====
            if (row == hoveredRow && tableHasFocus) {
                setBackground(HOVER_COLOR);
            }

            // ===== Selection Effect =====
            if (table.getSelectedRow() == row && tableHasFocus) {
                setBackground(SELECT_COLOR);
            }

           // ===== Alignment =====
            if (column == 1) { // Quantity column
                setHorizontalAlignment(SwingConstants.CENTER);
            }else if (column == 2) {
                setHorizontalAlignment(SwingConstants.CENTER);
            } else if (column == 3) {
                setHorizontalAlignment(SwingConstants.CENTER);
            }else if (column == 4) {
                return this; // Action handled separately
            }  else {
                setHorizontalAlignment(SwingConstants.LEFT);
            }

            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setVerticalAlignment(SwingConstants.CENTER);

            return this;

        }
    }


    /* =====================================================
     * ================= ACTION BUTTON RENDERER =============
     * ===================================================== */

    class ActionButtonRenderer extends JButton
            implements javax.swing.table.TableCellRenderer {

        public ActionButtonRenderer() {
            setText("•••");
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(true);
            setOpaque(true);
            setFont(new Font("Poppins", Font.BOLD, 18));
            setMargin(new Insets(0, 0, 0, 0));
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            int modelRow = table.convertRowIndexToModel(row);

            Color EVEN_ROW = new Color(245, 245, 245);
            Color ODD_ROW = Color.WHITE;
            Color HOVER_COLOR = new Color(230, 240, 255);
            Color SELECT_COLOR = new Color(200, 220, 255);

            setBackground(modelRow % 2 == 0 ? EVEN_ROW : ODD_ROW);
            setForeground(Color.BLACK);

            if (row == hoveredRow && tableHasFocus) {
                setBackground(HOVER_COLOR);
            }

            if (table.getSelectedRow() == row && tableHasFocus) {
                setBackground(SELECT_COLOR);
            }

            return this;
        }
    }


    /* =====================================================
     * ================= ACTION BUTTON EDITOR ===============
     * ===================================================== */

    class ActionButtonEditor extends javax.swing.DefaultCellEditor {

        private JButton button;
        private int selectedRow;
        private BookManagement parent;

        public ActionButtonEditor(BookManagement parent) {
            super(new javax.swing.JTextField());
            this.parent = parent;

            button = new JButton("•••");
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(true);
            button.setOpaque(true);
            button.setFont(new Font("Poppins", Font.BOLD, 18));

            button.addActionListener(e -> {
                fireEditingStopped();
                parent.showBookDetails(selectedRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value, boolean isSelected,
                int viewRow, int column) {

            selectedRow = viewRow;

            if (isSelected) {
                button.setBackground(table.getSelectionBackground());
                button.setForeground(table.getSelectionForeground());
            } else {
                button.setBackground(viewRow % 2 == 0
                        ? new Color(245, 245, 245)
                        : Color.WHITE);
                button.setForeground(Color.BLACK);
            }

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "•••";
        }
    }


    /* =====================================================
     * ================= OVERLAY HELPERS ====================
     * ===================================================== */

    public void closeAddBook() {
        addBook.setVisible(false);
        dimOverlay.setVisible(false);
    }

    public JLayeredPane getLayeredPaneRef() {
        return layeredPane;
    }

    public void showDimOverlay() {
        dimOverlay.setVisible(true);
    }

    public void hideDimOverlay() {
        dimOverlay.setVisible(false);
    }


    /* =====================================================
     * ================= CUSTOM ANIMATED BUTTON =============
     * ===================================================== */

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

}



