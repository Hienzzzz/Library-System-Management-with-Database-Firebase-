package project.Admin_Screen.Bookmanagement;

import javax.swing.*;
import java.awt.*;

public class LoadingOverLay extends JPanel {

    public LoadingOverLay(String message) {

        
        setOpaque(true);
        setLayout(new GridBagLayout());

        JPanel box = new JPanel();
        box.setBackground(Color.WHITE);
        box.setPreferredSize(new Dimension(250, 100));
        box.setLayout(new BorderLayout());
        box.setBorder(BorderFactory.createLineBorder(new Color(200,200,200)));

        JProgressBar progress = new JProgressBar();
        progress.setIndeterminate(true);

        JLabel label = new JLabel(message, JLabel.CENTER);

        box.add(label, BorderLayout.CENTER);
        box.add(progress, BorderLayout.SOUTH);

        add(box);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
