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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
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

public class Pendingreq extends JPanel {

    private MainFrame frame;
    private JTable table;
    private DefaultTableModel model;

    public Pendingreq(MainFrame frame) {
        this.frame = frame;
        initUI();
        loadPendingRequests();
    }

    private void initUI() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_pendingRequest.png")
        );

        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        // ================= SIDEBAR BUTTONS =================

        TButton dashboard = createMainButton("Dashboard", 240);
        dashboard.addActionListener(e -> {
            frame.setContentPane(new AdminDashboard(frame));
            frame.revalidate();
        });

        TButton reports = createMainButton("Reports", 297);
        reports.addActionListener(e -> {
            frame.setContentPane(new Reports(frame));
            frame.revalidate();
        });

        TButton bookManagement = createMainButton("Book Management", 350);
        bookManagement.addActionListener(e -> {
            frame.setContentPane(new BookManagement(frame));
            frame.revalidate();
        });

        TButton studentM = createMainButton("Student Management", 564);
        studentM.addActionListener(e -> {
            frame.setContentPane(new StudentManagement(frame));
            frame.revalidate();
        });

        TButton accountM = createMainButton("Admin Management", 615);
        accountM.addActionListener(e -> {
            frame.setContentPane(new Admin_AccountManagement(frame));
            frame.revalidate();
        });

        // ================= BOOK MANAGEMENT TABS =================

        TButton availableBooks = createSubButton("Available Books", 405);
        availableBooks.addActionListener(e -> {
            frame.setContentPane(new AvailabaleBooks(frame));
            frame.revalidate();
        });

        TButton borrowedBooks = createSubButton("Borrowed Books", 442);
        borrowedBooks.addActionListener(e -> {
            frame.setContentPane(new BorrowedBook(frame));
            frame.revalidate();
        });

        TButton pendingRequest = createSubButton("Pending Request", 477);
        pendingRequest.addActionListener(e -> {
            frame.setContentPane(new Pendingreq(frame));
            frame.revalidate();
        });

        TButton overdue = createSubButton("Overdue", 514);
        overdue.addActionListener(e -> {
            frame.setContentPane(new Overdue(frame));
            frame.revalidate();
        });

        background.add(dashboard);
        background.add(reports);
        background.add(bookManagement);
        background.add(studentM);
        background.add(accountM);

        background.add(availableBooks);
        background.add(borrowedBooks);
        background.add(pendingRequest);
        background.add(overdue);

        // ================= TABLE =================
            String[] columns = {
                "Book Title",
                "Book ID",
                "Student ID",
                "Request Type",
                "Action"
            };


        model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return col == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(30);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setTableHeader(null);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        int[] widths = {400, 150, 200, 180, 120};


        for (int i = 0; i < widths.length; i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
            column.setMinWidth(widths[i]);
            column.setMaxWidth(widths[i]);
        }
        table.getColumnModel().getColumn(4).setPreferredWidth(100);


        table.setDefaultRenderer(Object.class, new PendingRenderer());
        table.getColumnModel().getColumn(4)
                .setCellRenderer(new ActionRenderer());
        table.getColumnModel().getColumn(4)
                .setCellEditor(new ActionEditor());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(371, 383, 1025, 490);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setHorizontalScrollBarPolicy(
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        scroll.getVerticalScrollBar().setUI(new ModernScroll());

        background.add(scroll);
        add(background);

      


// table.setTableHeader(null); // comment this temporarily

    }

    // ================= BUTTON HELPERS =================

    private TButton createMainButton(String text, int y) {
        TButton btn = new TButton(text);
        btn.setBounds(12, y, 238, 49);
        btn.setFont(MainFrame.loadSanchez(15f));
        btn.setForeground(new Color(93, 93, 93));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setMargin(new Insets(0, 60, 0, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private TButton createSubButton(String text, int y) {
        TButton btn = new TButton(text);
        btn.setBounds(55, y, 200, 35);
        btn.setFont(MainFrame.loadSanchez(13f));
        btn.setForeground(new Color(93, 93, 93));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ================= LOAD + APPROVE + REJECT =================

    private void loadPendingRequests() {

        DatabaseReference ref = BookService.getRef()
                .getParent()
                .child("book_requests");

        ref.addValueEventListener(new ValueEventListener() {

            public void onDataChange(DataSnapshot snapshot) {

                model.setRowCount(0);

                for (DataSnapshot data : snapshot.getChildren()) {

                    String status = data.child("status").getValue(String.class);
                    if (!"Pending".equals(status)) continue;

                    String studentId =
                            data.child("studentId").getValue(String.class);
                    String bookId =
                            data.child("bookId").getValue(String.class);
                    Long time =
                            data.child("timestamp").getValue(Long.class);

                    String requestId = data.getKey();

                    loadStudentAndBook(requestId, studentId, bookId, time);
                }
            }

            public void onCancelled(DatabaseError error) {}
        });
    }

    private void loadStudentAndBook(
            String requestId,
            String studentId,
            String bookId,
            Long time) {

        DatabaseReference root =
                BookService.getRef().getParent();

        root.child("users").child(studentId)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {

                            public void onDataChange(DataSnapshot studentSnap) {

                                String studentName =
                                        studentSnap.child("fullName")
                                                .getValue(String.class);

                                root.child("books").child(bookId)
                                        .addListenerForSingleValueEvent(
                                                new ValueEventListener() {

                                                    public void onDataChange(DataSnapshot bookSnap) {

                                                        Books book =
                                                                bookSnap.getValue(Books.class);

                                                        if (book == null) return;

                                                        String date = "N/A";

                                                        if (time != null) {
                                                            date = new SimpleDateFormat("MMM dd, yyyy")
                                                                    .format(new Date(time));
                                                        }

                                                       model.addRow(new Object[]{
                                                                book.getTitle(),     // 0 Book Title
                                                                bookId,              // 1 Book ID
                                                                studentId,           // 2 Student ID
                                                                "Borrow",            // 3 Request Type (or get from Firebase)
                                                                requestId            // 4 Action (hidden ID)
                                                        });

                                                    }

                                                    public void onCancelled(DatabaseError e) {}
                                                });
                            }

                            public void onCancelled(DatabaseError e) {}
                        });
    }

    private void approveRequest(String requestId) {

        DatabaseReference root =
                BookService.getRef().getParent();

        root.child("book_requests")
                .child(requestId)
                .child("status")
                .setValue("Approved", null);
    }

    private void rejectRequest(String requestId) {

        DatabaseReference root =
                BookService.getRef().getParent();

        root.child("book_requests")
                .child(requestId)
                .child("status")
                .setValue("Rejected", null);
    }

    // ================= TABLE STYLING =================

    private class PendingRenderer extends DefaultTableCellRenderer {

            private final Color EVEN = new Color(245, 245, 245);
            private final Color ODD  = Color.WHITE;

            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value,
                    boolean isSelected, boolean hasFocus,
                    int row, int col) {

                super.getTableCellRendererComponent(
                        table, value, false, false, row, col);

                // Alternating row color
                setBackground(row % 2 == 0 ? EVEN : ODD);

                // Reset text color
                setForeground(Color.BLACK);

                // Padding
                setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

                // Align columns properly
                if (col == 4) { // Action column
                    setHorizontalAlignment(SwingConstants.CENTER);
                } else {
                    setHorizontalAlignment(SwingConstants.LEFT);
                }

                return this;
            }
        }


    private class ActionRenderer extends JButton
        implements TableCellRenderer {

    public ActionRenderer() {
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(true);
        setFont(new Font("Poppins", Font.BOLD, 12));
        setForeground(Color.WHITE);
    }

    @Override
    public Component getTableCellRendererComponent(
            JTable table, Object value,
            boolean isSel, boolean hasFocus,
            int row, int col) {

        setText("Pending");


        // Center text
        setHorizontalAlignment(SwingConstants.CENTER);
        setMargin(new Insets(5, 10, 5, 10));


        // Match row background
        if (row % 2 == 0) {
            setBackground(new Color(255, 180, 0));
        } else {
            setBackground(new Color(255, 170, 0));
        }

        return this;
    }
    
}


    private class ActionEditor extends DefaultCellEditor {

        private JButton button;
        private String requestId;

        public ActionEditor() {
            super(new JTextField());

            button = new JButton("Pending");
            button.setBackground(new Color(255, 180, 0));
            button.setForeground(Color.WHITE);
            button.setHorizontalAlignment(SwingConstants.CENTER);


            button.addActionListener(e -> {

                int option = JOptionPane.showOptionDialog(
                        Pendingreq.this,
                        "Choose action for this request",
                        "Request Action",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        new String[]{"Approve", "Reject", "Cancel"},
                        "Approve"
                );

                if (option == 0) approveRequest(requestId);
                else if (option == 1) rejectRequest(requestId);

                fireEditingStopped();
            });
        }

        public Component getTableCellEditorComponent(
                JTable table, Object value,
                boolean isSel, int row, int col) {

            requestId = model.getValueAt(row, 4).toString();
            return button;
        }

        public Object getCellEditorValue() {
            return requestId;
        }
    }

    private static class ModernScroll extends BasicScrollBarUI {
        protected JButton createDecreaseButton(int o){ return new JButton(); }
        protected JButton createIncreaseButton(int o){ return new JButton(); }
    }

    // ================= TButton =================

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
                if (alpha >= 1f) ((Timer) e.getSource()).stop();
            });

            fadeOut = new Timer(16, e -> {
                alpha = Math.max(alpha - SPEED, 0f);
                repaint();
                if (alpha <= 0f) ((Timer) e.getSource()).stop();
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
                g2.setColor(new Color(205, 205, 205, a));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }

            super.paintComponent(g);
        }
    }
}
