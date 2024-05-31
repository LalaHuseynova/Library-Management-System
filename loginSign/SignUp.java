package loginSign;
import javax.swing.*;

import lang.Messages;
import lang.Language;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class SignUp extends JFrame {
    private JTextField field;
    private JPasswordField passwordField;
    private JButton button;
    private JButton button2;
    public String language; 

    public SignUp(String language) {
        this.language = language;
        initComponents();
    }

    public JTextField getField() {
        return field;
    }

    public void setField(JTextField field) {
        this.field = field;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public void setPasswordField(JPasswordField passwordField) {
        this.passwordField = passwordField;
    }

    public JButton getButton() {
        return button;
    }

    public void setButton(JButton button) {
        this.button = button;
    }

    public JButton getButton2() {
        return button2;
    }

    public void setButton2(JButton button2) {
        this.button2 = button2;
    }

    private void initComponents() {
        ImageIcon img = new ImageIcon("src/icon4.jpg");
        JFrame gui = new JFrame();
        gui.setLayout(null);

        JLabel label = new JLabel();
        JLabel label1 = new JLabel();
        JLabel label2 = new JLabel();

        

        setField(new JTextField());
        setPasswordField(new JPasswordField());
        setButton(new JButton());
        setButton2(new JButton());

        Language lang = Language.valueOf(language.toUpperCase());
        label.setText(Messages.getMessage(Messages.Message.SIGN_UP, lang));
        label.setBounds(150, 10, 250, 250);
        label.setForeground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        label.setFont(new Font("Times New Roman", Font.BOLD, 48));

        label1.setText(Messages.getMessage(Messages.Message.NEW_USERNAME_LABEL, lang));
        label1.setBounds(150, 100, 250, 250);
        label1.setForeground(Color.BLACK);
        label1.setFont(new Font("Times New Roman", Font.BOLD, 27));

        label2.setText(Messages.getMessage(Messages.Message.NEW_PASSWORD_LABEL, lang));
        label2.setBounds(150, 200, 250, 250);
        label2.setForeground(Color.BLACK);
        label2.setFont(new Font("Times New Roman", Font.BOLD, 27));

        getField().setText("");
        getField().setBounds(350, 200, 500, 50);
        getField().setFont(new Font("Times New Roman", Font.PLAIN, 24));

        getPasswordField().setText("");
        getPasswordField().setBounds(350, 300, 500, 50);
        getPasswordField().setFont(new Font("Times New Roman", Font.PLAIN, 24));

        getButton().setText(Messages.getMessage(Messages.Message.REGISTER_BUTTON, lang));
        getButton().setBounds(500, 500, 500, 50);
        getButton().setForeground(new java.awt.Color(255, 255, 255));
        getButton().setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        getButton().setFont(new Font("Times New Roman", Font.BOLD, 24));

        getButton2().setText(Messages.getMessage(Messages.Message.BACK_TO_LOGIN_BUTTON, lang));
        getButton2().setBounds(615, 600, 300, 50);
        getButton2().setForeground(new java.awt.Color(255, 255, 255));
        getButton2().setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        getButton2().setFont(new Font("Times New Roman", Font.BOLD, 24));

        getButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = getField().getText();
                String password = new String(getPasswordField().getPassword());

                if (checkUsername(username)) {
                    JOptionPane.showMessageDialog(gui, Messages.getMessage(Messages.Message.USERNAME_ALREADY_TAKEN, lang), Messages.getMessage(Messages.Message.ERROR, lang), JOptionPane.ERROR_MESSAGE);
                } else if (password.length() < 4) {
                    JOptionPane.showMessageDialog(gui, Messages.getMessage(Messages.Message.PASSWORD_LENGTH_ERROR, lang), Messages.getMessage(Messages.Message.ERROR, lang), JOptionPane.ERROR_MESSAGE);
                } else {
                    addUserToDatabase(username, password, language);
                    JOptionPane.showMessageDialog(gui, Messages.getMessage(Messages.Message.REGISTRATION_SUCCESS, lang), Messages.getMessage(Messages.Message.SUCCESS, lang), JOptionPane.INFORMATION_MESSAGE);
                    saveUser(username, password);
                    getField().setText("");
                    getPasswordField().setText("");
                }
            }
        });

        getButton2().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.dispose();
                // Pass the language to the Login constructor
                new Login(language);
            }
        });

        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setSize(1024, 768);
        gui.setIconImage(img.getImage());
        gui.setTitle(Messages.getMessage(Messages.Message.BOOK_STACK, lang));


        gui.add(label);
        gui.add(label1);
        gui.add(label2);
        gui.add(getField());
        gui.add(getPasswordField());
        gui.add(getButton());
        gui.add(getButton2());
        gui.setVisible(true);
    }

    // public static void main(String[] args) {
    //      new SignUp(language);
    // }


    private static boolean checkUsername(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader("files/user.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveUser(String username, String password) {
        String folderPath = "persons";
        String filePath = folderPath + "/" + username + ".csv";

        try {
            File folder = new File(folderPath);
            if (!folder.exists()) {
                folder.mkdir();
            }

            // i missed to translate, cus not sure if it is possible
            try (PrintWriter printWriter = new PrintWriter(new FileWriter(filePath))) {
                printWriter.println("Title,Author,Rating,Review,Status,TimeSpent,StartDate,EndDate,UserRating,UserReview");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } catch (SecurityException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private static void addUserToDatabase(String username, String password, String language) {
        Language lang = Language.valueOf(language.toUpperCase());
        if (checkUsername(username)) {
            JOptionPane.showMessageDialog(null, Messages.getMessage(Messages.Message.USERNAME_ALREADY_EXISTS, lang), Messages.getMessage(Messages.Message.ERROR, lang), JOptionPane.ERROR_MESSAGE);
            return; // Exit the method without adding the user to the file
        }
        try (FileWriter writer = new FileWriter("files/user.csv", true)) {
            // Add the new user to the file
            writer.write(username + "," + password + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
