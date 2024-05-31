package adminPage;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import loginSign.Login;

public class IntroforAdmin extends JFrame {
    private ImageIcon img = new ImageIcon("src/icon4.jpg");
    private JLabel label;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JTextField field;
    private JPasswordField passwordField;
    private JButton button;
    private JButton button1;

    public IntroforAdmin() {
        initComponents();
    }

    void initComponents() {
        JFrame gui = new JFrame();
        gui.setLayout(null);

        // Buttons
        button1 = new JButton();
        button1.setText("General Database");
        button1.setBounds(600, 500, 250, 150);
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        button1.setFont(new Font("Times New Roman", Font.BOLD, 24));
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
                new GeneralAdmin();
            }
        });

        JButton button3 = new JButton();
        button3.setText("Delete user");
        button3.setBounds(100, 500, 250, 150);
        button3.setForeground(new java.awt.Color(255, 255, 255));
        button3.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        button3.setFont(new Font("Times New Roman", Font.BOLD, 24));
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
                new AdminUserDeletion().setVisible(true);
            }
        });

        JButton button2 = new JButton();
        button2.setText("Logout");
        button2.setBounds(1300, 650, 150, 100);
        button2.setForeground(new java.awt.Color(255, 255, 255));
        button2.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        button2.setFont(new Font("Times New Roman", Font.BOLD, 24));
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
                new Login("English"); // Pass appropriate language parameter
            }
        });

        // Image Label
        JLabel imageLabel = new JLabel(img);
        imageLabel.setBounds(0, -300, 500, 1000);

        // Text Label
        JLabel text = new JLabel("<html><div style='text-align: center;'>Explore,<br>Envision,<br>and Evolve:<br>Discover the World Through Books.</div></html>");
        text.setBounds(600, 45, 800, 250);
        text.setForeground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        text.setFont(new Font("Times New Roman", Font.BOLD, 50));

        // Add components to the frame
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(1024, 768);
        gui.setIconImage(img.getImage());
        gui.setTitle("Book Stack");

        gui.add(button1);
        gui.add(imageLabel);
        gui.add(text);
        gui.add(button2);
        gui.add(button3);

        // Bring text label to front
        text.setOpaque(false);
        gui.getContentPane().setComponentZOrder(text, 0);
        gui.getContentPane().setComponentZOrder(imageLabel, 1);

        gui.setVisible(true);
    }

    public static void main(String[] args) {
        new IntroforAdmin();
    }
}
