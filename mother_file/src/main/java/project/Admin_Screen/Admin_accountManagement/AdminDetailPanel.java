package project.Admin_Screen.Admin_accountManagement;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import project.Firebase_backend.User_backend.User;
import project.Firebase_backend.User_backend.UserService;

public class AdminDetailPanel extends JPanel {

    private Admin_AccountManagement parent;
    private User user;
    private Runnable onClose;

    public AdminDetailPanel(Admin_AccountManagement parent,
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

        // ===== SPLIT NAME =====
        String[] nameParts = user.getFullName().split(" ", 2);
        String firstName = nameParts.length > 0 ? nameParts[0] : "";
        String lastName = nameParts.length > 1 ? nameParts[1] : "";

        JLabel lastNameLabel = new JLabel(lastName);
        lastNameLabel.setBounds(247, 85, 300, 55);
        lastNameLabel.setFont(new Font("Poppins", Font.PLAIN, 45));
        lastNameLabel.setForeground(Color.BLACK);

        JLabel firstNameLabel = new JLabel(firstName);
        firstNameLabel.setBounds(247, 124, 400, 50);
        firstNameLabel.setFont(new Font("Poppins", Font.PLAIN, 25));
        firstNameLabel.setForeground(Color.BLACK);

        JLabel emailLabel = new JLabel(user.getEmail());
        emailLabel.setBounds(247, 175, 400, 50);
        emailLabel.setFont(new Font("Poppins", Font.PLAIN, 20));
        emailLabel.setForeground(Color.BLACK);

        JLabel idLabel = new JLabel(user.getId());
        idLabel.setBounds(247, 225, 400, 50);
        idLabel.setFont(new Font("Poppins", Font.PLAIN, 20));
        idLabel.setForeground(Color.BLACK);

        JLabel roleLabel = new JLabel(user.getRole());
        roleLabel.setBounds(247, 275, 400, 40);
        roleLabel.setFont(new Font("Poppins", Font.PLAIN, 18));
        roleLabel.setForeground(new Color(80, 80, 80));

        JLabel statusLabel = new JLabel(user.getStatus());
        statusLabel.setBounds(658, 225, 200, 40);
        statusLabel.setFont(new Font("Poppins", Font.BOLD, 18));

        if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
            statusLabel.setForeground(new Color(0, 150, 0));
        } else {
            statusLabel.setForeground(Color.RED);
        }

        // ===== UPDATE BUTTON =====
        JButton updateBtn = new JButton();
        updateBtn.setBounds(397, 615, 190, 32);
        updateBtn.setContentAreaFilled(false);

        updateBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(
                        this,
                        "Update feature coming soon.",
                        "Update Account",
                        JOptionPane.INFORMATION_MESSAGE
                )
        );

        // ===== DELETE BUTTON =====
        JButton removeBtn = new JButton();
        removeBtn.setBounds(637, 616, 190, 32);
        removeBtn.setContentAreaFilled(false);

        removeBtn.addActionListener(e -> {

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove this account?\n\n"
                            + user.getFullName(),
                    "Confirm Remove",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            UserService.deleteUserCompletely(user.getId(), success -> {

                if (!success) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Failed to delete account.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Account permanently deleted.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );

                parent.reloadAdmins();

                if (onClose != null) {
                    onClose.run();
                }
            });
        });

        // ===== CLOSE BUTTON =====
        JButton closeBtn = new JButton();
        closeBtn.setBounds(814, 13, 25, 25);
        closeBtn.setContentAreaFilled(false);

        closeBtn.addActionListener(e -> {
            if (onClose != null) {
                onClose.run();
            }
        });

        background.add(lastNameLabel);
        background.add(firstNameLabel);
        background.add(emailLabel);
        background.add(idLabel);
        background.add(roleLabel);
        background.add(statusLabel);
        background.add(updateBtn);
        background.add(removeBtn);
        background.add(closeBtn);

        add(background);
    }
}
