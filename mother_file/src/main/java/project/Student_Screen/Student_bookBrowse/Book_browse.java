package project.Student_Screen.Student_bookBrowse;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
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
import javax.swing.table.TableRowSorter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;
import project.Main_System.MainFrame;
import project.Student_Screen.Student_BookRequest.Book_request;
import project.Student_Screen.Student_borrowedBook.My_borrowed_Books;
import project.Student_Screen.Student_dashboard.Student_Dashboard;
import project.Student_Screen.Student_highlights.Highlights;


public class Book_browse extends JPanel {

    private final MainFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JComboBox<String> categoryBox;
    private JComboBox<String> sortBox;
    private TableRowSorter<DefaultTableModel> sorter;


    public Book_browse(MainFrame frame) {
        this.frame = frame;
        initializeUI();
        loadBooks();
    }

    private void initializeUI() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Students_browse books.png")
        );

        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        searchField = new JTextField();
        searchField.setBounds(390, 290, 260, 27);
        searchField.setFont(new java.awt.Font("Poppins", Font.PLAIN, 14));
        //searchField.setBorder(null);
        searchField.setBackground(Color.WHITE);
        background.add(searchField);

        String[] categories = {
        "All Categories",
        "Fiction",
        "Non-Fiction",
        "Science",
        "History",
        "Technology",
        "Education"
        };

        categoryBox = new JComboBox<>(categories);
        categoryBox.setBounds(684, 290, 145, 26);
        categoryBox.setFont(new java.awt.Font("Poppins", Font.PLAIN, 13));
        categoryBox.setFocusable(false);
        background.add(categoryBox);

        String[] sortOptions = {
        "Default",
        "Title (A-Z)",
        "Title (Z-A)",
        "Quantity (High-Low)",
        "Quantity (Low-High)"
            };

            sortBox = new JComboBox<>(sortOptions);
            sortBox.setBounds(854, 290, 170, 26);
            sortBox.setFont(new java.awt.Font("Poppins", Font.PLAIN, 13));
            sortBox.setFocusable(false);
            background.add(sortBox);



        addSidebarButtons(background);
        setupTable(background);

        searchField.getDocument().addDocumentListener(
        new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                applyFilters();
            }
            public void removeUpdate(DocumentEvent e) {
                applyFilters();
            }
            public void changedUpdate(DocumentEvent e) {
                applyFilters();
            }
        });

categoryBox.addActionListener(e -> applyFilters());

sortBox.addActionListener(e -> applySorting());


        add(background);
    }

    private void addSidebarButtons(JLabel background) {

        JButton dashboard = createSidebarButton("Student Dashboard", 240);
        dashboard.addActionListener(e -> {
            frame.setContentPane(new Student_Dashboard(frame));
            frame.revalidate();
        });

        JButton highlights = createSidebarButton("Highlights", 293);
        highlights.addActionListener(e -> {
            frame.setContentPane(new Highlights(frame));
            frame.revalidate();
        });

        JButton browse = createSidebarButton("Browse Books", 345);

        JButton borrowed = createSidebarButton("My Borrowed Books", 398);
        borrowed.addActionListener(e -> {
            frame.setContentPane(new My_borrowed_Books(frame));
            frame.revalidate();
        });

        JButton requests = createSidebarButton("My Requests", 451);
        requests.addActionListener(e -> {
            frame.setContentPane(new Book_request(frame));
            frame.revalidate();
        });

        background.add(dashboard);
        background.add(highlights);
        background.add(browse);
        background.add(borrowed);
        background.add(requests);
    }

    private JButton createSidebarButton(String text, int y) {
        JButton button = new JButton(text);
        button.setBounds(12, y, 238, 49);
        button.setFont(MainFrame.loadSanchez(15f));
        button.setForeground(new Color(93, 93, 93));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setMargin(new Insets(0, 60, 0, 0));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    
    
    private void setupTable(JLabel background) {

        String[] columns = {"Title", "Book ID", "Author", "Quantity", "Action"};

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setTableHeader(null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        

        int[] widths = {410, 150, 217, 80, 150};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            table.getColumnModel().getColumn(i).setMinWidth(widths[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(widths[i]);
        }

        table.setDefaultRenderer(Object.class, new StyledCellRenderer());
        table.getColumnModel().getColumn(4)
                .setCellRenderer(new ActionRenderer());
        table.getColumnModel().getColumn(4)
                .setCellEditor(new ActionEditor());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(371, 380, 1040, 550);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        scrollPane.getVerticalScrollBar().setPreferredSize(
                new Dimension(8, Integer.MAX_VALUE)
        );

        background.add(scrollPane);
    }

    private void loadBooks() {

        BookService.getRef().addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {

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
                            "•••"
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                JOptionPane.showMessageDialog(
                        Book_browse.this,
                        "Failed to load books.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    private void showBookDetails(int row) {

        String bookId = model.getValueAt(row, 1).toString();

        BookService.getRef()
                .child(bookId)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        Books book = snapshot.getValue(Books.class);
                        if (book == null) return;

                        JLayeredPane layeredPane = frame.getLayeredPane();

                        final StudentBookDetailsPanel[] panelHolder =
                                new StudentBookDetailsPanel[1];

                        panelHolder[0] =
                                new StudentBookDetailsPanel(book, () -> {
                                    layeredPane.remove(panelHolder[0]);
                                    layeredPane.repaint();
                                });

                        layeredPane.add(panelHolder[0],
                                JLayeredPane.POPUP_LAYER);
                        layeredPane.repaint();
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {}
                });
    }

    private class StyledCellRenderer extends DefaultTableCellRenderer {

        private final Color EVEN = new Color(245, 245, 245);
        private final Color ODD = Color.WHITE;

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            super.getTableCellRendererComponent(
                    table, value, false, false, row, column);

            setBackground(row % 2 == 0 ? EVEN : ODD);

            if (column == 3 && value != null) {
                int qty = Integer.parseInt(value.toString());
                if (qty == 0) setForeground(Color.RED);
                else if (qty <= 5) setForeground(new Color(255, 140, 0));
                else setForeground(new Color(0, 128, 0));
            } else {
                setForeground(Color.BLACK);
            }

            setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

            return this;
        }
    }

   private class ActionRenderer extends JButton
            implements javax.swing.table.TableCellRenderer {

        public ActionRenderer() {
            setText("•••");
            setFont(new java.awt.Font("Poppins", Font.BOLD, 18));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);   // 🔥 remove gradient
            setOpaque(true);
            setMargin(new Insets(0, 0, 0, 0));
            setBorder(BorderFactory.createEmptyBorder());
        }

        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            Color EVEN = new Color(245, 245, 245);
            Color ODD = Color.WHITE;

            setBackground(row % 2 == 0 ? EVEN : ODD);
            setForeground(Color.BLACK);

            return this;
        }
    }


    private class ActionEditor extends DefaultCellEditor {

        private final JButton button;
        private int selectedRow;

        public ActionEditor() {
            super(new JTextField());

            button = new JButton("•••");
            button.setFont(new java.awt.Font("Poppins", Font.BOLD, 18));
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setContentAreaFilled(false); // 🔥 remove gradient
            button.setOpaque(true);
            button.setBorder(BorderFactory.createEmptyBorder());

            button.addActionListener(e -> {
                fireEditingStopped();
                showBookDetails(selectedRow);
            });
        }

        @Override
        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSelected, int row, int column) {

            selectedRow = table.convertRowIndexToModel(row);

            Color EVEN = new Color(245, 245, 245);
            Color ODD = Color.WHITE;

            button.setBackground(row % 2 == 0 ? EVEN : ODD);
            button.setForeground(Color.BLACK);

            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "•••";
        }
    }


    private static class ModernScrollBarUI extends BasicScrollBarUI {
        @Override protected JButton createDecreaseButton(int o) { return new JButton(); }
        @Override protected JButton createIncreaseButton(int o) { return new JButton(); }
    }

        private void applyFilters() {

        List<RowFilter<Object, Object>> filters = new ArrayList<>();

        // 🔍 Search filter
        String text = searchField.getText().trim();
        if (!text.isEmpty()) {
            filters.add(RowFilter.regexFilter(
                    "(?i)" + Pattern.quote(text),
                    0, 1, 2
            ));
        }

        // 📚 Category filter
        String selectedCategory =
                categoryBox.getSelectedItem().toString();

        if (!selectedCategory.equals("All Categories")) {
            filters.add(RowFilter.regexFilter(
                    "(?i).*" + Pattern.quote(selectedCategory) + ".*",
                    0
            ));
        }

        sorter.setRowFilter(
                filters.isEmpty()
                        ? null
                        : RowFilter.andFilter(filters)
        );
    }

            private void applySorting() {

            String selected =
                    sortBox.getSelectedItem().toString();

            List<RowSorter.SortKey> keys = new ArrayList<>();

            switch (selected) {

                case "Title (A-Z)":
                    keys.add(new RowSorter.SortKey(
                            0, SortOrder.ASCENDING));
                    break;

                case "Title (Z-A)":
                    keys.add(new RowSorter.SortKey(
                            0, SortOrder.DESCENDING));
                    break;

                case "Quantity (High-Low)":
                    keys.add(new RowSorter.SortKey(
                            3, SortOrder.DESCENDING));
                    break;

                case "Quantity (Low-High)":
                    keys.add(new RowSorter.SortKey(
                            3, SortOrder.ASCENDING));
                    break;

                default:
                    sorter.setSortKeys(null);
                    return;
            }

            sorter.setSortKeys(keys);
        }


}
