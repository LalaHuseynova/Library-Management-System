package loginSign;
import javax.swing.*;

import lang.Messages;
import user.IntroforUser;
import lang.Language;
import adminPage.IntroforAdmin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Login {
    private ImageIcon img = new ImageIcon("src/icon4.jpg");
    private JLabel label;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JTextField field;
    private JPasswordField passwordField;
    private JButton button;
    private JButton button1;
    private String language; // Language variable

    public Login(String language) {

        this.language = language;
        initComponents();
    }

    
    public JLabel getLabel() {
        return label;
    }

    public JLabel getLabel1() {
        return label1;
    }


    public JLabel getLabel2() {
        return label2;
    }

    public JLabel getLabel3() {
        return label3;
    }

    public JTextField getField() {
        return field;
    }

   
    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JButton getButton() {
        return button;
    }

    public JButton getButton1() {
        return button1;
    }

    private void initComponents() {
        JFrame gui = new JFrame();
        gui.setLayout(null);

        label = new JLabel();
        
        Language lang = Language.valueOf(language.toUpperCase());
        label.setText(Messages.getMessage(Messages.Message.LOGIN, lang));
        label.setBounds(150, 10, 250, 250);
        label.setForeground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        label.setFont(new Font("Times New Roman", Font.BOLD, 48));

        label1 = new JLabel();
        label1.setText(Messages.getMessage(Messages.Message.USERNAME_LABEL, lang));
        label1.setBounds(150, 100, 250, 250);
        label1.setForeground(Color.BLACK);
        label1.setFont(new Font("Times New Roman", Font.BOLD, 27));

        label2 = new JLabel();
        label2.setText(Messages.getMessage(Messages.Message.PASSWORD_LABEL, lang));
        label2.setBounds(150, 200, 250, 250);
        label2.setForeground(Color.BLACK);
        label2.setFont(new Font("Times New Roman", Font.BOLD, 27));

        field = new JTextField();
        field.setText("");
        field.setBounds(350, 200, 500, 50);
        field.setFont(new Font("Times New Roman", Font.PLAIN, 24));

        passwordField = new JPasswordField();
        passwordField.setBounds(350, 300, 500, 50);
        passwordField.setFont(new Font("Times New Roman", Font.PLAIN, 24));

        button = new JButton();
        button.setText(Messages.getMessage(Messages.Message.GO_TO_LIBRARY_BUTTON, lang));
        button.setBounds(500, 450, 500, 50);
        button.setForeground(new java.awt.Color(255, 255, 255));
        button.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        button.setFont(new Font("Times New Roman", Font.BOLD, 24));



        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = field.getText();
                String password = new String(passwordField.getPassword());
        
                if (username.equals("admin") && password.equals("admin")) {
                    int option = JOptionPane.showConfirmDialog(gui, Messages.getMessage(Messages.Message.REDIRECTING_TO_ADMIN_PANEL, lang), Messages.getMessage(Messages.Message.SUCCESS, lang), JOptionPane.OK_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        gui.dispose();
                        new IntroforAdmin();
                    }
                } else if (checkCredentials(username, password)) {
                    int option = JOptionPane.showConfirmDialog(gui, Messages.getMessage(Messages.Message.REDIRECTING_TO_LIBRARY, lang), Messages.getMessage(Messages.Message.SUCCESS, lang), JOptionPane.OK_OPTION);

                    if (option == JOptionPane.OK_OPTION) {
                        gui.dispose();
                        new IntroforUser();
                    }
                } else {
                    int option = JOptionPane.showConfirmDialog(gui, Messages.getMessage(Messages.Message.INVALID_USERNAME_PASSWORD, lang), Messages.getMessage(Messages.Message.ERROR, lang), JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);

                    if (option == JOptionPane.YES_OPTION) {
                        gui.dispose();
                        new SignUp(language);
                    }
                }
            }
         
         
        });

        
       
        label3 = new JLabel();
        label3.setText(Messages.getMessage(Messages.Message.DONT_HAVE_AN_ACCOUNT, lang));
        label3.setBounds(500, 350, 450, 450);
        label3.setForeground(Color.BLACK);
        label3.setFont(new Font("Times New Roman", Font.PLAIN, 25));

        button1 = new JButton();
        button1.setText(Messages.getMessage(Messages.Message.SIGN_UP, lang));
        button1.setBounds(750, 550, 150, 50);
        button1.setForeground(new java.awt.Color(255, 255, 255));
        button1.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        button1.setFont(new Font("Times New Roman", Font.BOLD, 24));
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
                new SignUp(language);
            }
        });
    
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(1024, 768); 
        gui.setIconImage(img.getImage());
        gui.setTitle(Messages.getMessage(Messages.Message.BOOK_STACK, lang));

        gui.add(label);
        gui.add(label1);
        gui.add(label2);
        gui.add(field);
        gui.add(passwordField);
        gui.add(button);
        gui.add(label3);
        gui.add(button1);


        gui.setVisible(true);
        
    }

    

   
    private static String loggedInUsername;

    // Method to retrieve the username of the logged-in user
    public static String getLoggedInUsername() {
        return loggedInUsername;
    }

    public static boolean checkCredentials(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("files/user.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    loggedInUsername = username; // Set the logged-in username

                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
