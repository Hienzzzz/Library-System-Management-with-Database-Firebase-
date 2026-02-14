package project.Student_Screen.Student_BookRequest;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import project.Firebase_backend.Book_backend.BookService;
import project.Firebase_backend.Book_backend.Books;
import project.Main_System.MainFrame;
import project.Student_Screen.Student_bookBrowse.Book_browse;
import project.Student_Screen.Student_borrowedBook.My_borrowed_Books;
import project.Student_Screen.Student_dashboard.Student_Dashboard;
import project.Student_Screen.Student_highlights.Highlights;

public class Book_request extends JPanel {

    private MainFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JLabel background;

    // ⚠ Replace with real login system later
    private static final String CURRENT_STUDENT_ID = "CURRENT_STUDENT_ID";

    static {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    public Book_request(MainFrame frame){
        this.frame = frame;
        initializeUI();
        setupTable();
        loadRequests();
    }

    // ================= UI INITIALIZATION =================

    private void initializeUI(){

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Student_recentRequest.png")
        );

        background = new JLabel(icon);
        background.setBounds(0,0,1512,982);
        background.setLayout(null);

        addSidebarButtons();

        add(background);
    }

    // ================= SIDEBAR =================

    private void addSidebarButtons(){

        JButton dashboard = createSidebarButton("Student Dashboard",240);
        dashboard.addActionListener(e -> {
            frame.setContentPane(new Student_Dashboard(frame));
            frame.revalidate();
        });

        JButton highlights = createSidebarButton("Highlights",293);
        highlights.addActionListener(e -> {
            frame.setContentPane(new Highlights(frame));
            frame.revalidate();
        });

        JButton browse = createSidebarButton("Browse Books",345);
        browse.addActionListener(e -> {
            frame.setContentPane(new Book_browse(frame));
            frame.revalidate();
        });

        JButton borrowed = createSidebarButton("My Borrowed Books",398);
        borrowed.addActionListener(e -> {
            frame.setContentPane(new My_borrowed_Books(frame));
            frame.revalidate();
        });

        JButton requests = createSidebarButton("My Requests",451);

        background.add(dashboard);
        background.add(highlights);
        background.add(browse);
        background.add(borrowed);
        background.add(requests);
    }

    private JButton createSidebarButton(String text,int y){
        JButton btn = new JButton(text);
        btn.setBounds(12,y,238,49);
        btn.setFont(MainFrame.loadSanchez(15f));
        btn.setForeground(new Color(93,93,93));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0,60,0,0));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ================= TABLE SETUP =================

    private void setupTable(){

        String[] columns = {
                "Title","Book ID","Request Date","Status"
        };

        model = new DefaultTableModel(columns,0){
            @Override
            public boolean isCellEditable(int row,int column){
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setTableHeader(null);
        table.setFillsViewportHeight(true);

        
        int[] widths = {460, 200, 230, 160};

        for(int i=0;i<widths.length;i++){
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            table.getColumnModel().getColumn(i).setMinWidth(widths[i]);
            table.getColumnModel().getColumn(i).setMaxWidth(widths[i]);
        }

        table.setDefaultRenderer(Object.class,new RequestRenderer());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(371,372,1060,550);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.getVerticalScrollBar().setUI(new ModernScroll());
        scroll.getVerticalScrollBar().setPreferredSize(
                new Dimension(8,Integer.MAX_VALUE));

        background.add(scroll);

        JLabel endRecord = new JLabel("End of Record");
        endRecord.setBounds(820,870,200,30);
        endRecord.setForeground(Color.GRAY);
        background.add(endRecord);


  

    }

    // ================= LOAD REQUESTS =================

    private void loadRequests(){

        BookService.getRef().getParent()
                .child("book_requests")
                .addValueEventListener(new ValueEventListener(){

                    @Override
                    public void onDataChange(DataSnapshot snapshot){

                        model.setRowCount(0);

                        for(DataSnapshot data : snapshot.getChildren()){

                            String studentId =
                                    data.child("studentId")
                                            .getValue(String.class);

                            if(!CURRENT_STUDENT_ID.equals(studentId))
                                continue;

                            String bookId =
                                    data.child("bookId")
                                            .getValue(String.class);

                            String status =
                                    data.child("status")
                                            .getValue(String.class);

                            Long time =
                                    data.child("timestamp")
                                            .getValue(Long.class);

                            if(bookId == null || time == null)
                                continue;

                            BookService.getRef()
                                    .child(bookId)
                                    .addListenerForSingleValueEvent(
                                            new ValueEventListener(){

                                @Override
                                public void onDataChange(DataSnapshot snap){

                                    Books book =
                                            snap.getValue(Books.class);

                                    if(book == null) return;

                                    String formattedDate =
                                            new SimpleDateFormat("MMM dd, yyyy")
                                                    .format(new Date(time));

                                    model.addRow(new Object[]{
                                            book.getTitle(),
                                            bookId,
                                            formattedDate,
                                            status
                                    });
                                }

                                @Override
                                public void onCancelled(DatabaseError error){}
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error){}
                });
    }

    // ================= RENDERER =================

    private static class RequestRenderer
            extends DefaultTableCellRenderer{

        private final Color EVEN = new Color(245,245,245);
        private final Color ODD  = Color.WHITE;

        @Override
        public Component getTableCellRendererComponent(
                JTable table,Object value,
                boolean isSelected,boolean hasFocus,
                int row,int column){

            super.getTableCellRendererComponent(
                    table,value,false,false,row,column);

            setBackground(row%2==0?EVEN:ODD);
            setBorder(BorderFactory.createEmptyBorder(0,10,0,10));

            if(column==3 && value!=null){

                String status =
                        value.toString().toLowerCase();

                switch(status){
                    case "pending":
                        setForeground(new Color(255,140,0));
                        break;
                    case "approved":
                        setForeground(new Color(0,128,0));
                        break;
                    case "rejected":
                        setForeground(Color.RED);
                        break;
                    default:
                        setForeground(Color.BLACK);
                }
            }else{
                setForeground(Color.BLACK);
            }

            return this;
        }
    }

    // ================= MODERN SCROLL =================

    private static class ModernScroll
            extends BasicScrollBarUI{

        @Override
        protected void configureScrollBarColors(){
            thumbColor = new Color(180,180,180);
            trackColor = new Color(245,245,245);
        }

        @Override
        protected JButton createDecreaseButton(int o){
            return new JButton();
        }

        @Override
        protected JButton createIncreaseButton(int o){
            return new JButton();
        }
    }
}
