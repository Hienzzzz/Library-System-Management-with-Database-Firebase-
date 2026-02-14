package project.Admin_Screen.Bookmanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

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

public class Overdue extends JPanel {

    private MainFrame frame;
    private JTable table;
    private DefaultTableModel model;
    private JLabel endRecordLabel;
    private JTextField searchField;
    private JComboBox<String> sortBox;
    private TableRowSorter<DefaultTableModel> sorter;
    private RowFilter<DefaultTableModel, Object> searchFilter;
    private javax.swing.Timer fadeIn;
    private javax.swing.Timer fadeOut;



    public Overdue(MainFrame frame) {
        this.frame = frame;
        panel();
    }

    public void panel() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_Overdue.png")
        );

        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        // ================= SEARCH =================
        String placeholder = "Search Overdue...";
        searchField = new JTextField(placeholder);
        searchField.setBounds(440, 284, 222, 27);
        searchField.setFont(new Font("Poppins", Font.PLAIN, 15));
        searchField.setForeground(Color.GRAY);
        searchField.setBorder(null);
        background.add(searchField);

        searchField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals(placeholder)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(placeholder);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String text = searchField.getText().trim();
                if (text.isEmpty() || text.equalsIgnoreCase(placeholder)) {
                    searchFilter = null;
                } else {
                    searchFilter = RowFilter.regexFilter("(?i)" + text, 0, 1, 2);
                }
                applyFilters();
            }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });

        // ================= SORT =================
        sortBox = new JComboBox<>(new String[]{
                "Default",
                "Most Overdue",
                "Due Soonest",
                "A to Z (Student)",
                "A to Z (Book)"
        });

        sortBox.setBounds(685, 285, 160, 24);
        sortBox.setFont(new Font("Sanchez", Font.PLAIN, 13));
        sortBox.setBackground(Color.WHITE);
        sortBox.setFocusable(false);
        sortBox.setSelectedItem(null);
        background.add(sortBox);

        sortBox.addActionListener(e -> applySorting());

        // ================= SIDEBAR BUTTONS (UNCHANGED) =================
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

        background.add(createTabButton("Available Books",55,405,
                () -> frame.setContentPane(new AvailabaleBooks(frame))));
        background.add(createTabButton("Borrowed Books",55,442,
                () -> frame.setContentPane(new BorrowedBook(frame))));
        background.add(createTabButton("Pending Request",55,477,
                () -> frame.setContentPane(new Pendingreq(frame))));
        background.add(createTabButton("Overdue",55,514,
                () -> frame.setContentPane(new Overdue(frame))));

        initTable(background);
        loadOverdueBooks();

        add(background);
    }

    // ================= TABLE =================

    private void initTable(JLabel background){

        String[] columns = {
                "Student Name",
                "Book ID",
                "Book Title",
                "Due Date",
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

        int[] widths = {250,150,350,200,120};
        for(int i=0;i<widths.length;i++){
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
            column.setMinWidth(widths[i]);
            column.setMaxWidth(widths[i]);
        }

        table.setDefaultRenderer(Object.class,new OverdueRenderer());
        table.getColumnModel().getColumn(4)
                .setCellRenderer(new ReturnRenderer());
        table.getColumnModel().getColumn(4)
                .setCellEditor(new ReturnEditor());

        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(373,383,1025,530);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        background.add(scroll);

        endRecordLabel = new JLabel("End of Record");
        endRecordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        endRecordLabel.setForeground(new Color(150,150,150));
        endRecordLabel.setFont(new Font("Poppins", Font.PLAIN, 14));
        endRecordLabel.setBounds(886, 870, 200, 30);
        endRecordLabel.setVisible(false);
        background.add(endRecordLabel);
    }

    // ================= FILTER =================

    private void applyFilters() {
        if (searchFilter == null) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(searchFilter);
        }
    }

    // ================= SORT =================

    private void applySorting() {

        Object selected = sortBox.getSelectedItem();
        if (selected == null) {
            sorter.setSortKeys(null);
            sorter.sort();
            return;
        }

        String option = selected.toString();
        List<RowSorter.SortKey> keys = new ArrayList<>();

        switch (option) {
            case "Most Overdue":
                keys.add(new RowSorter.SortKey(3, SortOrder.ASCENDING));
                break;
            case "Due Soonest":
                keys.add(new RowSorter.SortKey(3, SortOrder.DESCENDING));
                break;
            case "A to Z (Student)":
                keys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                break;
            case "A to Z (Book)":
                keys.add(new RowSorter.SortKey(2, SortOrder.ASCENDING));
                break;
            default:
                sorter.setSortKeys(null);
                sorter.sort();
                return;
        }

        sorter.setSortKeys(keys);
        sorter.sort();
    }

    // ================= LOAD =================

    private void loadOverdueBooks(){

        DatabaseReference root =
                BookService.getRef().getParent();

        root.child("borrowed_books")
            .addValueEventListener(new ValueEventListener(){

            public void onDataChange(DataSnapshot snapshot){

                model.setRowCount(0);
                long now = System.currentTimeMillis();

                for(DataSnapshot data : snapshot.getChildren()){

                    Long dueDate = data.child("dueDate").getValue(Long.class);
                    String status = data.child("status").getValue(String.class);

                    if(dueDate == null) continue;
                    if(!"Borrowed".equals(status)) continue;
                    if(now <= dueDate) continue;

                    String studentId =
                            data.child("studentId").getValue(String.class);

                    String bookId =
                            data.child("bookId").getValue(String.class);

                    String borrowId = data.getKey();

                    loadDetails(borrowId,studentId,bookId,dueDate);
                }

                endRecordLabel.setVisible(model.getRowCount() > 0);
            }

            public void onCancelled(DatabaseError error){}
        });
    }

    private void loadDetails(
            String borrowId,
            String studentId,
            String bookId,
            Long dueDate){

        DatabaseReference root =
                BookService.getRef().getParent();

        root.child("users")
            .child(studentId)
            .addListenerForSingleValueEvent(new ValueEventListener(){

            public void onDataChange(DataSnapshot userSnap){

                String name =
                    userSnap.child("fullName").getValue(String.class);

                root.child("books")
                    .child(bookId)
                    .addListenerForSingleValueEvent(new ValueEventListener(){

                    public void onDataChange(DataSnapshot bookSnap){

                        Books book =
                                bookSnap.getValue(Books.class);

                        if(book==null) return;

                        String formatted =
                                new SimpleDateFormat("MMM dd, yyyy")
                                .format(new Date(dueDate));

                        model.addRow(new Object[]{
                                name,
                                bookId,
                                book.getTitle(),
                                formatted,
                                borrowId
                        });
                    }

                    public void onCancelled(DatabaseError e){}
                });
            }

            public void onCancelled(DatabaseError e){}
        });
    }

    private void returnBook(String borrowId){
        BookService.getRef().getParent()
            .child("borrowed_books")
            .child(borrowId)
            .child("status")
            .setValue("Returned", (error, ref) -> {});
    }

    // ================= RENDERERS =================

    private class OverdueRenderer extends DefaultTableCellRenderer {
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

    private class ReturnEditor extends DefaultCellEditor{

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

            borrowId = model.getValueAt(row,4).toString();
            return button;
        }

        public Object getCellEditorValue(){
            return borrowId;
        }
    }

    // ================= BUTTON HELPERS (UNCHANGED) =================

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
                if (alpha >= 1f)
                    ((Timer)e.getSource()).stop();
            });

            fadeOut = new Timer(16, e -> {
                alpha = Math.max(alpha - SPEED, 0f);
                repaint();
                if (alpha <= 0f)
                    ((Timer)e.getSource()).stop();
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
