package project.Student_Screen.Student_borrowedBook;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;

import project.Firebase_backend.Book_backend.Books;

public class StudentBorrowedDetailsPanel extends JPanel {

    private Books book;
    private Runnable onClose;

    public StudentBorrowedDetailsPanel(Books book, Runnable onClose) {

        this.book = book;
        this.onClose = onClose;

        setLayout(null);
        setBounds(439, 270, 762, 587);
        setOpaque(false);

        ImageIcon image = new ImageIcon(
                getClass().getResource("/Images/Admin_viewBook.png")
        );

        JLabel background = new JLabel(image);
        background.setBounds(0, 0, 762, 587);
        background.setLayout(null);

        // ================= TITLE =================
        JTextArea title = new JTextArea(book.getTitle());
        title.setWrapStyleWord(true);
        title.setLineWrap(true);
        title.setEditable(false);
        title.setOpaque(false);
        title.setFont(new Font("Poppins", Font.BOLD, 30));
        title.setBounds(200, 90, 500, 80);

        // ================= BOOK ID =================
        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setBounds(200, 190, 300, 22);
        bookIdLabel.setFont(new Font("Poppins", Font.PLAIN, 15));

        JLabel bookId = new JLabel(book.getBookId());
        bookId.setBounds(200, 215, 300, 22);
        bookId.setFont(new Font("Poppins", Font.PLAIN, 20));

        // ================= AUTHOR =================
        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(200, 250, 300, 25);
        authorLabel.setFont(new Font("Poppins", Font.PLAIN, 15));

        JLabel author = new JLabel(book.getAuthor());
        author.setBounds(200, 270, 300, 25);
        author.setFont(new Font("Poppins", Font.PLAIN, 17));

        // ================= QUANTITY =================
        JLabel quantity = new JLabel("Quantity: " + book.getQuantity());
        quantity.setBounds(500, 290, 300, 25);
        quantity.setFont(new Font("Poppins", Font.PLAIN, 15));

        // ================= COVER =================
        JLabel bookCover = new JLabel();
        bookCover.setBounds(29, 90, 158, 238);
        background.add(bookCover);

        new Thread(() -> {
            try {
                if (book.getCoverURL() == null || book.getCoverURL().isEmpty())
                    return;

                URL url = new URL(book.getCoverURL());
                BufferedImage imageBuffer = ImageIO.read(url);

                if (imageBuffer == null) return;

                Image scaled = imageBuffer.getScaledInstance(
                        153, 223, Image.SCALE_SMOOTH);

                SwingUtilities.invokeLater(() ->
                        bookCover.setIcon(new ImageIcon(scaled)));

            } catch (Exception ignored) {}
        }).start();

        // ================= DESCRIPTION =================
        JTextArea description = new JTextArea(book.getDescription());
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFont(new Font("Poppins", Font.PLAIN, 13));
        description.setBackground(new Color(0xF1F3F6));
        description.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));

        JScrollPane scrollPane = new JScrollPane(description);
        scrollPane.setBounds(33, 355, 700, 150);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(new Color(0xF1F3F6));

        scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            protected JButton createDecreaseButton(int o){ return new JButton(); }
            protected JButton createIncreaseButton(int o){ return new JButton(); }
        });

        // ================= CLOSE BUTTON =================
        JButton close = new JButton();
        close.setBounds(724, 13, 25, 25);
        close.setBorder(null);
        close.setContentAreaFilled(false);
        close.setFocusPainted(false);

        close.addActionListener(e -> {
            if (onClose != null) onClose.run();
        });

        // ================= ADD =================
        background.add(title);
        background.add(bookIdLabel);
        background.add(bookId);
        background.add(authorLabel);
        background.add(author);
        background.add(quantity);
        background.add(scrollPane);
        background.add(close);

        add(background);
    }
}
