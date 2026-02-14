package project.Admin_Screen.Bookmanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import project.Admin_Screen.Admin_accountManagement.Admin_AccountManagement;
import project.Admin_Screen.Dashboard.AdminDashboard;
import project.Admin_Screen.Report_screen.Reports;
import project.Admin_Screen.Studentmanagement.StudentManagement;
import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;
import project.Main_System.MainFrame;

public class AvailabaleBooks extends JPanel {

    private MainFrame frame;
    private JTable table;
    private DefaultTableModel model;

    public AvailabaleBooks(MainFrame frame) {
        this.frame = frame;
        panel();
        loadAvailableBooks();
    }

    public void panel() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_AvailableBook.png")
        );

        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        // ================= SIDEBAR BUTTONS =================

        background.add(createSideButton("Dashboard",12,240,238,49,
                () -> frame.setContentPane(new AdminDashboard(frame))));
        background.add(createSideButton("Reports",12,297,238,49,
                () -> frame.setContentPane(new Reports(frame))));
        background.add(createSideButton("Book Management",12,350,238,49,
                () -> frame.setContentPane(new BookManagement(frame))));
        background.add(createSideButton("Student Management",12,564,238,49,
                () -> frame.setContentPane(new StudentManagement(frame))));
        background.add(createSideButton("Admin Management",12,615,238,49,
                () -> frame.setContentPane(new Admin_AccountManagement(frame))));

        // ================= BOOK TABS =================

        background.add(createTabButton("Available Books",55,405,
                () -> frame.setContentPane(new AvailabaleBooks(frame))));
        background.add(createTabButton("Borrowed Books",55,442,
                () -> frame.setContentPane(new BorrowedBook(frame))));
        background.add(createTabButton("Pending Request",55,477,
                () -> frame.setContentPane(new Pendingreq(frame))));
        background.add(createTabButton("Overdue",55,514,
                () -> frame.setContentPane(new Overdue(frame))));

        initTable(background);

        add(background);
    }

    // ================= TABLE =================

    private void initTable(JLabel background){

        String[] columns = {
                "Book ID",
                "Title",
                "Author",
                "Category",
                "Available Quantity"
        };

        model = new DefaultTableModel(columns,0){
            public boolean isCellEditable(int row,int col){
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setTableHeader(null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        int[] widths = {150,300,250,200,150};

        for(int i=0;i<widths.length;i++){
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
            column.setMinWidth(widths[i]);
            column.setMaxWidth(widths[i]);
        }

        table.setDefaultRenderer(Object.class,new AvailableRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(373,383,1025,530);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.getVerticalScrollBar().setUI(new ModernScroll());

        background.add(scroll);
    }

    // ================= LOAD AVAILABLE BOOKS =================

    private void loadAvailableBooks(){

        DatabaseReference ref =
                BookService.getRef();

        ref.addValueEventListener(new ValueEventListener(){

            public void onDataChange(DataSnapshot snapshot){

                model.setRowCount(0);

                for(DataSnapshot data : snapshot.getChildren()){

                    Books book = data.getValue(Books.class);
                    if(book == null) continue;

                    if(book.getQuantity() <= 0) continue;

                    model.addRow(new Object[]{
                            book.getBookId(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getGenre(),
                            book.getQuantity()
                    });
                }
            }

            public void onCancelled(DatabaseError error){}
        });
    }

    // ================= RENDERER =================

    private class AvailableRenderer
            extends DefaultTableCellRenderer {

        public Component getTableCellRendererComponent(
                JTable table,Object value,
                boolean isSel,boolean hasFocus,
                int row,int col){

            super.getTableCellRendererComponent(
                    table,value,false,false,row,col);

            setBackground(row%2==0
                    ? new Color(245,245,245)
                    : Color.WHITE);

            setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
            return this;
        }
    }

    // ================= MODERN SCROLL =================

    private static class ModernScroll
            extends BasicScrollBarUI{

        protected JButton createDecreaseButton(int o){
            return new JButton();
        }
        protected JButton createIncreaseButton(int o){
            return new JButton();
        }
    }

    // ================= BUTTON HELPERS =================

    private TButton createSideButton(
            String text,int x,int y,int w,int h,
            Runnable action){

        TButton btn = new TButton(text);
        btn.setBounds(x,y,w,h);
        btn.setFont(MainFrame.loadSanchez(15f));
        btn.setForeground(new Color(93,93,93));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0,60,0,0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            action.run();
            frame.revalidate();
        });
        return btn;
    }

    private TButton createTabButton(
            String text,int x,int y,
            Runnable action){

        TButton btn = new TButton(text);
        btn.setBounds(x,y,200,35);
        btn.setFont(MainFrame.loadSanchez(13f));
        btn.setForeground(new Color(93,93,93));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> {
            action.run();
            frame.revalidate();
        });
        return btn;
    }

    // ================= CUSTOM BUTTON =================

    class TButton extends JButton {

        private float alpha = 0f;
        private final float SPEED = 0.08f;
        private javax.swing.Timer fadeIn;
        private javax.swing.Timer fadeOut;

        public TButton(String text) {
            super(text);

            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);

            fadeIn = new javax.swing.Timer(16, e -> {
                alpha = Math.min(alpha + SPEED, 1f);
                repaint();
                if (alpha >= 1f)
                    ((javax.swing.Timer)e.getSource()).stop();
            });

            fadeOut = new javax.swing.Timer(16, e -> {
                alpha = Math.max(alpha - SPEED, 0f);
                repaint();
                if (alpha <= 0f)
                    ((javax.swing.Timer)e.getSource()).stop();
            });

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){
                    fadeOut.stop();
                    fadeIn.start();
                }
                public void mouseExited(MouseEvent e){
                    fadeIn.stop();
                    fadeOut.start();
                }
            });
        }

        protected void paintComponent(Graphics g){
            Graphics2D g2=(Graphics2D)g;
            g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            if(alpha>0f){
                int a=(int)(alpha*77);
                g2.setColor(new Color(205,205,205,a));
                g2.fillRoundRect(0,0,getWidth(),
                        getHeight(),30,30);
            }

            super.paintComponent(g);
        }
    }
}
