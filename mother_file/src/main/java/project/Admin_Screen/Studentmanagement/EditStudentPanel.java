package project.Admin_Screen.Studentmanagement;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.Firebase_backend.User_backend.User;

public class EditStudentPanel extends JPanel {

    private StudentAccountPanel parent;
    private User user; 
    private Runnable onClose;

    public EditStudentPanel(StudentAccountPanel parent,
                               User user,
                               Runnable onClose) {

        this.parent = parent;
        this.user = user;
        this.onClose = onClose;

        setLayout(null);
        setOpaque(false);
        setBounds(0, 0, 862, 678);

        ImageIcon icon = new ImageIcon(
                getClass().getResource("/Images/ADmin_editBook.png")
        );

        Image scaledImage = icon.getImage().getScaledInstance(
                862, 678, Image.SCALE_SMOOTH
        );

        JLabel background = new JLabel(new ImageIcon(scaledImage));
        background.setBounds(0, 0, 862, 678);
        background.setLayout(null);

        JButton udpateBtn = new JButton();
        udpateBtn.setBounds(637, 615, 190, 32);
        udpateBtn.setBorder(null);
        udpateBtn.setContentAreaFilled(false);
        udpateBtn.setFocusPainted(false);
        udpateBtn.setOpaque(false);

        JButton removeBtn = new JButton();
        removeBtn.setBounds(397, 616, 190, 32);
        removeBtn.setBorder(null);
        removeBtn.setContentAreaFilled(false);
        removeBtn.setFocusPainted(false);
        removeBtn.setOpaque(false);

        JButton closeBtn = new JButton();
        closeBtn.setBounds(814, 13, 25, 25);
        closeBtn.setBorder(null);
        closeBtn.setContentAreaFilled(false);
        closeBtn.setFocusPainted(false);
        closeBtn.setOpaque(false);


        //first name
        
        background.add(closeBtn);
        background.add(udpateBtn);
        background.add(removeBtn);
        this.add(background);
    }
}
