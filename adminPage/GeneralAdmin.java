package adminPage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.*;

import abstractAndInterface.AbstractTableCSV;
import user.FilterData;
import user.Sort;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GeneralAdmin extends AbstractTableCSV {
    public JFrame jf;
    public JScrollPane js;
    public JTable jt;
    public String[] col;
    public Object[][] data;
    public JTextField searchField;
    int authorClicks, titleClicks, ratingClicks = 0;

    public GeneralAdmin() {

        ImageIcon img = new ImageIcon("src/icon4.jpg");

        jf = new JFrame("General Database");
        col = new String[] { "Title", "Author", "Rating", "Review" };
        data = getData();

        // Create a JTable with the model
        jt = new JTable(new DefaultTableModel(data, col) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        });

        // Enable row selection
        jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Sort sorter = new Sort(jt);

        // jt.setEnabled(false); // Make the entire table non-editable
        js = new JScrollPane(jt);
        Font font = new Font("New Times Roman", Font.PLAIN, 14);
        jt.setFont(font);

        // Center-align text in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < jt.getColumnCount(); i++) {
            jt.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set horizontal alignment for each column
        for (int i = 0; i < jt.getColumnCount(); i++) {
            jt.getColumnModel().getColumn(i).setHeaderRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                            column);
                    JLabel label = (JLabel) c;
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
                    label.setForeground(Color.WHITE);
                    return label;
                }
            });
        }

        // Initialize search field
        searchField = new JTextField(30);

        // Add search components
        JPanel searchPanel = new JPanel();
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));

        // Add search components to searchPanel
        JLabel searchTitle = new JLabel("Search: ");
        searchTitle.setBackground(new java.awt.Color(255, 255, 255));
        searchTitle.setForeground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        searchTitle.setFont(new Font("Times New Roman", Font.BOLD, 20));

        searchPanel.add(searchTitle);

        searchPanel.add(searchField);

        JButton homeButton = new JButton("Home");
        homeButton.setForeground(new Color(255, 255, 255));
        homeButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        homeButton.setFont(new Font("Times New Roman", Font.BOLD, 24));

        // ActionListener for Home button
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();

                new IntroforAdmin();
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String query = searchField.getText().toLowerCase();
               FilterData.filterData(query, jt, jf);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String query = searchField.getText().toLowerCase();
                FilterData.filterData(query, jt, jf);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                String query = searchField.getText().toLowerCase();
                FilterData.filterData(query, jt, jf);
            }
        });
        JButton deleteButton = new JButton("Delete books");

        deleteButton.setForeground(new Color(255, 255, 255));
        deleteButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        deleteButton.setFont(new Font("Times New Roman", Font.BOLD, 24));
        // Inside your General class

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = jt.getSelectedRow();
                if (selectedRow != -1) { // If a row is selected
                    int option = JOptionPane.showConfirmDialog(jf, "Are you sure you want to delete this book?",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (option == JOptionPane.YES_OPTION) {
                        // Remove the row from the data array
                        Object[][] newData = new Object[data.length - 1][4];
                        for (int i = 0, k = 0; i < data.length; i++) {
                            if (i != selectedRow) {
                                newData[k++] = data[i];
                            }
                        }
                        data = newData;
                        // Update the table model
                        jt.setModel(new DefaultTableModel(data, col));
                        // Reset font size and alignment
                        setTableCellAlignmentAndFont();
                        saveDataToCSV("files/personal.csv", jt);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a row to delete.");
                }
            }

        });

        JButton changeButton = new JButton("Change");
        changeButton.setForeground(new Color(255, 255, 255));
        changeButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        changeButton.setFont(new Font("Times New Roman", Font.BOLD, 24));

        changeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = jt.getSelectedRow();
                if (selectedRow != -1) {
                    String newTitle = JOptionPane.showInputDialog(jf, "Enter new Title:");
                    if (newTitle != null) {
                        String newAuthor = JOptionPane.showInputDialog(jf, "Enter new Author:");
                        if (newAuthor != null) {
                            data[selectedRow][0] = newTitle; // Update title in the data array
                            data[selectedRow][1] = newAuthor; // Update author in the data array
                            jt.setModel(new javax.swing.table.DefaultTableModel(data, col)); // Update the table
                            saveDataToCSV("files/personal.csv", jt);
                            setTableCellAlignmentAndFont();
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(jf, "Please select a row to update.");
                }
            }
        });

        JButton addButton = new JButton("Add books");
        addButton.setForeground(new Color(255, 255, 255));
        addButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        addButton.setFont(new Font("Times New Roman", Font.BOLD, 24));
        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                jf.dispose();
                new BookAdder();
                saveDataToCSV("files/personal.csv", jt);
                loadDataFromCSV("files/personal.csv", jt);
            }

        });

        jt.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = jt.getColumnModel().getColumnIndex("Review");
                int row = jt.rowAtPoint(e.getPoint());
                if (column == jt.columnAtPoint(e.getPoint())) {
                    String title = (String) jt.getValueAt(row, 0); // Get the title of the book
                    String author = (String) jt.getValueAt(row, 1); // Get the author of the book
                    String username = (String) jt.getValueAt(row, 3); // Get the username from the "Review" column
                    if (!username.equals("No review")) {
                        // If there is a review, display options to view or delete it
                        int option = JOptionPane.showOptionDialog(jf, "Choose an action for this review:",
                                "Review Options",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                                new String[] { "View", "Delete" }, null);
                        if (option == JOptionPane.YES_OPTION) {
                            // View the review
                            displayUserReview(title, author, username);
                            saveDataToCSV("files/personal.csv", jt);
                        } else if (option == JOptionPane.NO_OPTION) {
                            // Delete the review
                            deleteReview(username, title, author);
                            jt.setValueAt("No review", row, 3);
                            saveDataToCSV("files/personal.csv", jt);

                        }
                    } else {
                        JOptionPane.showMessageDialog(jf, "No review available for this book.", "Review Options",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
        // Add buttons to buttonPanel
        buttonPanel.add(homeButton);
        buttonPanel.add(addButton);

        buttonPanel.add(changeButton);
        buttonPanel.add(deleteButton);
        // Add components to frame
        jf.setLayout(new BorderLayout());
        jf.add(searchPanel, BorderLayout.NORTH);
        jf.add(js, BorderLayout.CENTER);
        jf.add(buttonPanel, BorderLayout.SOUTH);

        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.pack();
        jf.setSize(1024, 768);
        jf.setIconImage(img.getImage());
        jf.setVisible(true);
        setRowHeight(50);

    }

    private void setRowHeight(int height) {
        jt.setRowHeight(height);
    }

    public void setTableCellAlignmentAndFont() {
        // Center-align text in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < jt.getColumnCount(); i++) {
            jt.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set font for table header
        jt.getTableHeader().setFont(new Font("Times New Roman", Font.BOLD, 14));
        // Set header color
        jt.getTableHeader().setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        jt.getTableHeader().setForeground(Color.WHITE);
    }

    public static Object[][] getData() {
        ArrayList<Object[]> dataList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("files/personal.csv"))) {
            String line;
            boolean isFirstLine = true; // Variable to track if it's the first line
    
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the first line
                }
    
                String[] parts = line.split(",");
                Object[] newData = new Object[parts.length + 1]; // Increase the size by 1 for the review column
                for (int i = 0; i < parts.length; i++) {
                    newData[i] = parts[i].trim(); // Trim to remove leading/trailing whitespace
                }
                // Read the fourth column from the reviews.csv file
                String review = readReviewFromReviewsCSV(newData[0].toString().trim());
                newData[parts.length] = review.trim(); // Add the review to the newData array
                dataList.add(newData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    
        // Convert ArrayList to two-dimensional array
        Object[][] data = new Object[dataList.size()][];
        for (int i = 0; i < dataList.size(); i++) {
            data[i] = dataList.get(i);
        }
    
        return data;
    }
    private void deleteReview(String reviewer, String title, String author) {
        try {
            // Construct reviewer's file path
            String reviewerFilePath = "persons/" + reviewer + ".csv";
            System.out.println("Reviewer File Path: " + reviewerFilePath); // Print file path

            // Read the reviewer's file
            File file = new File(reviewerFilePath);
            List<String> lines = Files.readAllLines(file.toPath());

            // Write the updated file, replacing the content of the 10th column with
            // "Deleted by admin"
            FileWriter writer = new FileWriter(file);
            for (String line : lines) {
                String[] parts = line.split(",");
                String reviewTitle = parts[0].trim();
                String reviewAuthor = parts[1].trim();
                if (reviewTitle.equalsIgnoreCase(title) && reviewAuthor.equalsIgnoreCase(author)) {
                    // Replace the content of the 10th column with "Deleted by admin"
                    parts[9] = "Deleted by admin";
                    // Concatenate the parts back into a line
                    String updatedLine = String.join(",", parts);
                    writer.write(updatedLine + System.lineSeparator());
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }
            writer.close();
            JOptionPane.showMessageDialog(jf, "Review deleted successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(jf, "Error occurred while deleting the review.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String readReviewFromReviewsCSV(String title) {
        try (BufferedReader br = new BufferedReader(new FileReader("files/reviews.csv"))) {
            String line;
            boolean isFirstLine = true; // Variable to track if it's the first line
            StringBuilder review = new StringBuilder(); // StringBuilder to store the review

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Skip the first line
                }

                String[] parts = line.split(",");
                if (parts.length >= 4 && parts[0].trim().equalsIgnoreCase(title)) {
                    // Append the username to the review StringBuilder
                    if (review.length() > 0) {
                        review.append(", ");
                    }
                    review.append(parts[2].trim());
                }
            }
            // Check if any usernames were found
            if (review.length() > 0) {
                return review.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No review"; // Return "No review" if no matching title is found or no usernames were found
    }
    
    private void displayUserReview(String title, String author, String username) {
        try (BufferedReader br = new BufferedReader(new FileReader("files/reviews.csv"))) {
            String line;
            boolean isFirstLine = true;
            StringBuilder userReview = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }

                String[] parts = line.split(",");
                String reviewTitle = parts[0].trim();
                String reviewAuthor = parts[1].trim();
                String reviewer = parts[2].trim();
                String review = parts[3].trim();

                // Check if the review is for the same book, author, and user
                if (reviewTitle.equalsIgnoreCase(title) && reviewAuthor.equalsIgnoreCase(author)
                        && reviewer.equalsIgnoreCase(username)) {
                    userReview.append(review).append("\n");
                }
            }

            if (userReview.length() > 0) {
                // If review found, display it in a dialog
                JOptionPane.showMessageDialog(jf, userReview.toString(), "User Review",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(jf, "No review found for this book by the selected user.", "User Review",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(jf, "Error occurred while reading reviews.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to show the review in a new frame
    private void showReviewInNewFrame(String review) {
        JFrame reviewTextFrame = new JFrame("Review");
    
        JTextArea textArea = new JTextArea(review);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        reviewTextFrame.add(scrollPane);
        reviewTextFrame.setSize(400, 300); // Set size as needed
        reviewTextFrame.setLocationRelativeTo(null); // Center the frame
        reviewTextFrame.setVisible(true);
    }
    private void deleteReview(String reviewer, String title, String author, JFrame reviewFrame) {
        try {
            // Construct reviewer's file path
            String reviewerFilePath = "persons/" + reviewer + ".csv";
            System.out.println("Reviewer File Path: " + reviewerFilePath); // Print file path
    
            // Read the reviewer's file
            File file = new File(reviewerFilePath);
            List<String> lines = Files.readAllLines(file.toPath());
    
            // Write the updated file, replacing the content of the 10th column with
            // "Deleted by admin"
            FileWriter writer = new FileWriter(file);
            for (String line : lines) {
                String[] parts = line.split(",");
                String reviewTitle = parts[0].trim();
                String reviewAuthor = parts[1].trim();
                if (reviewTitle.equalsIgnoreCase(title) && reviewAuthor.equalsIgnoreCase(author)) {
                    // Replace the content of the 10th column with "Deleted by admin"
                    parts[9] = "Deleted by admin";
                    // Concatenate the parts back into a line
                    String updatedLine = String.join(",", parts);
                    writer.write(updatedLine + System.lineSeparator());
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }
            writer.close();
            JOptionPane.showMessageDialog(reviewFrame, "Review deleted successfully.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
    
            // Now, delete the review from the main reviews.csv file
            deleteReviewFromMainFile(title, author,reviewFrame);
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(reviewFrame, "Error occurred while deleting the review.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteReviewFromMainFile(String title, String author,JFrame reviewFrame) {
        try {
            // Read the main reviews file
            File mainFile = new File("files/reviews.csv");
            List<String> lines = Files.readAllLines(mainFile.toPath());
    
            // Write the updated file, excluding the deleted review
            FileWriter writer = new FileWriter(mainFile);
            boolean hasRemainingReviews = false;
            for (String line : lines) {
                String[] parts = line.split(",");
                String reviewTitle = parts[0].trim();
                String reviewAuthor = parts[1].trim();
                if (!(reviewTitle.equalsIgnoreCase(title) && reviewAuthor.equalsIgnoreCase(author))) {
                    writer.write(line + System.lineSeparator());
                    hasRemainingReviews = true; // Mark that there are remaining reviews
                }
            }
            writer.close();
    
            // If there are no remaining reviews, update the message
            if (!hasRemainingReviews) {
                JOptionPane.showMessageDialog(reviewFrame, "No reviews found for this book.", "User Reviews",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private String removeQuotationMarks(String s) {
        return s.replace("\"", ""); // Remove all occurrences of double quotation marks
    }

    public static boolean isNumeric(Object[] str) {
        try {
            // Try parsing the string as an integer
            Integer.parseInt((String) str[0]);
            return true; // If successful, it's numeric
        } catch (NumberFormatException e1) {
            try {
                // Try parsing the string as a double
                Double.parseDouble((String) str[0]);
                return true; // If successful, it's numeric
            } catch (NumberFormatException e2) {
                // Both attempts failed, so it's not numeric
                return false;
            }
        }
    }

   

    public static void main(String[] args) {
        new GeneralAdmin();
    }

    @Override
    public void saveDataToCSV(String filename, JTable table) {
        try {
            TableModel model = table.getModel();
            FileWriter csvWriter = new FileWriter(filename);

            // Write headers
            for (int i = 0; i < model.getColumnCount(); i++) {
                csvWriter.append(model.getColumnName(i));
                if (i < model.getColumnCount() - 1) {
                    csvWriter.append(",");
                }
            }
            csvWriter.append("\n");

            // Write data
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    csvWriter.append(model.getValueAt(i, j).toString());
                    if (j < model.getColumnCount() - 1) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }

            csvWriter.flush();
            csvWriter.close();
            System.out.println("CSV file has been saved successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void loadDataFromCSV(String filename, JTable table) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0); // Clear existing data

        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                model.addRow(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(jf, "Error occurred while loading data from CSV.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}