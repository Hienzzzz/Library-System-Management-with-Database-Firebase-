package project.Admin_Screen.Studentmanagement;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

//import project.Firebase_backend.Studnet_backend.Student;

public class StudentDetailsPanel extends JPanel{

    private StudentAccountPanel parent;
    private Student student;
    private  Runnable onClose;

    public StudentDetailsPanel(StudentAccountPanel parent, Student student, Runnable onClose){
        this.parent = parent;
        this.student = student;
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



      
        this.add(background);

    }
}