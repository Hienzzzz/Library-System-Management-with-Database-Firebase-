package project.Admin_Screen.Bookmanagement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
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

        // ================= TABLE =================
        String[] columns = {"Title", "Book ID", "Author", "Quantity"};
        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);
        table.setRowHeight(28);
        table.setBackground(Color.WHITE);
        table.setShowGrid(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(371, 510, 1030, 412);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        table.getColumnModel().getColumn(0).setPreferredWidth(412);
        table.getColumnModel().getColumn(1).setPreferredWidth(195);
        table.getColumnModel().getColumn(2).setPreferredWidth(244);
        table.getColumnModel().getColumn(3).setPreferredWidth(107);

        for (int i = 0; i < table.getColumnCount(); i++) {
            int w = table.getColumnModel().getColumn(i).getPreferredWidth();
            table.getColumnModel().getColumn(i).setMinWidth(w);
            table.getColumnModel().getColumn(i).setMaxWidth(w);
        }

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
        addBook.setBounds(375, 200, 762, 587); // EXACT image size
        addBook.setVisible(false);

        // ================= ADD BOOK BUTTON =================
        JButton addBookButton = new JButton();
        addBookButton.setBounds(1329, 412, 38, 49);
        addBookButton.setBorder(null);
        addBookButton.setContentAreaFilled(false);

        addBookButton.addActionListener(e -> {
           
            addBook.setVisible(true);
        });

        // ================= ADD TO LAYERS =================
        layeredPane.add(background, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(addBook, JLayeredPane.POPUP_LAYER);
        layeredPane.add(addBookButton, JLayeredPane.PALETTE_LAYER);

        add(layeredPane);
    }

    // ================= CLOSE ADD BOOK =================
    public void closeAddBook() {
        addBook.setVisible(false);
     
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

                        model.addRow(new Object[]{
                                book.getTitle(),
                                book.getBook_id(),
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
