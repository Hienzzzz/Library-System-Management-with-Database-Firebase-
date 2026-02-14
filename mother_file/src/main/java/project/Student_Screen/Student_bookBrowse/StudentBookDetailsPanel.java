package project.Student_Screen.Student_bookBrowse;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;

import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;

public class StudentBookDetailsPanel extends JPanel {

    private final Books book;
    private final Runnable onClose;

    public StudentBookDetailsPanel(Books book, Runnable onClose) {

        this.book = book;
        this.onClose = onClose;

        setLayout(null);
        setBounds(439, 270, 762, 587);
        setOpaque(false);

        ImageIcon image = new ImageIcon(
                getClass().getResource("/Images/Student_bookdetails.png")
        );

        JLabel background = new JLabel(image);
        background.setBounds(0, 0, 762, 587);
        background.setLayout(null);

        // ================= TITLE =================
        JTextArea title = new JTextArea(book.getTitle());
        title.setWrapStyleWord(true);
        title.setLineWrap(true);
        title.setEditable(false);
        title.setFocusable(false);
        title.setOpaque(false);
        title.setFont(new Font("Poppins", Font.BOLD, 30));
        title.setBounds(200, 90, 500, 80);

        // ================= BOOK INFO =================
        JLabel bookIdLabel = new JLabel("Book ID:");
        bookIdLabel.setBounds(200, 190, 300, 22);
        bookIdLabel.setFont(new Font("Poppins", Font.PLAIN, 15));

        JLabel bookId = new JLabel(book.getBookId());
        bookId.setBounds(200, 215, 300, 22);
        bookId.setFont(new Font("Poppins", Font.PLAIN, 20));

        JLabel authorLabel = new JLabel("Author:");
        authorLabel.setBounds(200, 250, 300, 25);
        authorLabel.setFont(new Font("Poppins", Font.PLAIN, 15));

        JLabel author = new JLabel(book.getAuthor());
        author.setBounds(200, 270, 300, 25);
        author.setFont(new Font("Poppins", Font.PLAIN, 17));

        JLabel quantity = new JLabel("Quantity: " + book.getQuantity());
        quantity.setBounds(500, 290, 300, 25);
        quantity.setFont(new Font("Poppins", Font.PLAIN, 15));

        // ================= COVER IMAGE =================
        JLabel bookCover = new JLabel();
        bookCover.setBounds(29, 90, 158, 238);

        new Thread(() -> {
            try {
                if (book.getCoverURL() == null || book.getCoverURL().isEmpty())
                    return;

                URL url = new URL(book.getCoverURL());
                BufferedImage bufferedImage = ImageIO.read(url);
                if (bufferedImage == null) return;

                Image scaled = bufferedImage.getScaledInstance(
                        153, 223, Image.SCALE_SMOOTH);

                SwingUtilities.invokeLater(() ->
                        bookCover.setIcon(new ImageIcon(scaled))
                );

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

        JScrollPane descScroll = new JScrollPane(description);
        descScroll.setBounds(33, 355, 700, 150);
        descScroll.setBorder(null);
        descScroll.getViewport().setBackground(new Color(0xF1F3F6));

        descScroll.setBorder(null);
        descScroll.setOpaque(false);
        descScroll.getViewport().setOpaque(false);

        descScroll.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));

        descScroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

            @Override
            protected void configureScrollBarColors() {
                thumbColor = new Color(0xB0B8C4);
                trackColor = new Color(0xF1F3F6);
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
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }

            @Override
            protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(thumbColor);
                g2.fillRoundRect(
                        r.x + 2,
                        r.y + 2,
                        r.width - 4,
                        r.height - 4,
                        10,
                        10
                );

                g2.dispose();
            }

            @Override
            protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
                g.setColor(trackColor);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
        });

        // ================= CLOSE BUTTON =================
        JButton closeButton = new JButton();
        closeButton.setBounds(724, 13, 25, 25);
        closeButton.setBorder(null);
        closeButton.setContentAreaFilled(false);
        closeButton.addActionListener(e -> onClose.run());

        // ================= REQUEST BUTTON =================
        JButton requestButton = new JButton();
        requestButton.setBounds(600, 531, 137, 38);
        requestButton.setBorder(null);
        requestButton.setContentAreaFilled(false);
        requestButton.setFocusPainted(false);
        requestButton.setOpaque(false);

        requestButton.addActionListener(e -> {

            if (book.getQuantity() <= 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Book is out of stock.",
                        "Unavailable",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Do you want to request this book?",
                    "Confirm Request",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            Map<String, Object> request = new HashMap<>();
            request.put("studentId", "CURRENT_STUDENT_ID"); // replace later
            request.put("bookId", book.getBookId());
            request.put("status", "Pending");
            request.put("timestamp", System.currentTimeMillis());

            BookService.getRef()
                    .getParent()
                    .child("book_requests")
                    .push()
                    .setValue(request, (error, ref) -> {

                        if (error != null) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Request failed.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Request sent successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                            onClose.run();
                        }
                    });
        });

        // ================= ADD COMPONENTS =================
        background.add(title);
        background.add(bookIdLabel);
        background.add(bookId);
        background.add(authorLabel);
        background.add(author);
        background.add(quantity);
        background.add(bookCover);
        background.add(descScroll);
        background.add(closeButton);
        background.add(requestButton);

        add(background);
    }
}
