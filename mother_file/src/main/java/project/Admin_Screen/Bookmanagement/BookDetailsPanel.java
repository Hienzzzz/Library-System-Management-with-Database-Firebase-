package project.Admin_Screen.Bookmanagement;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;

public class BookDetailsPanel extends JPanel {

   

    public BookDetailsPanel(Books book, Runnable onClose){
       

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
        title.setFont(new Font("Poppins", Font.PLAIN, 30));
        title.setBounds(216, 127, 500, 33);

        JLabel bookId = new JLabel("Book ID: " + book.getBookId());
        bookId.setBounds(216, 190, 300, 22);

        JLabel author = new JLabel("Author: " + book.getAuthor());
        author.setBounds(216, 160, 300, 25);

        JLabel genre = new JLabel("Genre: " + book.getGenre());
        genre.setBounds(216, 220, 300, 25);

        JLabel quantity = new JLabel("Quantity: " + book.getQuantity());
        quantity.setBounds(216, 260, 300, 25);

        JTextArea description = new JTextArea(book.getDescription());
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setEditable(false);
        description.setBounds(30, 220, 590, 200);


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
