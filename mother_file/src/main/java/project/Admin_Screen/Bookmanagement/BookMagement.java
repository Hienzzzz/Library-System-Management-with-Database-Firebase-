package project.Admin_Screen.Bookmanagement;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import  com.google.firebase.database.ValueEventListener;

import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;
import project.Main_System.MainFrame;



public class BookMagement extends javax.swing.JPanel {
    private MainFrame frame;
    private JTable table;
    private  DefaultTableModel model;

    public BookMagement(MainFrame frame){
        this.frame = frame;
        panel();
    }
    public void panel(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_BookManagement.png")
        );

        JLabel background = new JLabel();
        background.setBounds(0,0,1512,982);
        background.setLayout(null);
        background.setIcon(icon);


        String[] colums = {"Title", "Book ID", "Author", "Quantity"};
        model = new DefaultTableModel(colums, 0);

        JTable table = new JTable(model);
        table.setRowHeight(28);

        Color bg = new Color(255, 255, 255);
        table.setBackground(bg);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(371, 510, 1030, 412);
        scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
        );

        scrollPane.setBackground(bg);
        scrollPane.getViewport().setBackground(bg);

        scrollPane.setBorder(BorderFactory.createEmptyBorder());

    // set widths first
    table.getColumnModel().getColumn(0).setPreferredWidth(412);
    table.getColumnModel().getColumn(1).setPreferredWidth(195);
    table.getColumnModel().getColumn(2).setPreferredWidth(244);
    table.getColumnModel().getColumn(3).setPreferredWidth(107);
    
    

    // disable user changes
    table.getTableHeader().setReorderingAllowed(false);
    table.getTableHeader().setResizingAllowed(false);

    //border
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    table.setShowGrid(false);
    table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
    table.getTableHeader().setOpaque(true);
    

    // lock widths
    for (int i = 0; i < table.getColumnCount(); i++) {
        int w = table.getColumnModel().getColumn(i).getPreferredWidth();
        table.getColumnModel().getColumn(i).setMinWidth(w);
        table.getColumnModel().getColumn(i).setMaxWidth(w);
    }
    // table header font
        JTableHeader header = table.getTableHeader();

        
        header.setFont(new Font("Poppins", Font.PLAIN, 18));
        header.setBackground(new Color(241, 243, 246));
        header.setForeground(Color.gray);
        header.setOpaque(true);
        header.setBorder(BorderFactory.createEmptyBorder());

        // optional alignment
        DefaultTableCellRenderer headerRenderer =
        (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);   
        table.setTableHeader(null);
        scrollPane.setColumnHeaderView(null);

        
        
        

        background.add(scrollPane);
        this.add(background);

    }

        //firebase listener 

        private void loadBooks(){

            BookService.getRef().addValueEventListener(new ValueEventListener() {
                
               @Override
               public void onDataChange(DataSnapshot snapshot){
                SwingUtilities.invokeLater(() ->{
                    model.setRowCount(0);

                    for(DataSnapshot data : snapshot.getChildren()){
                        Books book = data.getValue(Books.class);

                        model.addRow(new Object[]{
                            book.getTitle(),
                            book.getBook_id(),
                            book.getAuthor(),
                            book.getQuantity(),
                            book.getGenre()
                        });
                    }
                });
               } 

               @Override
               public void onCancelled(DatabaseError error){
                System.out.println("Firebase error: " + error.getMessage());
               }
            });
        }


    
    
}
