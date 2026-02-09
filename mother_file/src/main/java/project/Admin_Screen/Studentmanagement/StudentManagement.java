package project.Admin_Screen.Studentmanagement;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

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
    private TableRowSorter<DefaultTableModel> sorter;
    private boolean tableHasFocus = false;

    public StudentManagement(MainFrame frame) {
        this.frame = frame;
        initUI();

    }

    private void initUI() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));


        //============================Layerd pane======================================
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1512, 982);

        //========================Background ========================================
        ImageIcon icon = new ImageIcon(
            getClass().getResource("/Images/Admin_Student management.png")
        );
        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);


        dimOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
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

        dimOverlay.addMouseListener(new java.awt.event.MouseAdapter() {});

        //=========================table================================================

        String[] columns ={"Name", "Student ID", "Email", "Penalty", "Borrowed", "Action" };
        model = new DefaultTableModel(columns, 0){
            @Override
        public boolean isCellEditable(int row, int column){
                return column == 5;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setBackground(Color.white);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        //===================search bar field ====================================
        String search_textHolder = "Search student...";

        searchField = new JTextField();
        searchField.setBounds(436, 415, 228, 27);
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Poppins", Font.PLAIN, 15));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(null);
        background.add(searchField);
        

        searchField.setText(search_textHolder);
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals(search_textHolder)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(search_textHolder);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

         //======================== sort bar ==============================
        String[] sortOption = {
            "Default",
            "Newest",
            "Oldest",
            "A to Z",
            "Student ID"
        };

        sortBox = new JComboBox<>(sortOption);
        sortBox.setBounds(683, 416, 145, 24);
        sortBox.setFont(new Font("Sanchez", Font.PLAIN, 13));
        sortBox.setBackground(Color.WHITE);
        sortBox.setForeground(new Color(60, 60, 60));
        sortBox.setFocusable(false);
        sortBox.setOpaque(false);
        sortBox.setBorder(null);
        background.add(sortBox);

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


        // ================= SCROLL PANE =================
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(371, 510, 1030, 412);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        background.add(scrollPane);

      
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
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(70);


        table.setRowSelectionAllowed(true);
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
                hoveredRow = -1; // clear hover when focus is lost
                table.clearSelection();
                table.repaint();
            }
        });

        background.setFocusable(true);
        background.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                background.requestFocusInWindow();
            }
        });

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

        

         //====================== for sorter table =======================
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

      
        // ================= ADD Student PANEL =================
        addStudent = new AddStudentPanel(this);
        addStudent.setBounds(439, 270, 762, 587); // [wede ma edit for add book panel location]
        addStudent.setVisible(false);

        // ================= ADD Studnet BUTTON =================
        JButton addStudentButton = new JButton();
        addStudentButton.setBounds(1335, 409, 36, 40);
        addStudentButton.setBorder(null);
        addStudentButton.setContentAreaFilled(false);

        addStudentButton.addActionListener(e -> {
            // Prevent double-open
            if (addStudent.isVisible()) return;

            showDimOverlay();

            addStudent.setVisible(true);
            addStudent.repaint();
            addStudent.revalidate();

            // Bring panel to front
            layeredPane.moveToFront(addStudent);
        });


        // ================= LAYERS =================
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(dimOverlay, JLayeredPane.MODAL_LAYER);
        layeredPane.add(addStudent, JLayeredPane.POPUP_LAYER);
        layeredPane.add(addStudentButton, JLayeredPane.PALETTE_LAYER);

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
        
        sortBox.addActionListener(e -> applySorting());


        this.add(layeredPane);

    }

     // ================= CLOSE ADD BOOK =================
    public void closeAddStudent() {
        addStudent.setVisible(false);
        dimOverlay.setVisible(false);
    }

    // ============================for Filters =========================================

    private void applyFilters() {

        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        String text = searchField.getText().trim();
        if (!text.isEmpty() && !text.equalsIgnoreCase("Search student...")) {
            filters.add(RowFilter.regexFilter(
                "(?i)" + Pattern.quote(text),
                0, // Name
                1, // Student ID
                2  // Email
            ));
        }

        sorter.setRowFilter(
            filters.isEmpty() ? null : RowFilter.andFilter(filters)
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
            case "Default" :
                sorter.setSortKeys(null);
                return;
            case "Newest":
                keys.add(new RowSorter.SortKey(1, SortOrder.DESCENDING));
                break;

            case "Oldest":
                keys.add(new RowSorter.SortKey(1, SortOrder.ASCENDING));
                break;

            case "A to Z":
                keys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                break;

            default:
                sorter.setSortKeys(null);
                return;
        }

        sorter.setSortKeys(keys);
    }

    
    //=========================Custom Cell renderer =======================================
    
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


        setBackground(modelRow % 2 == 0 ? EVEN_ROW : ODD_ROW);
        setForeground(Color.BLACK);


        if (row == hoveredRow && tableHasFocus){
                setBackground(HOVER_COLOR);
            }

            // Click selection highlight
            if (table.getSelectedRow() == row && tableHasFocus) {
                setBackground(SELECT_COLOR);
            }
            if (column == 5) {
                return this; // skip hover highlight for action button column
            }


            // Alignment tweaks (keep your layout)
            setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            setVerticalAlignment(SwingConstants.CENTER);

            return this;
        }

    }


    
    // ================= Overlay helpers =================

    public void showDimOverlay() {
        dimOverlay.setVisible(true);
    }

    public void hideDimOverlay() {
            dimOverlay.setVisible(false);
    }

    public JLayeredPane getLayeredPaneRef() {
            return layeredPane;
    }


}