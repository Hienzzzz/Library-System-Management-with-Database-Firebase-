package project.Student_Screen.Student_borrowedBook;

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
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;
import project.Main_System.MainFrame;
import project.Student_Screen.Student_BookRequest.Book_request;
import project.Student_Screen.Student_bookBrowse.Book_browse;
import project.Student_Screen.Student_dashboard.Student_Dashboard;
import project.Student_Screen.Student_highlights.Highlights;

public class My_borrowed_Books extends JPanel {

    private MainFrame frame;
    private JTable table;
    private DefaultTableModel model;

    // ⚠ Replace with real login later
    private static final String CURRENT_STUDENT_ID = "CURRENT_STUDENT_ID";

    static {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    public My_borrowed_Books(MainFrame frame){
        this.frame = frame;
        panel();
        setupTable();
        loadBorrowedBooks();
    }

    public void panel(){

        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
            getClass().getResource("/Images/Student_borrowedBooks.png")
        );

        JLabel background = new JLabel(icon);
        background.setBounds(0,0,1512,982);
        background.setLayout(null);

        // ========== YOUR BUTTON SYSTEM (UNCHANGED) ==========
        class TButton extends JButton {
            private float alpha = 0f;
            private final float SPEED = 0.08f;
            private Timer fadeIn;
            private Timer fadeOut;

            public TButton(String text) {
                super(text);
                setContentAreaFilled(false);
                setBorderPainted(false);
                setFocusPainted(false);
                setOpaque(false);

                fadeIn = new Timer(16, e -> {
                    alpha = Math.min(alpha + SPEED, 1f);
                    repaint();
                    if (alpha >= 1f) ((Timer)e.getSource()).stop();
                });

                fadeOut = new Timer(16, e -> {
                    alpha = Math.max(alpha - SPEED, 0f);
                    repaint();
                    if (alpha <= 0f) ((Timer)e.getSource()).stop();
                });

                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        fadeOut.stop();
                        fadeIn.start();
                    }
                    public void mouseExited(MouseEvent e) {
                        fadeIn.stop();
                        fadeOut.start();
                    }
                });
            }

            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);

                if (alpha > 0f) {
                    int a = (int) (alpha * 77);
                    g2.setColor(new Color(205,205,205,a));
                    g2.fillRoundRect(0,0,getWidth(),getHeight(),30,30);
                }
                super.paintComponent(g);
            }
        }

        TButton DashBoard = new TButton("Student Dashboard");
        DashBoard.setBounds(12,240,238,49);
        DashBoard.setFont(MainFrame.loadSanchez(15f));
        DashBoard.setForeground(new Color(93,93,93));
        DashBoard.setHorizontalAlignment(SwingConstants.LEFT);
        DashBoard.setMargin(new Insets(0,60,0,0));
        DashBoard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        DashBoard.addActionListener(e -> {
            frame.setContentPane(new Student_Dashboard(frame));
            frame.revalidate();
        });

        TButton highlights = new TButton("Highlights");
        highlights.setBounds(12,293,238,49);
        highlights.setFont(MainFrame.loadSanchez(15f));
        highlights.setForeground(new Color(93,93,93));
        highlights.setHorizontalAlignment(SwingConstants.LEFT);
        highlights.setMargin(new Insets(0,60,0,0));
        highlights.setCursor(new Cursor(Cursor.HAND_CURSOR));
        highlights.addActionListener(e -> {
            frame.setContentPane(new Highlights(frame));
            frame.revalidate();
        });

        TButton BrowseBooks = new TButton("Browse Books");
        BrowseBooks.setBounds(12,345,238,49);
        BrowseBooks.setFont(MainFrame.loadSanchez(15f));
        BrowseBooks.setForeground(new Color(93,93,93));
        BrowseBooks.setHorizontalAlignment(SwingConstants.LEFT);
        BrowseBooks.setMargin(new Insets(0,60,0,0));
        BrowseBooks.setCursor(new Cursor(Cursor.HAND_CURSOR));
        BrowseBooks.addActionListener(e -> {
            frame.setContentPane(new Book_browse(frame));
            frame.revalidate();
        });

        TButton BorrowedBooks = new TButton("My Borrowed Books");
        BorrowedBooks.setBounds(12,398,238,49);
        BorrowedBooks.setFont(MainFrame.loadSanchez(15f));
        BorrowedBooks.setForeground(new Color(93,93,93));
        BorrowedBooks.setHorizontalAlignment(SwingConstants.LEFT);
        BorrowedBooks.setMargin(new Insets(0,60,0,0));
        BorrowedBooks.setCursor(new Cursor(Cursor.HAND_CURSOR));

        TButton MyRequest = new TButton("My Requests");
        MyRequest.setBounds(12,451,238,49);
        MyRequest.setFont(MainFrame.loadSanchez(15f));
        MyRequest.setForeground(new Color(93,93,93));
        MyRequest.setHorizontalAlignment(SwingConstants.LEFT);
        MyRequest.setMargin(new Insets(0,60,0,0));
        MyRequest.setCursor(new Cursor(Cursor.HAND_CURSOR));
        MyRequest.addActionListener(e -> {
            frame.setContentPane(new Book_request(frame));
            frame.revalidate();
        });

        background.add(BrowseBooks);
        background.add(highlights);
        background.add(DashBoard);
        background.add(BorrowedBooks);
        background.add(MyRequest);

        this.add(background);
    }

    // ================= TABLE =================

    private void setupTable() {

        String[] columns = {
            "Title","Book ID","Borrowed Date","Due Date","Status","Action"
        };

        model = new DefaultTableModel(columns,0){
            public boolean isCellEditable(int row,int col){
                return col==5;
            }
        };

        table = new JTable(model);
        table.setBounds(371,510,1030,412);
        table.setRowHeight(28);
        table.setShowGrid(false);
        table.setTableHeader(null);
        table.setFillsViewportHeight(true);

        int[] widths = {350,120,150,150,100,100};
        for(int i=0;i<widths.length;i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            table.getColumnModel().getColumn(i).setMinWidth(widths[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(widths[i]);
        }

        table.setDefaultRenderer(Object.class,new BorrowRenderer());
        table.getColumnModel().getColumn(5)
             .setCellRenderer(new ActionRenderer());
        table.getColumnModel().getColumn(5)
             .setCellEditor(new ActionEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(371,510,1030,412);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.getVerticalScrollBar().setUI(new ModernScroll());

        this.add(scroll);
    }

    // ================= LOAD BORROWED =================

    private void loadBorrowedBooks(){

        BookService.getRef().getParent()
        .child("book_requests")
        .addValueEventListener(new ValueEventListener(){

            public void onDataChange(DataSnapshot snapshot){

                model.setRowCount(0);

                for(DataSnapshot data: snapshot.getChildren()){

                    String studentId = data.child("studentId").getValue(String.class);
                    String status = data.child("status").getValue(String.class);

                    if(!CURRENT_STUDENT_ID.equals(studentId)) continue;
                    if(!"Approved".equals(status)) continue;

                    String bookId = data.child("bookId").getValue(String.class);
                    Long borrow = data.child("borrowDate").getValue(Long.class);
                    Long due = data.child("dueDate").getValue(Long.class);

                    BookService.getRef().child(bookId)
                    .addListenerForSingleValueEvent(new ValueEventListener(){

                        public void onDataChange(DataSnapshot snap){
                            Books book = snap.getValue(Books.class);
                            if(book==null) return;

                            model.addRow(new Object[]{
                                book.getTitle(),
                                bookId,
                                new Date(borrow),
                                new Date(due),
                                status,
                                "•••"
                            });
                        }

                        public void onCancelled(DatabaseError error){}
                    });
                }
            }

            public void onCancelled(DatabaseError error){}
        });
    }

    // ================= RENDERERS =================

    class BorrowRenderer extends DefaultTableCellRenderer{
        public Component getTableCellRendererComponent(
            JTable table,Object value,boolean isSel,
            boolean hasFocus,int row,int col){

            super.getTableCellRendererComponent(
                table,value,false,false,row,col);

            setBackground(row%2==0?
                new Color(245,245,245):Color.WHITE);

            setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
            return this;
        }
    }

    class ActionRenderer extends JButton implements TableCellRenderer{
        public ActionRenderer(){
            setText("•••");
            setContentAreaFilled(false);
            setBorderPainted(false);
        }
        public Component getTableCellRendererComponent(
            JTable table,Object value,boolean isSel,
            boolean hasFocus,int row,int col){
            return this;
        }
    }

    class ActionEditor extends DefaultCellEditor{

        private JButton button;
        private int row;

        public ActionEditor(){
            super(new JTextField());
            button=new JButton("•••");
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);

            button.addActionListener(e->{
                fireEditingStopped();
                showDetails(row);
            });
        }

        public Component getTableCellEditorComponent(
            JTable table,Object value,boolean isSel,
            int row,int col){
            this.row=table.convertRowIndexToModel(row);
            return button;
        }

        public Object getCellEditorValue(){
            return "•••";
        }
    }

    private void showDetails(int row){

    String bookId = model.getValueAt(row,1).toString();

    BookService.getRef().child(bookId)
    .addListenerForSingleValueEvent(new ValueEventListener(){

        @Override
        public void onDataChange(DataSnapshot snap){

            Books book = snap.getValue(Books.class);
            if(book == null) return;

            JLayeredPane layer = frame.getLayeredPane();

            // 🔥 Use array holder (Java-safe trick)
            final StudentBorrowedDetailsPanel[] holder =
                    new StudentBorrowedDetailsPanel[1];

            holder[0] = new StudentBorrowedDetailsPanel(book, () -> {
                layer.remove(holder[0]);
                layer.repaint();
            });

            layer.add(holder[0], JLayeredPane.POPUP_LAYER);
            layer.repaint();
        }

        @Override
        public void onCancelled(DatabaseError error){}
    });
}


    static class ModernScroll extends BasicScrollBarUI{
        protected JButton createDecreaseButton(int o){return new JButton();}
        protected JButton createIncreaseButton(int o){return new JButton();}
    }
}
