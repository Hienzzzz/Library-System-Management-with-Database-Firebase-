package project.Student_Screen.Student_BookRequest;


import javax.swing.*;

import com.google.api.ResourceDescriptor.History;

import project.Main_System.MainFrame;
import project.Student_Screen.Student_bookBrowse.*;

import project.Student_Screen.Student_borrowedBook.My_borrowed_Books;
import project.Student_Screen.Student_dashboard.Student_Dashboard;
import project.Student_Screen.Student_highlights.Highlights;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;


public class Book_request extends javax.swing.JPanel {
    private MainFrame frame;

    static {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
    }

    public Book_request(MainFrame frame){
        this.frame = frame;
        panel();
    }
    public void panel(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));
        
         ImageIcon icon = new ImageIcon(
            getClass().getResource("/Images/Student_recentRequest.png")
        );
        JLabel background = new JLabel(icon);
        background.setBounds(0,0,1512,982);
        background.setLayout(null);

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
                    g2.fillRoundRect(
                        0, 0,
                        getWidth(),
                        getHeight(),
                        30, 30  
                    );
                }

                super.paintComponent(g);
            }
        }

            TButton DashBoard = new TButton("Student Dashboard");
            DashBoard.setBounds(12, 240, 238, 49);
            DashBoard.setFont(MainFrame.loadSanchez(15f));
            DashBoard.setForeground(new Color(93, 93, 93));
            DashBoard.setHorizontalAlignment(SwingConstants.LEFT);
            DashBoard.setMargin(new Insets(0, 60, 0, 0));
            DashBoard.setCursor(new Cursor(Cursor.HAND_CURSOR));

            DashBoard.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.out.println("Switching to dashborad screen");
                    frame.setContentPane(new Student_Dashboard(frame));
                    frame.revalidate();

                }
            });


            TButton highlights = new TButton("Highlights");
            highlights.setBounds(12, 293, 238, 49);
            highlights.setFont(MainFrame.loadSanchez(15f));
            highlights.setForeground(new Color(93, 93, 93));
            highlights.setHorizontalAlignment(SwingConstants.LEFT);
            highlights.setMargin(new Insets(0, 60, 0, 0));
            highlights.setCursor(new Cursor(Cursor.HAND_CURSOR));

            highlights.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.out.println("switching to Highlights screen");
                    frame.setContentPane(new Highlights(frame));
                    frame.revalidate();

                }
            });

            TButton BrowseBooks = new TButton("Browse Books");
            BrowseBooks.setBounds(12, 345, 238, 49);
            BrowseBooks.setFont(MainFrame.loadSanchez(15f));
            BrowseBooks.setForeground(new Color(93, 93, 93));
            BrowseBooks.setHorizontalAlignment(SwingConstants.LEFT);
            BrowseBooks.setMargin(new Insets(0, 60, 0, 0));
            BrowseBooks.setCursor(new Cursor(Cursor.HAND_CURSOR));

            BrowseBooks.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    frame.setContentPane(new Book_browse(frame));
                    frame.revalidate();

                }
            });

            TButton BorrowedBooks = new TButton("My Borrowed Books");
            BorrowedBooks.setBounds(12, 398, 238, 49);
            BorrowedBooks.setFont(MainFrame.loadSanchez(15f));
            BorrowedBooks.setForeground(new Color(93, 93, 93));
            BorrowedBooks.setHorizontalAlignment(SwingConstants.LEFT);
            BorrowedBooks.setMargin(new Insets(0, 60, 0, 0));
            BorrowedBooks.setCursor(new Cursor(Cursor.HAND_CURSOR));

            BorrowedBooks.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.out.println("switching to  My_borrowed_book screen");
                    frame.setContentPane(new My_borrowed_Books(frame));
                    frame.revalidate();

                }
            });

            TButton MyRequest = new TButton("My Requests");
            MyRequest.setBounds(12, 451, 238, 49);
            MyRequest.setFont(MainFrame.loadSanchez(15f));
            MyRequest.setForeground(new Color(93, 93, 93));
            MyRequest.setHorizontalAlignment(SwingConstants.LEFT);
            MyRequest.setMargin(new Insets(0, 60, 0, 0));
            MyRequest.setCursor(new Cursor(Cursor.HAND_CURSOR));

            MyRequest.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    System.out.println("Switching to My_request screen");
                    frame.setContentPane(new Book_request(frame));
                    frame.revalidate();

                }
            });

           


        
        
         

        background.add(BrowseBooks);
        background.add(highlights);
        background.add(DashBoard);
        background.add(BorrowedBooks);
        background.add(MyRequest);
        this.add(background);
    }
    
}
