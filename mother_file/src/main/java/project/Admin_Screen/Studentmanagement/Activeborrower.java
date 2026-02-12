package project.Admin_Screen.Studentmanagement;


import project.Admin_Screen.Admin_accountManagement.Admin_AccountManagement;
import project.Admin_Screen.Bookmanagement.BookManagement;
import project.Admin_Screen.Dashboard.AdminDashboard;
import project.Admin_Screen.Report_screen.Reports;
import project.Main_System.MainFrame;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class Activeborrower extends JPanel {

    private MainFrame frame;

    public Activeborrower(MainFrame frame) {
        this.frame = frame;
        panel();
    }

    public void panel() {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));

        ImageIcon icon = new ImageIcon(
            getClass().getResource("/Images/Admin_Active Borrower.png")
        );
        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);

        // ================= BUTTONS =================

        TButton dashboard = new TButton("Dashboard");
        dashboard.setBounds(12, 240, 238, 49);
        dashboard.setFont(MainFrame.loadSanchez(20f));
        dashboard.setForeground(new Color(93, 93, 93));
        dashboard.setHorizontalAlignment(SwingConstants.LEFT);
        dashboard.setMargin(new Insets(0, 60, 0, 0));
        dashboard.setCursor(new Cursor(Cursor.HAND_CURSOR));
        dashboard.addActionListener(e -> {
            frame.setContentPane(new AdminDashboard(frame));
            frame.revalidate();
        });

        TButton reports = new TButton("Reports");
        reports.setBounds(12, 398, 238, 49);
        reports.setFont(MainFrame.loadSanchez(20f));
        reports.setForeground(new Color(93, 93, 93));
        reports.setHorizontalAlignment(SwingConstants.LEFT);
        reports.setMargin(new Insets(0, 60, 0, 0));
        reports.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reports.addActionListener(e -> {
            frame.setContentPane(new Reports(frame));
            frame.revalidate();
        });

        TButton bookManagement = new TButton("Book Management");
        bookManagement.setBounds(12, 451, 238, 49);
        bookManagement.setFont(MainFrame.loadSanchez(20f));
        bookManagement.setForeground(new Color(93, 93, 93));
        bookManagement.setHorizontalAlignment(SwingConstants.LEFT);
        bookManagement.setMargin(new Insets(0, 60, 0, 0));
        bookManagement.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookManagement.addActionListener(e -> {
            frame.setContentPane(new BookManagement(frame));
            frame.revalidate();
        });

        TButton studentM = new TButton("Student Management");
        studentM.setBounds(12, 504, 238, 49);
        studentM.setFont(MainFrame.loadSanchez(20f));
        studentM.setForeground(new Color(93, 93, 93));
        studentM.setHorizontalAlignment(SwingConstants.LEFT);
        studentM.setMargin(new Insets(0, 60, 0, 0));
        studentM.setCursor(new Cursor(Cursor.HAND_CURSOR));
        studentM.addActionListener(e -> {
            frame.setContentPane(new StudentManagement(frame));
            frame.revalidate();
        });

        TButton accountM = new TButton("Admin Management");
        accountM.setBounds(12, 557, 238, 49);
        accountM.setFont(MainFrame.loadSanchez(20f));
        accountM.setForeground(new Color(93, 93, 93));
        accountM.setHorizontalAlignment(SwingConstants.LEFT);
        accountM.setMargin(new Insets(0, 60, 0, 0));
        accountM.setCursor(new Cursor(Cursor.HAND_CURSOR));
        accountM.addActionListener(e -> {
            frame.setContentPane(new Admin_AccountManagement(frame));
            frame.revalidate();
        });

        // ================= STUDENT MANAGEMENT SUB BUTTONS =================

        TButton smAccount = new TButton("Account");
        smAccount.setBounds(30, 553, 220, 40);
        smAccount.setFont(MainFrame.loadSanchez(16f));
        smAccount.setForeground(new Color(120, 120, 120));
        smAccount.setCursor(new Cursor(Cursor.HAND_CURSOR));
        smAccount.addActionListener(e -> {
            frame.setContentPane(new StudentAccountPanel(frame));
            frame.revalidate();
        });

        TButton smActiveBorrower = new TButton("Active Borrower");
        smActiveBorrower.setBounds(30, 593, 220, 40);
        smActiveBorrower.setFont(MainFrame.loadSanchez(16f));
        smActiveBorrower.setForeground(new Color(120, 120, 120));
        smActiveBorrower.setCursor(new Cursor(Cursor.HAND_CURSOR));
        smActiveBorrower.addActionListener(e -> {
            frame.setContentPane(new Activeborrower(frame));
            frame.revalidate();
        });

        background.add(dashboard);
        background.add(reports);
        background.add(bookManagement);
        background.add(studentM);
        background.add(accountM);

        background.add(smAccount);
        background.add(smActiveBorrower);

        this.add(background);
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
                if (alpha >= 1f) ((Timer) e.getSource()).stop();
            });

            fadeOut = new Timer(16, e -> {
                alpha = Math.max(alpha - SPEED, 0f);
                repaint();
                if (alpha <= 0f) ((Timer) e.getSource()).stop();
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    fadeOut.stop();
                    fadeIn.start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    fadeIn.stop();
                    fadeOut.start();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            if (alpha > 0f) {
                int a = (int) (alpha * 77);

                for (int i = 4; i >= 1; i--) {
                    g2.setColor(new Color(205, 205, 205, (int) (a * (i / 4f))));
                    g2.fillRoundRect(
                            -i, -i,
                            getWidth() + i * 2,
                            getHeight() + i * 2,
                            30, 30
                    );
                }

                g2.setColor(new Color(205, 205, 205, a));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
            }

            super.paintComponent(g);
        }
    }
}
