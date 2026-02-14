package project.Admin_Screen.Studentmanagement;

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
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.Admin_Screen.Admin_accountManagement.Admin_AccountManagement;
import project.Admin_Screen.Bookmanagement.BookManagement;
import project.Admin_Screen.Dashboard.AdminDashboard;
import project.Admin_Screen.Report_screen.Reports;
import project.Main_System.MainFrame;

public class Offenses extends JPanel {

    private MainFrame frame;
    private JTable table;
    private DefaultTableModel model;

    public Offenses(MainFrame frame) {
        this.frame = frame;
        panel();
    }

    public void panel() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_offense.png")
        );

        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        // ================= BUTTONS =================

        TButton dashboard = createSideButton("Dashboard",12,240,238,49,
                () -> frame.setContentPane(new AdminDashboard(frame)));

        TButton reports = createSideButton("Reports",12,297,238,49,
                () -> frame.setContentPane(new Reports(frame)));

        TButton bookManagement = createSideButton("Book Management",12,350,238,49,
                () -> frame.setContentPane(new BookManagement(frame)));

        TButton studentM = createSideButton("Student Management",12,403,238,49,
                () -> frame.setContentPane(new StudentManagement(frame)));

        TButton accountM = createSideButton("Admin Management",12,592,238,49,
                () -> frame.setContentPane(new Admin_AccountManagement(frame)));

        TButton smAccount = createTabButton("Account",40,468,
                () -> frame.setContentPane(new StudentAccountPanel(frame)));

        TButton smActiveBorrower = createTabButton("Active Borrower",40,503,
                () -> frame.setContentPane(new Activeborrower(frame)));

        TButton offenseBtn = createTabButton("Offenses",40,540,
                () -> frame.setContentPane(new Offenses(frame)));

        background.add(dashboard);
        background.add(reports);
        background.add(bookManagement);
        background.add(studentM);
        background.add(accountM);
        background.add(smAccount);
        background.add(smActiveBorrower);
        background.add(offenseBtn);

        // ================= TABLE =================

        initTable(background);
        loadOffenses();

        add(background);
    }

    // ================= TABLE SETUP =================

    private void initTable(JLabel background){

        String[] columns = {
                "Name",
                "Student ID",
                "Email",
                "Offense",
                "Action"
        };

        model = new DefaultTableModel(columns,0){
            public boolean isCellEditable(int row,int col){
                return col == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0,0));
        table.setTableHeader(null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFillsViewportHeight(true);

        int[] widths = {300,150,250,120,120};

        for(int i=0;i<widths.length;i++){
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
            column.setMinWidth(widths[i]);
            column.setMaxWidth(widths[i]);
        }

        table.setDefaultRenderer(Object.class,new OffenseRenderer());

        table.getColumnModel().getColumn(4)
                .setCellRenderer(new ClearRenderer());
        table.getColumnModel().getColumn(4)
                .setCellEditor(new ClearEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(373,383,1025,530);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        background.add(scroll);
    }

    // ================= LOAD DATA =================

    private void loadOffenses(){

        DatabaseReference usersRef =
                FirebaseDatabase.getInstance()
                        .getReference("users");

        usersRef.addValueEventListener(new ValueEventListener(){

            public void onDataChange(DataSnapshot snapshot){

                model.setRowCount(0);

                for(DataSnapshot child : snapshot.getChildren()){

                    String role =
                            child.child("role").getValue(String.class);

                    if(role == null || !role.equals("STUDENT"))
                        continue;

                    Long offenseCount =
                            child.child("studentData")
                                    .child("offenseCount")
                                    .getValue(Long.class);

                    if(offenseCount == null || offenseCount <= 0)
                        continue;

                    String name =
                            child.child("fullName")
                                    .getValue(String.class);

                    String id =
                            child.child("id")
                                    .getValue(String.class);

                    String email =
                            child.child("email")
                                    .getValue(String.class);

                    model.addRow(new Object[]{
                            name,
                            id,
                            email,
                            offenseCount,
                            id
                    });
                }
            }

            public void onCancelled(DatabaseError error){}
        });
    }

    // ================= CLEAR OFFENSE =================

    private void clearOffense(String studentId){

        DatabaseReference ref =
                FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(studentId)
                        .child("studentData")
                        .child("offenseCount");

        ref.setValue(0L, (error, databaseReference) -> {});
    }

    // ================= RENDERERS =================

    private class OffenseRenderer
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

    private class ClearRenderer extends JButton
            implements TableCellRenderer{

        public ClearRenderer(){
            setText("Clear");
            setBackground(new Color(0,120,215));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setBorderPainted(false);
            setHorizontalAlignment(SwingConstants.CENTER);
        }

        public Component getTableCellRendererComponent(
                JTable table,Object value,
                boolean isSel,boolean hasFocus,
                int row,int col){
            return this;
        }
    }

    private class ClearEditor
            extends DefaultCellEditor{

        private JButton button;
        private String studentId;

        public ClearEditor(){
            super(new JTextField());

            button = new JButton("Clear");
            button.setBackground(new Color(0,120,215));
            button.setForeground(Color.WHITE);
            button.setFocusPainted(false);

            button.addActionListener(e -> {
                clearOffense(studentId);
                fireEditingStopped();
            });
        }

        public Component getTableCellEditorComponent(
                JTable table,Object value,
                boolean isSel,int row,int col){

            studentId =
                    model.getValueAt(row,4).toString();

            return button;
        }

        public Object getCellEditorValue(){
            return studentId;
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
        btn.setBounds(x,y,220,35);
        btn.setFont(MainFrame.loadSanchez(13f));
        btn.setForeground(new Color(93,93,93));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0,58,0,0));
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
