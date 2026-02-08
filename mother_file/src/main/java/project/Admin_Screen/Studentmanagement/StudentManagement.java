package project.Admin_Screen.Studentmanagement;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import project.Main_System.MainFrame;

public class StudentManagement extends JPanel {
    private MainFrame frame;
    private JTextField searchField;
    private JTable table;
    private DefaultTableModel model;
    private JLayeredPane layeredPane;
    private JComboBox sortBox;
    private JPanel dimOverlay;
    private TableRowSorter<DefaultTableModel> sorter;

    public StudentManagement(MainFrame frame) {
        this.frame = frame;
        initUI();

    }

    private void initUI() {

        setLayout(null);
        setPreferredSize(new Dimension(1512, 982));


        //============================Layerd pane======================================
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, 1512, 982);

        //========================Background ========================================
        ImageIcon icon = new ImageIcon(
            getClass().getResource("/Images/Admin_Student management.png")
        );
        JLabel background = new JLabel(icon);
        background.setBounds(0, 0, 1512, 982);
        background.setLayout(null);


        dimOverlay = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setComposite(AlphaComposite.SrcOver.derive(0.15f));
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        dimOverlay.setBounds(0, 0, 1512, 982);
        dimOverlay.setOpaque(false);
        dimOverlay.setVisible(false);

        dimOverlay.addMouseListener(new java.awt.event.MouseAdapter() {});

        //=========================table================================================

        String[] columns ={"Name", "Student ID", "Email", "Penalty", "Borrowed", "Action" };
        model = new DefaultTableModel(columns, 0){
        public boolean isCVellEditable(int row, int column){
                return column == 4;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setBackground(Color.white);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        //===================search bar field ====================================
        String search_textHolder = "Search student...";

        searchField = new JTextField();
        searchField.setBounds(436, 415, 228, 27);
        searchField.setForeground(Color.GRAY);
        searchField.setFont(new Font("Poppins", Font.PLAIN, 15));
        searchField.setBackground(Color.WHITE);
        searchField.setBorder(null);
        background.add(searchField);
        

        searchField.setText(search_textHolder);
        searchField.setForeground(Color.GRAY);

        searchField.addFocusListener(new java.awt.event.FocusAdapter() {

            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                if (searchField.getText().equals(search_textHolder)) {
                    searchField.setText("");
                    searchField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setText(search_textHolder);
                    searchField.setForeground(Color.GRAY);
                }
            }
        });

         //======================== sort bar ==============================
        String[] sortOption = {
            "Default",
            "Newest",
            "Oldest",
            "A to Z",
            "Title",
            "Author",
            "Book ID"
        };

        sortBox = new JComboBox<>(sortOption);
        sortBox.setBounds(854, 416, 145, 24);
        sortBox.setFont(new Font("Sanchez", Font.PLAIN, 13));
        sortBox.setBackground(Color.WHITE);
        sortBox.setForeground(new Color(60, 60, 60));
        sortBox.setFocusable(false);
        sortBox.setOpaque(false);

        sortBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 0),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        sortBox.setSelectedItem(null); 

        sortBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (index == -1 && value == null) {
                    
                    setText("Sort by:");
                    setForeground(new Color(150, 150, 150));
                } else if (index == -1) {
                 
                    setText("Sort by: " + value.toString());
                    setForeground(new Color(60, 60, 60));
                } else {
                  
                    setText(value.toString());
                    setForeground(new Color(60, 60, 60));
                }

                return this;
            }
        });


       
      
        


        this.add(background);
    }

    public void showDimOverlay() {
        dimOverlay.setVisible(true);
    }

    public void hideDimOverlay() {
            dimOverlay.setVisible(false);
    }

    public JLayeredPane getLayeredPaneRef() {
            return layeredPane;
    }


}