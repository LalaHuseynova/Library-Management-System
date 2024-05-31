package adminPage;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class BookAdder extends JFrame {
    private JTextField titleField;
    private JTextField authorField;
    private JLabel label, label1, label2;
    private JButton button;

    ImageIcon img = new ImageIcon("src/icon4.jpg");

    public BookAdder() {
        JFrame gui = new JFrame();
        gui.setLayout(null);

        label = new JLabel();
        label.setText("Adding book");
        label.setBounds(20, 5, 250, 100);
        label.setForeground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        label.setFont(new Font("Times New Roman", Font.BOLD, 32));

        label1 = new JLabel();
        label1.setText("Title:");
        label1.setBounds(20, 20, 250, 250);
        label1.setForeground(Color.BLACK);
        label1.setFont(new Font("Times New Roman", Font.BOLD, 27));

        titleField = new JTextField();
        titleField.setText("");
        titleField.setBounds(140, 120, 400, 50);
        titleField.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        label2 = new JLabel();
        label2.setText("Author:");
        label2.setBounds(20, 100, 250, 250);
        label2.setForeground(Color.BLACK);
        label2.setFont(new Font("Times New Roman", Font.BOLD, 27));

        authorField = new JTextField();
        authorField.setText("");
        authorField.setBounds(140, 200, 400, 50);
        authorField.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        button = new JButton();
        button.setText("Add book to the database");
        button.setBounds(100, 300, 450, 50);
        button.setForeground(new java.awt.Color(255, 255, 255));
        button.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        button.setFont(new Font("Times New Roman", Font.BOLD, 24));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBookToFile();
                gui.dispose();
                new GeneralAdmin();
            }
        });

        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setBounds(450, 100, 700, 500); // Set smaller size
        gui.setIconImage(img.getImage());
        gui.setTitle("Book Stack");

        gui.add(label);
        gui.add(label1);
        gui.add(authorField);
        gui.add(titleField);
        gui.add(label2);
        gui.add(button);

        gui.setVisible(true);
    }

    private void addBookToFile () {
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
    
        if (title.isEmpty() || author.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both title and author.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Ensure that the "personal.csv" file exists
        String filePath = "files/personal.csv";
        if (!Files.exists(Paths.get(filePath))) {
            try {
                Files.createFile(Paths.get(filePath));
                // Write headers to the CSV file if it's newly created
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.append("Title,Author,Review,Rating\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred while creating the file.", "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    
        // Check if the book already exists in the database
        try {
            ArrayList<String> lines = new ArrayList<>(Files.readAllLines(Paths.get(filePath)));
            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length >= 2 && parts[0].equals(title) && parts[1].equals(author)) {
                    JOptionPane.showMessageDialog(this, "This book already exists in the database.", "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while checking for duplicate books.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Write book information to CSV file with default values for review and rating
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.append(title).append(",").append(author).append(",No Review,No Rating\n");
            writer.flush();
            JOptionPane.showMessageDialog(this, "Book added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            titleField.setText("");
            authorField.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while adding book.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
  

    public static void main(String[] args) {
        new BookAdder();
    }
}
