package project.Admin_Screen.Bookmanagement;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;

public class BookDetailsPanel extends JPanel {

   private BookMagement parent;

    public BookDetailsPanel(Books book, Runnable onClose){
        this.parent = parent;
       

        setLayout(null);
        setBounds(439, 270, 762, 587);
        setOpaque(false);

        ImageIcon image = new ImageIcon(
            getClass().getResource("/Images/Admin_viewBook.png")
        );

        JLabel background = new JLabel(image);
        background.setBounds(0, 0, 762, 587);
        background.setLayout(null);

       
        
        JLabel title = new JLabel(book.getTitle());
        title.setFont(new Font("Poppins", Font.BOLD, 30));
        title.setBounds(216, 127, 500, 33);

        JLabel bookId = new JLabel("Book ID: " + book.getBookId());
        bookId.setBounds(216, 190, 300, 22);

        JLabel author = new JLabel("Author: " + book.getAuthor());
        author.setBounds(216, 160, 300, 25);

        JLabel genre = new JLabel("Genre: " + book.getGenre());
        genre.setBounds(216, 220, 300, 25);

        JLabel status = new JLabel("Status: " + book.getStatus());
        status.setBounds(520, 284, 300, 25);
        status.setFont(new Font("Poppins", Font.BOLD, 15));

       JLabel book_cover = new JLabel();
        book_cover.setBounds(26, 85, 153, 223);
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

        JLabel quantity = new JLabel("Quantity: " + book.getQuantity());
        quantity.setBounds(520, 300, 300, 25);
        quantity.setFont(new Font("Poppins", Font.BOLD, 15));

        JTextArea description = new JTextArea(book.getDescription());
        description.setBounds(33, 355, 700, 150);
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setFont(new Font("Poppins", Font.PLAIN, 13));
        //description.setBackground(new Color(0xF1F3F6));


        JButton edit_button = new JButton();
        edit_button.setBounds(452, 531, 117, 38);
       // edit_button.setBorder(null);
        edit_button.setContentAreaFilled(false);
        edit_button.setFocusPainted(false);
        edit_button.setOpaque(false);

        edit_button.addActionListener(e ->{
            int result = JOptionPane.showConfirmDialog(this, 
                        "Do you want to edit his book?",
                        "confirm edit",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                        
            );

            if(result == JOptionPane.YES_OPTION){
                
            }else{

            }
        });
       


        JButton delete_button = new JButton();
        delete_button.setBounds(600, 531, 137, 38);
        //delete_button.setBorder(null);
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

        JButton close_button = new JButton();
        close_button.setBounds(726, 13, 25, 25);
        close_button.setContentAreaFilled(false);
        close_button.setBorder(null);
        close_button.setFocusPainted(false);
        close_button.setOpaque(false);

        close_button.addActionListener(e -> onClose.run());


        background.add(status);
        background.add(close_button);
        background.add(description);
        background.add(quantity);
        background.add(genre);
        background.add(author);
        background.add(bookId);
        background.add(title);
        background.add(delete_button);
        background.add(edit_button);
        this.add(background);

    }
    
}
