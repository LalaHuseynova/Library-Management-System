package loginSign;
import java.awt.Color;
import java.awt.Font;

import javax.swing.*;

public class LanguageGUI {
    public void selectLanguageAndOpenLogin() {
        // Create a pop-up window for language selection
        JFrame frame = new JFrame("Language Selection");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("src/icon4.jpg");

        // Create a panel to hold components
        JPanel panel = new JPanel();

        // Create radio buttons for language selection
        JRadioButton englishRadioButton = new JRadioButton("English");
        JRadioButton azerbaijaniRadioButton = new JRadioButton("Azerbaijani");
        JRadioButton russianRadioButton = new JRadioButton("Russian");

        // Create a button group to ensure only one radio button is selected at a time
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(englishRadioButton);
        buttonGroup.add(azerbaijaniRadioButton);
        buttonGroup.add(russianRadioButton);

        // Add radio buttons to the panel
        panel.add(englishRadioButton);
        panel.add(azerbaijaniRadioButton);
        panel.add(russianRadioButton);

        // Create a button to confirm selection
        JButton confirmButton = new JButton("Confirm");

        confirmButton.setBackground(new java.awt.Color(255, 255, 255));
        confirmButton.setForeground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        confirmButton.setFont(new Font("Times New Roman", Font.BOLD, 20));
        System.out.println("before confirmation");
        // Add action listener to the confirm button
        confirmButton.addActionListener(e -> {
            if (englishRadioButton.isSelected()) {
                // Return English language
                frame.dispose(); // Close the language selection window
                openLoginPage("English"); // Open login page with selected language
            } else if (azerbaijaniRadioButton.isSelected()) {
                // Return Azerbaijani language
                frame.dispose(); // Close the language selection window
                openLoginPage("Azerbaijani"); // Open login page with selected language
            } else if (russianRadioButton.isSelected()) {
                // Return Russian language
                frame.dispose(); // Close the language selection window
                openLoginPage("Russian"); // Open login page with selected language
            } else {
                // No language selected
                JOptionPane.showMessageDialog(frame, "Please select a language");
            }
        });
        System.out.println("after confirmation");

        // Add the confirm button to the panel
        panel.add(confirmButton);
        System.out.println(confirmButton);

        // Add the panel to the frame
        frame.getContentPane().add(panel);
        System.out.println("Add the panel to the frame");

        frame.setSize(1024, 768);
        frame.setIconImage(img.getImage());
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);
        System.out.println("Set frame size and make it visible");
    }

    private void openLoginPage(String selectedLanguage) {
        // Open login page with selected language
        System.out.println(selectedLanguage);
        new Login(selectedLanguage);
        System.out.println("execution ended");

    }
}
