package project.Admin_Screen.Bookmanagement;

import javax.swing.*;
import java.awt.*;

public class LoadingOverLay extends JPanel {

    private JProgressBar progress;

    public LoadingOverLay(String message) {

        setOpaque(false);
        setLayout(new GridBagLayout());

        JPanel box = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Color.WHITE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
            }
        };

        box.setOpaque(false);
        box.setPreferredSize(new Dimension(350, 160));
        box.setLayout(new BorderLayout(15,15));
        box.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setFont(new Font("SansSerif", Font.BOLD, 18));
        label.setForeground(new Color(40,40,40));

        progress = new JProgressBar(0, 100);
        progress.setIndeterminate(true);
        progress.setStringPainted(false);
        progress.setPreferredSize(new Dimension(250, 16));
        

        box.add(label, BorderLayout.CENTER);
        box.add(progress, BorderLayout.SOUTH);

        add(box);
    }


    public void setProgress(int value) {
        progress.setValue(value);
    }

    public void stop(){
        progress.setIndeterminate(false);
    }

}