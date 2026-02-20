package project.Login_screen;

import project.Admin_Screen.Bookmanagement.LoadingOverLay;

import javax.swing.*;
import java.awt.*;

public class TestDrive extends JPanel {

    private LoadingOverLay loading;
    private JPanel mainContent;
    private JLayeredPane layeredPane;

    public TestDrive() {

        setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        add(layeredPane, BorderLayout.CENTER);

        // Main content
        mainContent = new JPanel();
        mainContent.setBackground(Color.LIGHT_GRAY);
        mainContent.setLayout(new GridBagLayout());

        JButton startButton = new JButton("Start Loading Task");
        mainContent.add(startButton);

        // Loading overlay
        loading = new LoadingOverLay("Processing, please wait...");
        loading.setVisible(false);

        layeredPane.add(mainContent, Integer.valueOf(0));
        layeredPane.add(loading, Integer.valueOf(1));

        // Resize listener to make components fill the layeredPane
        layeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                Dimension size = layeredPane.getSize();
                mainContent.setBounds(0, 0, size.width, size.height);
                loading.setBounds(0, 0, size.width, size.height);
            }
        });

        startButton.addActionListener(e -> startLongTask());
    }

    private void startLongTask() {

    loading.setVisible(true);

    SwingWorker<Void, Integer> worker = new SwingWorker<>() {

        @Override
        protected Void doInBackground() throws Exception {

            for (int i = 0; i <= 100; i += 5) {
                Thread.sleep(100);  
                publish(i);
            }

            return null;
        }

        @Override
        protected void process(java.util.List<Integer> chunks) {
            int value = chunks.get(chunks.size() - 1);
            loading.setProgress(value);
        }

        @Override
        protected void done() {
            loading.setVisible(false);
            JOptionPane.showMessageDialog(TestDrive.this, "Task Completed!");
        }
    };

    worker.execute();
}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Real Loading Test");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setContentPane(new TestDrive());
            frame.setVisible(true);
        });
    }
}