package project.Admin_Screen.Bookmanagement;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

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
        g2.setComposite(AlphaComposite.SrcOver.derive(0.12f)); // 15% dim
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
        String[] columns = {"Title", "Book ID", "Author", "Quantity", "Action"};
        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);
        table.setRowHeight(28);
        table.setBackground(Color.WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

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
        table.getColumnModel().getColumn(2).setPreferredWidth(240);
        table.getColumnModel().getColumn(3).setPreferredWidth(115);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);

        // ================= CELL RENDERER =================
        table.setDefaultRenderer(Object.class, new CustomCellRenderer());

        for (int i = 0; i < table.getColumnCount(); i++) {
            int w = table.getColumnModel().getColumn(i).getPreferredWidth();
            table.getColumnModel().getColumn(i).setMinWidth(w);
            table.getColumnModel().getColumn(i).setMaxWidth(w);
        }

        // ================= HEADER (HIDDEN) =================
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Poppins", Font.PLAIN, 18));
        header.setBackground(new Color(241, 243, 246));
        header.setForeground(Color.GRAY);
        header.setBorder(BorderFactory.createEmptyBorder());

        DefaultTableCellRenderer headerRenderer =
                (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        table.setTableHeader(null);
        scrollPane.setColumnHeaderView(null);

        background.add(scrollPane);

        // ================= ADD BOOK PANEL =================
        addBook = new AddBookPanel(this);
        addBook.setBounds(375, 200, 762, 587);
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

        add(layeredPane);
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
                                book.getQuantity()
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
}
