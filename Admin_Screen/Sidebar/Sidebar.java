package Admin_Screen.Sidebar;

import java.awt.*;
import javax.swing.*;


public class Sidebar extends javax.swing.JPanel{
    
    private JButton activeMainButton;
    private JButton activeSubButton;

    private JPanel bookSubMenu;
    private JButton button_Book_Management;

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public Sidebar(CardLayout cardLayout, JPanel contPanel){
        this.cardLayout = cardLayout;
        this.contentPanel = contPanel;

        //transparent panel

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);
        setPreferredSize(new Dimension(230, 0));

        //main buttons
        JButton button_Dashboard = createMainButton("Dashboard");
        button_Book_Management = createMainButton("Book Management");
        JButton button_Students = createMainButton("Student Management");
        JButton button_Accounts = createMainButton("Account Management");

        //Books Submenu

        bookSubMenu = new JPanel();
        bookSubMenu.setLayout(new BoxLayout(bookSubMenu, BoxLayout.Y_AXIS));
        bookSubMenu.setOpaque(false);
        bookSubMenu.setVisible(false);

        JButton  button_Availabe = createSubButton("Available Books");
        JButton button_Borrowed = createSubButton("Borrowed Books");

        bookSubMenu.add(button_Availabe);
        bookSubMenu.add(button_Borrowed);

        //add components
        add(Box.createVerticalStrut(120)); // for spacing from top image
        add(button_Dashboard);
        add(button_Book_Management);
        add(bookSubMenu);
        add(button_Students);
        add(button_Accounts);


        // actions

        button_Dashboard.addActionListener(e -> {
            cardLayout.show(contentPanel, "DASHBOARD");
            setActiveMain(button_Dashboard);
            clearSubHighlight();
        });

        button_Book_Management.addActionListener(e -> {
            bookSubMenu.setVisible(!bookSubMenu.isVisible());
            setActiveMain(button_Book_Management);
            
            revalidate();
        });

        button_Availabe.addActionListener(e ->{
            cardLayout.show(contentPanel, "AVAILABLE");
            setActiveMain(button_Book_Management);
            setActiveSub(button_Availabe);

        });

        button_Borrowed.addActionListener(e -> {
            cardLayout.show(contentPanel, "BORROWED");
            setActiveMain(button_Book_Management);
            setActiveSub(button_Borrowed);
        });

        button_Students.addActionListener(e ->{
            cardLayout.show(contentPanel, "STUDENTS");
            setActiveMain(button_Students);
            clearSubHighlight();
        });

        button_Accounts.addActionListener(e -> {
            cardLayout.show(contentPanel, "ACCOUNTS");
            setActiveMain(button_Accounts);
            clearSubHighlight();
        });
    }

    // active state

    private void setActiveMain(JButton button){
        if(activeMainButton != null){
            activeMainButton.setForeground(Color.white);
        }
        activeMainButton = button;
        activeMainButton.setForeground(new Color(180, 220, 255));
    }

    private void setActiveSub(JButton button){
        if(activeSubButton != null){
            activeSubButton.setFont(activeSubButton.getFont().deriveFont(Font.PLAIN));
        }

        activeSubButton = button;
        activeSubButton.setFont(
            activeSubButton.getFont().deriveFont(Font.BOLD)
        );
    }

    private void clearSubHighlight(){
        if(activeSubButton != null){
            activeSubButton.setFont(activeSubButton.getFont().deriveFont(Font.PLAIN));
            activeSubButton = null;
        }
    }
   
    // button style
    private JButton createMainButton(String text){
        JButton button = new JButton(text);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setForeground(Color.white);
        button.setFont(new Font("Sanchez", Font.BOLD, 13));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        return button;
    }

    private JButton createSubButton(String text){
        JButton button = new JButton(" " + text);
        button.setAlignmentX(LEFT_ALIGNMENT);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorderPainted(false);
        button.setFocusable(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setForeground(new Color(220, 220, 220));
        button.setFont(new Font("Sanchez", Font.PLAIN, 12));
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return button;

    }

}
