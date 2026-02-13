package project.Admin_Screen.Studentmanagement;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import project.Firebase_backend.User_backend.User;

public class StudentDetailsPanel extends JPanel {

    private StudentAccountPanel parent;
    private User user; 
    private Runnable onClose;

    public StudentDetailsPanel(StudentAccountPanel parent,
                               User user,
                               Runnable onClose) {

        this.parent = parent;
        this.user = user;
        this.onClose = onClose;

        setLayout(null);
        setOpaque(false);
        setBounds(0, 0, 862, 678);

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/Admin_StudentDetails.png")
        );

        Image scaledImage = icon.getImage().getScaledInstance(
                862, 678, Image.SCALE_SMOOTH
        );

        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setBounds(0, 0, 862, 678);
        background.setLayout(null);

        JButton udpateBtn = new JButton();
        udpateBtn.setBounds(397, 615, 190, 32);
        udpateBtn.setBorder(null);
        udpateBtn.setContentAreaFilled(false);
        udpateBtn.setFocusPainted(false);
        udpateBtn.setOpaque(false);

        udpateBtn.addActionListener(e -> {
        JOptionPane.showMessageDialog(
                this,
                "Update feature coming soon.",
                "Update Student",
                javax.swing.JOptionPane.INFORMATION_MESSAGE
        );
    });


        JButton removeBtn = new JButton();
        removeBtn.setBounds(637, 616, 190, 32);
        removeBtn.setBorder(null);
        removeBtn.setContentAreaFilled(false);
        removeBtn.setFocusPainted(false);
        removeBtn.setOpaque(false);

        removeBtn.addActionListener(e -> {

            int confirm = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove this student account?\n\n"
                            + user.getFullName(),
                    "Confirm Remove",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );

            if (confirm != javax.swing.JOptionPane.YES_OPTION) return;

            com.google.firebase.database.FirebaseDatabase
                    .getInstance()
                    .getReference("users")
                    .child(user.getId())
                    .removeValue((error, ref) -> {

                        if (error != null) {
                            javax.swing.JOptionPane.showMessageDialog(
                                    this,
                                    "Failed to remove account:\n" + error.getMessage(),
                                    "Error",
                                    javax.swing.JOptionPane.ERROR_MESSAGE
                            );
                        } else {

                            javax.swing.JOptionPane.showMessageDialog(
                                    this,
                                    "Student account removed successfully.",
                                    "Success",
                                    javax.swing.JOptionPane.INFORMATION_MESSAGE
                            );

                            if (onClose != null) {
                                onClose.run(); // close panel after delete
                            }
                        }
                    });
        });


        JButton closeBtn = new JButton();
        closeBtn.setBounds(814, 13, 25, 25);
        closeBtn.setBorder(null);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setOpaque(false);

        closeBtn.addActionListener(e -> {
            if (onClose != null) {
                onClose.run();   // closes overlay properly
            }
        });

        String[] nameParts = user.getFullName().split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";


        //last name
        JLabel LastName = new JLabel(lastName);
        LastName.setBounds(247, 85, 300, 55 );
        LastName.setFont(new Font("Poppins", Font.PLAIN, 45));
        LastName.setForeground(Color.BLACK);

        //first name
        JLabel FirstName = new JLabel(firstName);
        FirstName.setBounds(247, 124, 400, 50 );
        FirstName.setFont(new Font("Poppins", Font.PLAIN, 25));
        FirstName.setForeground(Color.BLACK);

        //Email
        JLabel Email = new JLabel(user.getEmail());
        Email.setBounds(247, 175, 400, 50 );
        Email.setFont(new Font("Poppins", Font.PLAIN, 20));
        Email.setForeground(Color.BLACK);


        //ID
        JLabel IDno = new JLabel(user.getId());
        IDno.setBounds(247, 225, 400, 50 );
        IDno.setFont(new Font("Poppins", Font.PLAIN, 20));
        IDno.setForeground(Color.BLACK);

        
         //Status
        JLabel status = new JLabel(user.getStatus());
        status.setBounds(658, 225, 400, 50 );
        status.setFont(new Font("Poppins", Font.PLAIN, 18));
        status.setForeground(Color.green);

        if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
            status.setForeground(new Color(0,150,0));
        } else if ("RESTRICTED".equalsIgnoreCase(user.getStatus())) {
            status.setForeground(new Color(255,140,0));
        } else {
            status.setForeground(Color.RED);
        }


         //penalty Fees
        JLabel fees = new JLabel("20 Php");
        fees.setBounds(150, 306, 400, 50 );
        fees.setFont(new Font("Poppins", Font.PLAIN, 18));
        fees.setForeground(Color.red);

         //Borrowed books
        JLabel books = new JLabel("5");
        books.setBounds(193, 353, 400, 50 );
        books.setFont(new Font("Poppins", Font.PLAIN, 18));
        books.setForeground(Color.WHITE);

      // ================= RECENT ACTIVITY TABLE =================

        String[] columns = {"Activity","Book","Date"};
        DefaultTableModel historyModel =
                new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };


        JTable history = new javax.swing.JTable(historyModel);
        history.setRowHeight(28);
        history.setShowGrid(false);
        history.setIntercellSpacing(new Dimension(0, 0));
        history.setFont(new Font("Poppins", Font.PLAIN, 13));
        history.setForeground(new Color(0x333333));
        history.setBackground(new Color(0xF1F3F6));
        history.setFocusable(false);

        // Remove header
        history.setTableHeader(null);

        // Column widths
        history.getColumnModel().getColumn(0).setPreferredWidth(100);  // Type
        history.getColumnModel().getColumn(1).setPreferredWidth(450);  // Book
        history.getColumnModel().getColumn(2).setPreferredWidth(120);  // Date

        // Apply badge renderer ONLY to TYPE column
        history.getColumnModel()
                .getColumn(0)
                .setCellRenderer(new BadgeRenderer());

        // Apply clean alternating renderer for other columns
        history.setDefaultRenderer(Object.class,
                new javax.swing.table.DefaultTableCellRenderer() {

            private final Color EVEN = new Color(0xF1F3F6);
            private final Color ODD = Color.WHITE;

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                super.getTableCellRendererComponent(
                        table, value, false, false, row, column);

                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                setVerticalAlignment(SwingConstants.CENTER);
                setBackground(row % 2 == 0 ? EVEN : ODD);
                setForeground(new Color(0x333333));
                setFont(new Font("Poppins", Font.PLAIN, 13));
                setHorizontalAlignment(SwingConstants.LEFT);

                return this;
            }
        });


        


        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(50, 420, 735, 150);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(new Color(0xF1F3F6));
        scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, 0));
        scrollPane.setCorner(JScrollPane.LOWER_RIGHT_CORNER, new JPanel());

        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportView(history);
        scrollPane.setViewportBorder(null);
        history.setBorder(null);





        Color SCROLL_TRACK = new Color(0xF1F3F6);
        Color SCROLL_THUMB = new Color(0xB0B8C4); 
        Color SCROLL_THUMB_HOVER = new Color(0x8FA1B5);

        //scrollPane.setBorder(null);

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

            // ================= LOAD HISTORY =================

                DatabaseReference activityRef =
                        FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(user.getId())
                                .child("studentData")
                                .child("activityHistory");

                activityRef.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        historyModel.setRowCount(0);

                        if (!snapshot.exists()) {
                            historyModel.addRow(new Object[]{
                                    "No Activity", "-", "-"
                            });
                            return;
                        }

                        for (DataSnapshot actSnap : snapshot.getChildren()) {

                            String type = actSnap.child("type").getValue(String.class);
                            String title = actSnap.child("bookTitle").getValue(String.class);
                            String date = actSnap.child("date").getValue(String.class);

                            historyModel.addRow(new Object[]{
                                    type != null ? type : "-",
                                    title != null ? title : "-",
                                    date != null ? date : "-"
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        System.err.println("Failed loading activity: " + error.getMessage());
                    }
                });


            //=============== connect to database==============================

            DatabaseReference studentRef =
                    FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(user.getId())
                            .child("studentData");

            studentRef.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot snapshot) {

                    if (!snapshot.exists()) return;

                    Long borrowed = snapshot.child("borrowedCount").getValue(Long.class);
                    Long penalty = snapshot.child("penaltyAmount").getValue(Long.class);

                    books.setText(String.valueOf(borrowed != null ? borrowed : 0));
                    fees.setText((penalty != null ? penalty : 0) + " Php");
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    System.err.println("Failed loading student data: " + error.getMessage());
                }
            });
            



        background.add(scrollPane);
        background.add(books);
        background.add(fees);
        background.add(status);
        background.add(IDno);
        background.add(Email);
        background.add(FirstName);
        background.add(LastName);
        background.add(closeBtn);
        background.add(udpateBtn);
        background.add(removeBtn);
        this.add(background);
    }

    // ================= BADGE RENDERER =================
        class BadgeRenderer extends javax.swing.table.DefaultTableCellRenderer {

            @Override
            public Component getTableCellRendererComponent(
                    JTable table,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {

                super.getTableCellRendererComponent(
                        table, value, false, false, row, column);

                String type = value != null ? value.toString().toUpperCase() : "";

                setHorizontalAlignment(SwingConstants.CENTER);
                setForeground(Color.WHITE);
                setFont(new Font("Poppins", Font.BOLD, 12));

                if (type.equals("BORROW")) {
                    setBackground(new Color(46, 125, 50));
                }
                else if (type.equals("RETURN")) {
                    setBackground(new Color(25, 118, 210));
                }
                else if (type.equals("OVERDUE")) {
                    setBackground(new Color(198, 40, 40));
                }
                else {
                    setBackground(new Color(120,120,120));
                }

                setText("  " + type + "  ");

                return this;
            }
        }

    
}



