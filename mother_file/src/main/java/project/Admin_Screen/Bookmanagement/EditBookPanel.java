package project.Admin_Screen.Bookmanagement;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.text.NumberFormatter;

import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;
import project.Firebase_backend.Storage_backend.ImageService;




public class EditBookPanel  extends  JPanel{
    private BookMagement parent;
    private Books book;
    private Runnable onClose;
    private JLabel imgPreview;

    public EditBookPanel(BookMagement parent, Books book, Runnable onClose) {
        this.parent = parent;
        this.book = book;
        this.onClose = onClose;
        this.imgPreview = imgPreview;
       

        setLayout(null);
        setBounds(439, 308, 762, 587);
        setOpaque(false);

        // background image
        JLabel background = new JLabel(
            new ImageIcon(getClass().getResource("/Images/ADmin_editBook.png"))
        );
        background.setBounds(0, 0, 762, 587);
        background.setLayout(null);

        
        Color fieldColor = new Color(241, 243, 246);

        JTextField Title = new JTextField();
        Title.setBounds(216, 127, 500, 33);
        Title.setBackground(fieldColor);
        Title.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        Title.setText(book.getTitle());
        background.add(Title);

        JTextField Author = new JTextField();
        Author.setBounds(215, 211, 405, 33);
        Author.setBackground(fieldColor);
        Author.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        Author.setText(book.getAuthor());
        background.add(Author);

        JTextField Genre = new JTextField();
        Genre.setBounds(215, 295, 335, 33);
        Genre.setBackground(fieldColor);
        Genre.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        Genre.setText(book.getGenre());
        background.add(Genre);

        final int MAX_QUANTITY = 20;

        SpinnerNumberModel quantityModel =
        new SpinnerNumberModel(0, 0, MAX_QUANTITY, 1);

        JSpinner quantityButton = new JSpinner(quantityModel);
        quantityButton.setBounds(680, 295, 45, 31);

        JSpinner.NumberEditor editor =
        new JSpinner.NumberEditor(quantityButton, "#");
        quantityButton.setEditor(editor);

        JFormattedTextField textField = editor.getTextField();

        NumberFormatter formatter = (NumberFormatter) textField.getFormatter();
        formatter.setAllowsInvalid(false); 
        formatter.setMinimum(0);          
        formatter.setMaximum(MAX_QUANTITY);

        textField.setBackground(fieldColor);
        textField.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 5));
        quantityButton.setValue(book.getQuantity());

        background.add(quantityButton);

// =======================================================

        JTextArea description = new JTextArea();
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setBackground(fieldColor);
        description.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        

        JScrollPane scrollPane = new JScrollPane(description);
        scrollPane.setBounds(214, 386, 505, 117);
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
        description.setText(book.getDescription());
        background.add(scrollPane);

        JButton saved_button = new JButton();
        saved_button.setBounds(555, 539, 183, 38);
        saved_button.setBorder(null);
        saved_button.setContentAreaFilled(false);
        saved_button.setFocusPainted(false);
        saved_button.setOpaque(false);
        background.add(saved_button);

        JButton cancel_button = new JButton();
        cancel_button.setBounds(27, 538, 130, 38);
        cancel_button.setBorder(null);
        cancel_button.setContentAreaFilled(false);
        cancel_button.setFocusPainted(false);
        cancel_button.setOpaque(false);
        background.add(cancel_button);

        

        JButton UploadImage = new JButton();
        UploadImage.setBounds(17, 324, 175, 42);
        UploadImage.setBorder(null);
        UploadImage.setContentAreaFilled(false);
        UploadImage.setFocusPainted(false);
        UploadImage.setOpaque(false);
        background.add(UploadImage);

        imgPreview = new JLabel();
        imgPreview.setBounds(26, 85, 153, 223);
        imgPreview.setBackground(Color.BLUE);
        background.add(imgPreview);
        
        if (book.getCoverURL() != null && !book.getCoverURL().isEmpty()) {
            loadCoverImage(book.getCoverURL());
        }


        final File[] selectedFile = new File[1];
        
        UploadImage.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(
                new FileNameExtensionFilter(
                    "Image Files", "jpg", "png", "jpeg"
                )
            );

            if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
                selectedFile[0] = chooser.getSelectedFile();

                ImageIcon icon = new ImageIcon(
                    new ImageIcon(selectedFile[0].getAbsolutePath())
                    .getImage().getScaledInstance(153, 223, Image.SCALE_SMOOTH)
                );
                imgPreview.setIcon(icon);
            }
        });
        
        cancel_button.addActionListener(e -> {

        int result = JOptionPane.showConfirmDialog(
            this,
            "Cancel editing this book?\nAll entered data will be lost.",
            "Confirm Cancel",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
            );

            
            if (result != JOptionPane.YES_OPTION) return;

            onClose.run(); 
        });
        saved_button.addActionListener(e -> {

            if (Title.getText().isEmpty()
                || Author.getText().isEmpty()
                || Genre.getText().isEmpty()
                || description.getText().isEmpty()) {

                JOptionPane.showMessageDialog(
                    this,
                    "Please fill in required fields",
                    "Invalid",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

                book.setTitle(Title.getText().trim());
                book.setAuthor(Author.getText().trim());
                book.setGenre(Genre.getText().trim());
                book.setQuantity((int) quantityButton.getValue());
                book.setDescription(description.getText().trim());;

            if (selectedFile[0] != null) {

       
                if (book.getCoverURL() != null && !book.getCoverURL().isEmpty()) {
                    ImageService.deleteBookCoverByUrl(book.getCoverURL());
                }

                String newCover = ImageService.uploadBookCover(
                    selectedFile[0],
                    book.getBookId()
                );

                if (newCover != null) {
                    book.setCoverURL(newCover);
                }

            }


            
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Save changes to this book?",
                    "Confirm Update",
                    JOptionPane.YES_NO_OPTION
                );

                if (confirm != JOptionPane.YES_OPTION) return;

                Map<String, Object> updates = new HashMap<>();
                updates.put("title", Title.getText().trim());
                updates.put("author", Author.getText().trim());
                updates.put("genre", Genre.getText().trim());
                updates.put("description", description.getText().trim());
                updates.put("quantity", (int) quantityButton.getValue());
                updates.put("coverURL", book.getCoverURL());

                
                BookService.updateBookFields(book.getBookId(), updates);

        
            JOptionPane.showMessageDialog(
                this,
                "Book updated successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE
            );

            onClose.run();
        });





        this.add(background);
    }
    private void loadCoverImage(String url) {
    new Thread(() -> {
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(url));
            Image img = icon.getImage().getScaledInstance(
                153, 223, Image.SCALE_SMOOTH
            );

            javax.swing.SwingUtilities.invokeLater(() ->
                imgPreview.setIcon(new ImageIcon(img))
            );

        } catch (Exception e) {
            System.err.println("Failed to load cover image: " + e.getMessage());
        }
    }).start();
}

}
