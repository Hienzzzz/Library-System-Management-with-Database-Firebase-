package project.Admin_Screen.Studentmanagement;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.*;
import com.google.firebase.database.*;

import project.Admin_Screen.Admin_accountManagement.Admin_AccountManagement;
import project.Admin_Screen.Bookmanagement.BookManagement;
import project.Admin_Screen.Dashboard.AdminDashboard;
import project.Admin_Screen.Report_screen.Reports;
import project.Main_System.MainFrame;

public class Activeborrower extends JPanel {

    private MainFrame frame;
    private JTable table;
    private DefaultTableModel model;

    public Activeborrower(MainFrame frame) {
        this.frame = frame;
        panel();
    }

    public void panel() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_Active Borrower.png")
        );

        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        // ================= BUTTONS =================
        TButton dashboard = createButton("Dashboard",12,240,238,49,
                () -> frame.setContentPane(new AdminDashboard(frame)));

        TButton reports = createButton("Reports",12,297,238,49,
                () -> frame.setContentPane(new Reports(frame)));

        TButton bookManagement = createButton("Book Management",12,350,238,49,
                () -> frame.setContentPane(new BookManagement(frame)));

        TButton studentM = createButton("Student Management",12,403,238,49,
                () -> frame.setContentPane(new StudentManagement(frame)));

        TButton accountM = createButton("Admin Management",12,592,238,49,
                () -> frame.setContentPane(new Admin_AccountManagement(frame)));

        TButton smAccount = createButton("Account",40,468,220,35,
                () -> frame.setContentPane(new StudentAccountPanel(frame)));

        TButton smActiveBorrower = createButton("Active Borrower",40,503,220,35,
                () -> frame.setContentPane(new Activeborrower(frame)));

        TButton offense = createButton("Offenses",40,540,220,35,
                () -> frame.setContentPane(new Offenses(frame)));

        background.add(dashboard);
        background.add(reports);
        background.add(bookManagement);
        background.add(studentM);
        background.add(accountM);
        background.add(smAccount);
        background.add(smActiveBorrower);
        background.add(offense);

        // 🔥 IMPORTANT (YOU FORGOT THIS BEFORE)
        initTable(background);
        loadActiveBorrowers();

        add(background);
    }

    // ================= TABLE =================

    private void initTable(JLabel background){

        String[] columns = {
                "Student Name",
                "Student ID",
                "Book ID",
                "Borrowed Date",
                "Due Date",
                "Action"
        };

        model = new DefaultTableModel(columns,0){
            public boolean isCellEditable(int row,int col){
                return col == 5;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setTableHeader(null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        int[] widths = {250,150,120,180,180,120};

        for(int i=0;i<widths.length;i++){
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
            column.setMinWidth(widths[i]);
            column.setMaxWidth(widths[i]);
        }

        table.setDefaultRenderer(Object.class,new ActiveRenderer());

        table.getColumnModel().getColumn(5)
                .setCellRenderer(new ReturnRenderer());
        table.getColumnModel().getColumn(5)
                .setCellEditor(new ReturnEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(373,383,1025,530);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        background.add(scroll);
    }

    // ================= LOAD DATA =================

    private void loadActiveBorrowers(){

        DatabaseReference root =
                FirebaseDatabase.getInstance().getReference();

        root.child("borrowed_books")
            .addValueEventListener(new ValueEventListener(){

            public void onDataChange(DataSnapshot snapshot){

                model.setRowCount(0);

                for(DataSnapshot data : snapshot.getChildren()){

                    String status =
                            data.child("status").getValue(String.class);

                    if(!"Borrowed".equals(status)) continue;

                    String studentId =
                            data.child("studentId").getValue(String.class);

                    String bookId =
                            data.child("bookId").getValue(String.class);

                    Long borrowDate =
                            data.child("borrowDate").getValue(Long.class);

                    Long dueDate =
                            data.child("dueDate").getValue(Long.class);

                    String borrowId = data.getKey();

                    loadDetails(
                            borrowId,
                            studentId,
                            bookId,
                            borrowDate,
                            dueDate
                    );
                }
            }

            public void onCancelled(DatabaseError error){}
        });
    }

    private void loadDetails(
        String borrowId,
        String studentId,
        String bookId,
        Long borrowDate,
        Long dueDate){

        DatabaseReference root =
                FirebaseDatabase.getInstance().getReference();

        root.child("users")
            .child(studentId)
            .addListenerForSingleValueEvent(new ValueEventListener(){

            public void onDataChange(DataSnapshot userSnap){

                String name =
                        userSnap.child("fullName")
                                .getValue(String.class);

                String formattedBorrow = "-";
                String formattedDue = "-";

                if(borrowDate != null){
                    formattedBorrow =
                        new java.text.SimpleDateFormat("MMM dd, yyyy")
                        .format(new java.util.Date(borrowDate));
                }

                if(dueDate != null){
                    formattedDue =
                        new java.text.SimpleDateFormat("MMM dd, yyyy")
                        .format(new java.util.Date(dueDate));
                }

                model.addRow(new Object[]{
                        name,
                        studentId,
                        bookId,
                        formattedBorrow,
                        formattedDue,
                        borrowId
                });
            }

            public void onCancelled(DatabaseError e){}
        });
    }

    // ================= RETURN =================
private void returnBook(String borrowId){

    DatabaseReference root =
            FirebaseDatabase.getInstance().getReference();

    root.child("borrowed_books")
        .child(borrowId)
        .child("status")
        .setValue("Returned", new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError error, DatabaseReference ref) {
                if (error != null) {
                    System.out.println("Return failed: " + error.getMessage());
                } else {
                    System.out.println("Book returned successfully.");
                }
            }
        });
}


    // ================= RENDERERS =================

    private class ActiveRenderer
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

            setBorder(BorderFactory.createEmptyBorder(0,15,0,15));
            return this;
        }
    }

    private class ReturnRenderer extends JButton
            implements TableCellRenderer{

        public ReturnRenderer(){
            setText("Return");
            setBackground(new Color(220,0,0));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        public Component getTableCellRendererComponent(
                JTable table,Object value,
                boolean isSel,boolean hasFocus,
                int row,int col){
            return this;
        }
    }

    private class ReturnEditor
            extends DefaultCellEditor{

        private JButton button;
        private String borrowId;

        public ReturnEditor(){
            super(new JTextField());

            button = new JButton("Return");
            button.setBackground(new Color(220,0,0));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                returnBook(borrowId);
                fireEditingStopped();
            });
        }

        public Component getTableCellEditorComponent(
                JTable table,Object value,
                boolean isSel,int row,int col){

            borrowId =
                    model.getValueAt(row,5).toString();

            return button;
        }

        public Object getCellEditorValue(){
            return borrowId;
        }
    }

    // ================= CUSTOM BUTTON =================

    private TButton createButton(
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
