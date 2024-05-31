package user;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import loginSign.Login;

public class IntroforUser extends JFrame {
    private ImageIcon img = new ImageIcon("src/icon4.jpg");

    public IntroforUser() {
        initComponents();
    }

    void initComponents() {
        JFrame gui = new JFrame();
        gui.setLayout(null);

        // Buttons
        JButton button = new JButton();
        button.setText("Personal Database");
        button.setBounds(35, 500, 250, 150);
        button.setForeground(new java.awt.Color(255, 255, 255));
        button.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        button.setFont(new Font("Times New Roman", Font.BOLD, 24));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
                new PersonalDatabase();
            }
        });

        JButton button1 = new JButton();
        button1.setText("General Database");
        button1.setBounds(600, 500, 250, 150);
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        button1.setFont(new Font("Times New Roman", Font.BOLD, 24));
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
                new GeneralDatabase();
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
        gui.setSize(1200, 768); // Set the frame size
        gui.setIconImage(img.getImage());
        gui.setTitle("Book Stack");
        gui.add(button);
        gui.add(button1);
        gui.add(imageLabel);
        gui.add(text);
        gui.add(button2);

        // Bring text label to front
        text.setOpaque(false);
        gui.getContentPane().setComponentZOrder(text, 0);
        gui.getContentPane().setComponentZOrder(imageLabel, 1);

        gui.setVisible(true);
    }

    public static void main(String[] args) {
        new IntroforUser();
    }
}
