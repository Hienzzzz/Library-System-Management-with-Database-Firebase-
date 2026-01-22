package Login_screen;
 
import Main_System.MainFrame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class Login extends javax.swing.JPanel{
 
    private MainFrame frame;
    public Login(MainFrame frame){
        this.frame = frame;
        panel();
 
        
    }
 
    public void panel(){
        this.setLayout(null);
        this.setPreferredSize(new Dimension(1512, 982));
 
        JLabel background = new JLabel();
        background.setBounds(0,0,1512,982);
        background.setIcon(new ImageIcon("Images/LoginScreen_Login.png"));
        background.setLayout(null);
 
        //username========================================================================
        String nameText_placaeHolder = "Enter your Email / Username";
 
        JTextField username = new JTextField(nameText_placaeHolder);
        username.setBounds(978, 420, 346, 51);;
        username.setBackground(new Color(255, 255, 255));
        username.setBorder(null);
        username.setFont(new Font("Sanchez", Font.PLAIN, 20));
        username.setForeground(Color.GRAY);
 
        username.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained (FocusEvent e){
                
                if (username.getText().equals(nameText_placaeHolder)) {
                    username.setText("");
                    username.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e){
                if(username.getText().isEmpty()){
                    username.setText(nameText_placaeHolder);
                    username.setForeground(Color.GRAY);
                }
            }
        });
 
        //password========================================================================
        
        String passText_Placeholder ="Enter your password";
 
        JPasswordField password = new JPasswordField(passText_Placeholder);
        password.setBounds(980, 512, 300, 51);
        password.setBorder(null);
        password.setFont(new Font("Sanchez", Font.PLAIN, 20));
        password.setForeground(Color.GRAY);
        password.setEchoChar((char) 0 );
 
        ImageIcon opeEye = new ImageIcon( //open eye icon for password filed
            new ImageIcon("Images\\eye (2).png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)
        );
 
        ImageIcon closedEye = new ImageIcon( // closed eye icon for password file
            new ImageIcon("Images\\hidden.png").getImage().getScaledInstance(30,30,Image.SCALE_SMOOTH)
        );
 
        JCheckBox showPassword = new JCheckBox();
        showPassword.setBounds(1285, 525, 35,30 );
        showPassword.setBorder(null);
        showPassword.setContentAreaFilled(false);
        showPassword.setOpaque(false);
        showPassword.setFocusPainted(false);
        showPassword.setIcon(closedEye);
        showPassword.setSelectedIcon(opeEye);
 
        showPassword.addActionListener(e ->{
            String text = new String(password.getPassword());
            if(!text.equals(passText_Placeholder)){
                if(showPassword.isSelected()){
                    password.setEchoChar((char) 0);
                }else{
                    password.setEchoChar('•');
                }
            }
        } );
 
        password.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e){
                String currentText = new String (password.getPassword());
 
                if(currentText.equals(passText_Placeholder)){
                    password.setText("");
                    password.setForeground(Color.BLACK);
                    if(!showPassword.isSelected()){
                        password.setEchoChar('•');
                    }
                }
            }
            @Override
            public void focusLost(FocusEvent e){
                if(password.getPassword().length == 0){
                    password.setText(passText_Placeholder);
                    password.setForeground(Color.GRAY);
                    password.setEchoChar((char) 0);
                }
            }
        });
 
        JButton clearButton = new JButton();
        clearButton.setBounds(963, 617, 175, 62);
        clearButton.setOpaque(false);
        clearButton.setContentAreaFilled(false);
        clearButton.setBorderPainted(false);
        clearButton.setFocusPainted(false);
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                username.setText(nameText_placaeHolder);
                password.setText(passText_Placeholder);
                password.setEchoChar((char) 0);
                username.setForeground(Color.GRAY);
                password.setForeground(Color.GRAY);
            }
            
        });
 
        JButton loginButton = new JButton();
        loginButton.setBounds(1161,619,176,57);
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setBorderPainted(false);
        loginButton.setFocusPainted(false);
 
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("switching to Dashboard");
 
                Color errorRed = new Color(220, 80, 80);
                Color normalColor = Color.BLACK;

                boolean username_empty = 
                username.getText().trim().isEmpty() ||
                username.getText().equals(nameText_placaeHolder);

                boolean password_empty =
                password.getPassword().length == 0 ||
                new String(password.getPassword()).equals(passText_Placeholder);

                if(username_empty && password_empty){
                    username.setForeground(errorRed);
                    password.setForeground(errorRed);

                    JOptionPane.showMessageDialog(
                        frame, 
                        "Please fill out all required fields",
                        "Required Fields",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if(username_empty){
                    username.setForeground(errorRed);
                    JOptionPane.showMessageDialog(
                        frame, 
                    "Please enter your username",
                    "Required Field",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }else{
                    username.setForeground(normalColor);
                }

                if(password_empty){
                    password.setForeground(errorRed);
                    JOptionPane.showMessageDialog(
                        frame, 
                    "Please enter your password",
                    "Required field",
                    JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        });
 
 
 
 
        //register button=================================================================
        JButton register = new JButton();
        register.setBounds(1196,711, 104, 19);
        register.setOpaque(false);
        register.setContentAreaFilled(false);
        register.setBorderPainted(false);
 
        register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                System.out.println("Switching to register page");
                frame.setContentPane(new Register(frame));
                frame.revalidate();
 
            }
        });

        background.add(showPassword);
        background.add(loginButton);
        background.add(clearButton);
        background.add(showPassword);
        background.add(password);
        background.add(register);
        background.add(username);
        this.add(background);
    
       
 
 
 
    }
}