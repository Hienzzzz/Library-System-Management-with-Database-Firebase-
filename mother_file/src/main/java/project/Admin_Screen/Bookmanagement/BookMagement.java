package project.Admin_Screen.Bookmanagement;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;
import project.Main_System.MainFrame;

public class BookMagement extends JPanel {

    private MainFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JLayeredPane layeredPane;
    private AddBookPanel addBook;
    private JPanel dimOverlay;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> categoryBox;
    private JComboBox<String> sortBox;

    public BookMagement(MainFrame frame) {
        this.frame = frame;
        initUI();
        loadBooks();
        
    }
    

    private void initUI() {

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

        dimOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.12f)); // 
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();


            }
        };

        dimOverlay.setBounds(0, 0, 1512, 982);
        dimOverlay.setOpaque(false);      
        dimOverlay.setVisible(false);


        dimOverlay.addMouseListener(new java.awt.event.MouseAdapter() {});

        // ================= TABLE =================
        String[] columns = {"Title", "Book ID", "Author", "Quantity", "Action", "Genre"};
        model = new DefaultTableModel(columns, 0) {
        @Override
            public boolean isCellEditable(int row, int column) {
            return column == 4; // disable editing for all cells
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setBackground(Color.WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));


        //========================search bar field 'to ==========================
       String search_placeHolder = "Search Book...";

        searchField = new JTextField();
        searchField.setBounds(436, 415, 228, 27);
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Poppins", Font.PLAIN, 15));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(null);
        

        searchField.setText("Search Book...");
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals("Search Book...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText("Search Book...");
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        background.add(searchField);

                // ===================== categories filter ====================================
        String[] genres ={
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

        categoryBox.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(new Color(220, 220, 220), 0),
        BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        categoryBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI(){
            @Override
            protected  JButton createArrowButton(){
                JButton CaButton = new JButton();
                CaButton.setContentAreaFilled(false);
                CaButton.setBorder(null);
                CaButton.setFocusPainted(false);
                CaButton.setOpaque(false);

                ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/down-chevron.png")
                );
                Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                CaButton.setIcon(new ImageIcon(img));
                
                return CaButton;
            }
        });
        background.add(categoryBox);

        //======================== sort bar ==============================
        String[] sortOption = {
            "Newest",
            "Oldest",
            "A to Z",
            "Title",
            "Author",
            "Book ID"
        };

        sortBox = new JComboBox<>(sortOption);
        sortBox.setBounds(854, 416, 145, 24);
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
                 
                    setText("Sort by: " + value.toString());
                    setForeground(new Color(60, 60, 60));
                } else {
                  
                    setText(value.toString());
                    setForeground(new Color(60, 60, 60));
                }

                return this;
            }
        });


        sortBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI(){
            @Override
            protected  JButton createArrowButton(){
                JButton sortButton = new JButton();
                sortButton.setContentAreaFilled(false);
                sortButton.setBorder(null);
                sortButton.setFocusPainted(false);
                sortButton.setOpaque(false);

                ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/down-chevron.png")
                );
                Image img = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
                sortButton.setIcon(new ImageIcon(img));
                
                return sortButton;
            }
        });
        background.add(sortBox);

        // ================= SCROLL PANE =================
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(371, 510, 1030, 412);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

      
        JScrollBar vBar = scrollPane.getVerticalScrollBar();
        vBar.setUI(new BasicScrollBarUI() {

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
        });

        vBar.setPreferredSize(new Dimension(8, Integer.MAX_VALUE));

        // ================= COLUMN WIDTHS =================
        table.getColumnModel().getColumn(0).setPreferredWidth(410);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);
        table.getColumnModel().getColumn(2).setPreferredWidth(217);
        table.getColumnModel().getColumn(3).setPreferredWidth(80);
        table.getColumnModel().getColumn(4).setPreferredWidth(170);
        table.getColumnModel().getColumn(5).setMinWidth(0);
        table.getColumnModel().getColumn(5).setMaxWidth(0);
        table.getColumnModel().getColumn(5).setWidth(0);


        table.getColumnModel().getColumn(4).setCellRenderer(new ActionButtonRenderer());
        table.getColumnModel().getColumn(4).setCellEditor(new ActionButtonEditor(this));

        // ================= CELL RENDERER =================
        table.setDefaultRenderer(Object.class, new CustomCellRenderer());

        for (int i = 0; i < table.getColumnCount(); i++) {
            int w = table.getColumnModel().getColumn(i).getPreferredWidth();
            table.getColumnModel().getColumn(i).setMinWidth(w);
            table.getColumnModel().getColumn(i).setMaxWidth(w);
        }

        // ================= HEADER (HIDDEN) =================
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Poppins", Font.PLAIN, 19));
        header.setBackground(new Color(241, 243, 246));
        header.setForeground(Color.GRAY);
        header.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer headerRenderer =
                (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.setTableHeader(null);
        scrollPane.setColumnHeaderView(null);

        background.add(scrollPane);

         //====================== for sorter table =======================
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // ================= ADD BOOK PANEL =================
        addBook = new AddBookPanel(this);
        addBook.setBounds(439, 270, 762, 587); // [wede ma edit for add book panel location]
        addBook.setVisible(false);

        // ================= ADD BOOK BUTTON =================
        JButton addBookButton = new JButton();
        addBookButton.setBounds(1329, 412, 38, 49);
        addBookButton.setBorder(null);
        addBookButton.setContentAreaFilled(false);

        addBookButton.addActionListener(e -> {
            dimOverlay.setVisible(true);
            addBook.setVisible(true);
        });

        

        // ================= LAYERS =================
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(dimOverlay, JLayeredPane.MODAL_LAYER);
        layeredPane.add(addBook, JLayeredPane.POPUP_LAYER);
        layeredPane.add(addBookButton, JLayeredPane.PALETTE_LAYER);


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


        this.add(layeredPane);
    }

    // ================= CLOSE ADD BOOK =================
    public void closeAddBook() {
        addBook.setVisible(false);
        dimOverlay.setVisible(false);
    }


   

    // ================= FIREBASE LISTENER =================
    private void loadBooks() {

        BookService.getRef().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

                SwingUtilities.invokeLater(() -> {
                    model.setRowCount(0);

                    for (DataSnapshot data : snapshot.getChildren()) {
                        Books book = data.getValue(Books.class);
                        if (book == null) continue;

                        model.addRow(new Object[]{
                                book.getTitle(),
                                book.getBookId(),
                                book.getAuthor(),
                                book.getQuantity(),
                                "...",
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

    // ================= SHOW BOOK DETAILS =================
    public void showBookDetails(int row) {

    String bookId = model.getValueAt(row, 1).toString();

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
                        BookMagement.this,   
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

    // ============================for Filters =========================================

   private void applyFilters() {

    List<RowFilter<Object, Object>> filters = new ArrayList<>();

    // 🔍 SEARCH FILTER
    String text = searchField.getText().trim();
    if (!text.isEmpty() && !text.equalsIgnoreCase("Search Book...")) {
        filters.add(RowFilter.regexFilter(
            "(?i)" + text,
            0, // Title
            1, // Book ID
            2  // Author
        ));
    }

    // 🏷 CATEGORY FILTER (MULTI-GENRE SUPPORT)
    String genre = categoryBox.getSelectedItem().toString().trim();
    if (!genre.equalsIgnoreCase("All Categories")) {
        filters.add(RowFilter.regexFilter(
            "(?i).*" + Pattern.quote(genre) + ".*",
            5 // Genre column (hidden)
        ));
    }

    sorter.setRowFilter(
        filters.isEmpty()
            ? null
            : RowFilter.andFilter(filters)
    );
}

    //========================sorting ========================================

    private void applySorting() {

        Object selected = sortBox.getSelectedItem();
        if (selected == null) {
            sorter.setSortKeys(null);
            return;
        }

        String option = selected.toString();
        List<RowSorter.SortKey> keys = new ArrayList<>();

        switch (option) {
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

            default:
                sorter.setSortKeys(null);
                return;
        }

        sorter.setSortKeys(keys);
    }


    //=========================Custom Cell renderer =======================================
    
    class CustomCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column
            );

            // RESET defaults (important)
            setFont(table.getFont());
            setForeground(Color.BLACK);
            setBackground(Color.WHITE);
            setHorizontalAlignment(SwingConstants.LEFT);
            setVerticalAlignment(SwingConstants.CENTER);
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

            if (column == 0) {
                setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
            }

            if (column == 1){
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0,0));
            }

            if (column == 2){
                setBorder(BorderFactory.createEmptyBorder(0, 60, 0,0));
            }
        
            if (column == 3) {
                setHorizontalAlignment(SwingConstants.CENTER);
            }

           
            if (column == 4) {
                setHorizontalAlignment(SwingConstants.RIGHT);
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 20));
            }

           
            if (row % 2 == 0) {
                setBackground(new Color(245, 245, 245));
            }

            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            }

            return this;
        }
    }
    
    // ====================================Action Button Renderer =============================================

    class ActionButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {

    public ActionButtonRenderer() {
            setText("•••");
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(true);
            setFont(new Font("Poppins", Font.BOLD, 18));

            setMargin(new Insets(0, 0, 0, 0));
            setBorder(BorderFactory.createEmptyBorder());

        }
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {

            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                // Zebra striping
                setBackground(row % 2 == 0
                        ? new Color(245, 245, 245)
                        : Color.WHITE);
                setForeground(Color.BLACK);
            }

            return this;
    }
    }



    //=============================button editor ======================================================
   class ActionButtonEditor extends javax.swing.DefaultCellEditor {

        private JButton button;
        private int selectedRow;
        private BookMagement parent;

        public ActionButtonEditor(BookMagement parent) {
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
                JTable table, Object value, boolean isSelected, int viewRow, int column) {

            // ✅ CONVERT VIEW ROW → MODEL ROW
            selectedRow = table.convertRowIndexToModel(viewRow);

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

    // ================= Overlay helpers =================

    public JLayeredPane getLayeredPaneRef() {
        return layeredPane;
    }

    public void showDimOverlay() {
        dimOverlay.setVisible(true);
    }

    public void hideDimOverlay() {
        dimOverlay.setVisible(false);
    }

  




    
}