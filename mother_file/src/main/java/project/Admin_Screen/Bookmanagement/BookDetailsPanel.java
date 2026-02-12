package project.Admin_Screen.Bookmanagement;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicScrollBarUI;

import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;

public class BookDetailsPanel extends JPanel {

    private BookManagement parent;
    private Books book;
    private Runnable onClose;



    public BookDetailsPanel(BookManagement parent, Books book, Runnable onClose){
        this.parent = parent;
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

       
        
        JTextArea title = new JTextArea(book.getTitle());
        title.setWrapStyleWord(true);
        title.setLineWrap(true);
        title.setEditable(false);
        title.setFocusable(false);
        title.setOpaque(false);
        title.setFont(new Font("Poppins", Font.BOLD, 30));
        title.setBounds(200, 90, 500, 80);

        JLabel bookId_textHolder = new JLabel("Book ID: " );
        bookId_textHolder.setBounds(200, 190, 300, 22);
        bookId_textHolder.setFont(new Font("Poppins", Font.PLAIN, 15));

        JLabel bookId = new JLabel(book.getBookId());
        bookId.setBounds(200, 215, 300, 22);
        bookId.setFont(new Font("Poppins", Font.PLAIN, 20));

        JLabel author_textHolder = new JLabel("Author: ");
        author_textHolder.setBounds(200, 250, 300, 25);
        author_textHolder.setFont(new Font("Poppins", Font.PLAIN, 15));

        JLabel author = new JLabel(book.getAuthor());
        author.setBounds(200, 270, 300, 25);
        author.setFont(new Font("Poppins", Font.PLAIN, 17));

       // ---- Genre label title ----
        JLabel genre_textHolder = new JLabel("Genre:");
        genre_textHolder.setBounds(500, 190, 80, 25);
        genre_textHolder.setFont(new Font("Poppins", Font.PLAIN, 15));
        genre_textHolder.setForeground(new Color(0x333333));


        // ---- Genre value label ----
        JLabel genreLabel = new JLabel(book.getGenre());
        genreLabel.setFont(new Font("Poppins", Font.PLAIN, 17));
        genreLabel.setForeground(new Color(0x333333));
        genreLabel.setOpaque(false);

        // ---- Scroll container ----
        JScrollPane genreScroll = new JScrollPane(
                genreLabel,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        genreScroll.setBounds(500, 210, 200, 25);
        genreScroll.setBorder(null);
        genreScroll.setOpaque(false);
        genreScroll.getViewport().setOpaque(false);

        // Hide scrollbar by default
        JScrollBar hBar = genreScroll.getHorizontalScrollBar();
        hBar.setPreferredSize(new Dimension(0, 0));
        hBar.setOpaque(false);
        hBar.setVisible(false);

        // Show scrollbar only when hovering or scrolling
        genreScroll.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                hBar.setPreferredSize(new Dimension(0, 3));
                hBar.setVisible(true);
                genreScroll.revalidate();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                hBar.setPreferredSize(new Dimension(0, 0));
                hBar.setVisible(false);
                genreScroll.revalidate();
            }
        });
        hBar.setUI(new BasicScrollBarUI() {
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
                JButton b = new JButton();
                b.setPreferredSize(new Dimension(0, 0));
                b.setMinimumSize(new Dimension(0, 0));
                b.setMaximumSize(new Dimension(0, 0));
                return b;
            }
        });
       
        JLabel status_textHolder = new JLabel("Status: " );
        status_textHolder.setBounds(500, 240, 300, 25);
        status_textHolder.setFont(new Font("Poppins", Font.PLAIN, 15));

        JLabel status = new JLabel(book.getStatus());
        status.setBounds(500, 260, 300, 25);
        status.setFont(new Font("Poppins", Font.PLAIN, 17));

        JLabel quantity = new JLabel("Quantity: " + book.getQuantity());
        quantity.setBounds(500, 290, 300, 25);
        quantity.setFont(new Font("Poppins", Font.PLAIN, 15));

        JLabel book_cover = new JLabel();
        book_cover.setBounds(29, 90, 158, 238);
        book_cover.setOpaque(false);
        book_cover.setBackground(Color.GRAY);
        
        background.add(book_cover);

        new Thread(() -> {
            try {
                if (book.getCoverURL() == null || book.getCoverURL().isEmpty()) {
                    System.out.println("Cover URL is null or empty");
                    return;
                }

                URL url = new URL(book.getCoverURL());

                // FORCE full load
                BufferedImage bufferedImage = ImageIO.read(url);
                if (bufferedImage == null) {
                    System.out.println("Failed to load image from URL");
                    return;
                }

                Image scaled = bufferedImage.getScaledInstance(
                        153, 223,
                        Image.SCALE_SMOOTH
                );

                SwingUtilities.invokeLater(() -> {
                    book_cover.setIcon(new ImageIcon(scaled));
                    book_cover.revalidate();
                    book_cover.repaint();
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        

JTextArea description = new JTextArea(book.getDescription());
description.setWrapStyleWord(true);
description.setLineWrap(true);
description.setEditable(false);
description.setFont(new Font("Poppins", Font.PLAIN, 13));
description.setForeground(new Color(0x333333));
description.setBackground(new Color(0xF1F3F6));
description.setBorder(BorderFactory.createEmptyBorder(10, 12, 10, 12));
description.setCaretPosition(0);
description.setFocusable(false);
description.setBorder(null);
description.setFocusable(false);
description.setFocusable(false);
description.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));



JScrollPane scrollPane = new JScrollPane(description);
scrollPane.setBounds(33, 355, 700, 150);
//scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0xDADDE2)));
scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
scrollPane.getVerticalScrollBar().setUnitIncrement(16);
scrollPane.setBackground(Color.WHITE);
scrollPane.getViewport().setBackground(new Color(0xF1F3F6));
scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, new JPanel());
scrollPane.setBorder(null);
scrollPane.setOpaque(false);
scrollPane.getViewport().setOpaque(false);




Color SCROLL_TRACK = new Color(0xF1F3F6);
Color SCROLL_THUMB = new Color(0xB0B8C4); // soft gray-blue
Color SCROLL_THUMB_HOVER = new Color(0x8FA1B5);

scrollPane.setBorder(null);

scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {

        @Override
        protected void configureScrollBarColors() {
            thumbColor = SCROLL_THUMB;
            trackColor = SCROLL_TRACK;
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

            g2.setColor(isThumbRollover() ? SCROLL_THUMB_HOVER : SCROLL_THUMB);
            g2.fillRoundRect(r.x, r.y, r.width, r.height, 8, 8);
            g2.dispose();
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
            g.setColor(SCROLL_TRACK);
            g.fillRect(r.x, r.y, r.width, r.height);
        }
    });

        JButton close_Button = new JButton();
        close_Button.setBounds(724, 13, 25, 25);
        close_Button.setBorder(null);
        close_Button.setContentAreaFilled(false);
        close_Button.setFocusPainted(false);
        close_Button.setOpaque(false);

        close_Button.addActionListener(e -> {

            
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to Close this Panel?",
                "Confirm Close Panel",
                JOptionPane.YES_NO_OPTION
            );
                if (confirm == JOptionPane.YES_OPTION){
                    onClose.run();
                }
        });


        

        JButton edit_button = new JButton();
        edit_button.setBounds(452, 531, 117, 38);
        edit_button.setBorder(null);
        edit_button.setContentAreaFilled(false);
        edit_button.setFocusPainted(false);
        edit_button.setOpaque(false);

        edit_button.addActionListener(e -> {

            int result = JOptionPane.showConfirmDialog(
                this,
                "Do you want to edit this book?",
                "Confirm Edit",
                JOptionPane.YES_NO_OPTION
            );

            if (result != JOptionPane.YES_OPTION) return;

            
            onClose.run();

            final EditBookPanel[] editPanelHolder = new EditBookPanel[1];

            editPanelHolder[0] = new EditBookPanel(
                parent,
                book,   
                () -> {
                    parent.getLayeredPaneRef().remove(editPanelHolder[0]);
                    parent.hideDimOverlay();
                    parent.getLayeredPaneRef().revalidate();
                    parent.getLayeredPaneRef().repaint();
                }
            );

            parent.showDimOverlay();

            parent.getLayeredPaneRef().add(
                editPanelHolder[0],
                JLayeredPane.POPUP_LAYER
            );

            parent.getLayeredPaneRef().revalidate();
            parent.getLayeredPaneRef().repaint();
        });





        JButton delete_button = new JButton();
        delete_button.setBounds(600, 531, 137, 38);
        delete_button.setBorder(null);
        delete_button.setContentAreaFilled(false);
        delete_button.setFocusPainted(false);
        delete_button.setOpaque(false);

        delete_button.addActionListener(e -> {

            if(book.getBorrowedCount() > 0){
                delete_button.setEnabled(false);
                delete_button.setToolTipText("Book us currenltu borrowed");

            }

            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this book?\nThis action cannot be undone",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
            );
                if (confirm == JOptionPane.YES_OPTION){
                    BookService.deleteBook(book.getBookId());
                    onClose.run();
                }
        });

       


        background.add(close_Button);
        background.add(status_textHolder);
        background.add(genre_textHolder);
        background.add(author_textHolder);
        background.add(bookId_textHolder);
        background.add(status);
        background.add(scrollPane);
        background.add(quantity);
        background.add(genreScroll);
        background.add(author);
        background.add(bookId);
        background.add(title);
        background.add(delete_button);
        background.add(edit_button);
        this.add(background);

    }
    
}