package user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import abstractAndInterface.AbstractCSV;
import loginSign.Login;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.List;

public class PersonalDatabase implements AbstractCSV {

    private DefaultTableModel model;
    private String[] columnHeaders = { "Title", "Author", "Review", "Rating", "Status", "Time Spent",
            "Start Date", "End Date", "User Rating", "User Review" };

    public JFrame frame;
    LocalDate currentDate = LocalDate.now();
    int authorClicks, titleClicks, reviewClicks, ratingClicks, timeSpentClicks, startDateClicks, endDateClicks,
            userRatingClicks, userReviewClicks, statusClicks = 0;
    public JTable personalTable;
    // Format the current date
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy");
    String formattedDate = currentDate.format(formatter);
    public JTextField searchField;

    String username = Login.getLoggedInUsername();
    String path = "persons/" + username + ".csv";
    String user = "files/personal.csv";

    public PersonalDatabase() {
        frame = new JFrame("Personal Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ImageIcon img = new ImageIcon("src/icon4.jpg");

        // Create a table model with no data initially
        model = new DefaultTableModel(columnHeaders, 0);

        // Create a JTable with the model
        personalTable = new JTable(model);
        personalTable.setRowHeight(50);
        Sort sorter = new Sort(personalTable);
        // Set font size for table headers
        JTableHeader header = personalTable.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 16)); // Adjust font size as needed
        header.setForeground(Color.WHITE); // Set text color
        header.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f)); // Set background color

        loadDataFromCSV(path);
        initComponents();
        // Create search field
        searchField = new JTextField(20); // Adjust the size as needed

        // Add a key listener to the search field for live filtering
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().trim();
                FilterData.filterData(query, personalTable, frame);
            }
        });

        // Create a panel to hold the search field
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Search: "));
        searchPanel.add(searchField);

        // Add the search panel and the scroll pane containing the table to the frame
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(personalTable), BorderLayout.CENTER);

        // Create buttons for home and return book
        JButton homeButton = new JButton("Home");
        JButton returnButton = new JButton("Return Book");
        homeButton.setForeground(new Color(255, 255, 255));
        homeButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        homeButton.setFont(new Font("Times New Roman", Font.BOLD, 24));
        // Add action listeners for buttons
        homeButton.addActionListener(e -> {
            frame.dispose();
            new IntroforUser();
            new RatingCalculator().main(columnHeaders);

        });

        returnButton.setForeground(new Color(255, 255, 255));
        returnButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        returnButton.setFont(new Font("Times New Roman", Font.BOLD, 24));
        returnButton.addActionListener(e -> {
            int selectedRow = personalTable.getSelectedRow();
            if (selectedRow != -1) {
                int option = JOptionPane.showConfirmDialog(frame, "Are you sure you want to return the book?",
                        "Confirmation", JOptionPane.YES_NO_OPTION);
                if (option == JOptionPane.YES_OPTION) {
                    model.removeRow(selectedRow); // Remove the selected row from the personalTable model
                    saveDataToCSV(path); // Save the updated data to the CSV file
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a row to return the book.");
            }
        });

        JButton addButton = new JButton("Add Book");
        addButton.setForeground(new Color(255, 255, 255));
        addButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        addButton.setFont(new Font("Times New Roman", Font.BOLD, 24));
        // Add action listeners for buttons
        addButton.addActionListener(e -> {
            frame.dispose();
            new GeneralDatabase();
          
        });

        JButton createButton = new JButton("Create own book");
        createButton.setForeground(new Color(255, 255, 255));
        createButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        createButton.setFont(new Font("Times New Roman", Font.BOLD, 24));

        // Add action listener for the create button
        createButton.addActionListener(e -> {
            String bookName = JOptionPane.showInputDialog(frame, "Enter the name of the title:");
            String authorName = JOptionPane.showInputDialog(frame, "Enter the name of the author:");

            // Check if both book name and author name are provided
            if (bookName != null && authorName != null && !bookName.isEmpty() && !authorName.isEmpty()) {
                // Add the book entry to the table
                Object[] rowData = { bookName, authorName, "Unvailable", "Unvailable", "Not Started", "0", "0", "0",
                        "Add Rating", "Add Review" };
                model.addRow(rowData);

                // Save the updated data to the CSV file
                saveDataToCSV(path);
            } else {
                JOptionPane.showMessageDialog(frame, "Please provide both book name and author name.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Create a panel to hold the buttons
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(createButton);

        buttonPanel.add(homeButton);
        buttonPanel.add(returnButton);
        buttonPanel.add(addButton);
        // Add the personalTable and button panel to the frame
        frame.add(new JScrollPane(personalTable), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set frame properties
        frame.setSize(1024, 768);
        frame.setIconImage(img.getImage());
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true);

        // Save data to CSV file when the application is closed
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                saveDataToCSV(path);
            }
        });

    }

    void initComponents() {
        // Add mouse listener to the table
        personalTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int ratingColumn = personalTable.getColumnModel().getColumnIndex("User Rating");
                int reviewColumn = personalTable.getColumnModel().getColumnIndex("User Review");
                int generalRating = personalTable.getColumnModel().getColumnIndex("Rating");
                int titleColumn = personalTable.getColumnModel().getColumnIndex("Title");

                int row = personalTable.getSelectedRow();
                int column = personalTable.getSelectedColumn();

                if (column == ratingColumn) {
                    // Get the rating directly from user input
                    String selectedRating = JOptionPane.showInputDialog(frame, "Enter the rating (1-5):");
                    // Check if the input is not null and within the range 1-5
                    if (selectedRating != null && !selectedRating.isEmpty()) {
                        int rating = Integer.parseInt(selectedRating);
                        if (rating >= 1 && rating <= 5) {
                            personalTable.setValueAt(selectedRating, row, ratingColumn);
                            saveDataToCSV(path);
                            // Call updateRating to update the rating in the model and CSV file
                            String title = (String) personalTable.getValueAt(row, 0); // Get the title
                            String author = (String) personalTable.getValueAt(row, 1); // Get the author

                            updateRating(title, author, selectedRating);

                        } else {
                            JOptionPane.showMessageDialog(frame, "Please enter a rating between 1 and 5.",
                                    "Invalid Rating", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else if (column == reviewColumn) {
                    // Display options to change or view review
                    int option = JOptionPane.showOptionDialog(frame, "Choose:",
                            "User Review", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
                            null, new String[] { "Write", "View" }, "Change");
                    if (option == JOptionPane.YES_OPTION) {
                        // User chose to change the review
                        String userReview = JOptionPane.showInputDialog(frame, "Enter your review:");
                        if (userReview != null && !userReview.isEmpty()) {
                            // Set the user review in the selected cell
                            personalTable.setValueAt(userReview, row, reviewColumn);
                            // Update the data model
                            model.setValueAt(userReview, row, reviewColumn);
                            // Save the data to the CSV file
                            saveDataToCSV(path);
                            GeneralDatabase.getData();

                        } else {
                            // User deleted the review without writing anything, set default value
                            personalTable.setValueAt("Add Review", row, reviewColumn);
                            // Update the data model
                            model.setValueAt("Add Review", row, reviewColumn);
                            // Save the data to the CSV file
                            saveDataToCSV(path);
                        }
                    } else if (option == JOptionPane.NO_OPTION) {
                        String currentReview = (String) personalTable.getValueAt(row, reviewColumn);
                        JOptionPane.showMessageDialog(frame, currentReview, "User Review",
                                JOptionPane.PLAIN_MESSAGE);
                    }
                }
            }

        });

        personalTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int startDateColumn = personalTable.getColumnModel().getColumnIndex("Start Date");
                int endDateColumn = personalTable.getColumnModel().getColumnIndex("End Date");
                int timeSpentColumn = personalTable.getColumnModel().getColumnIndex("Time Spent");
                int statusColumn = personalTable.getColumnModel().getColumnIndex("Status");
                int row = personalTable.getSelectedRow();
                int column = personalTable.getSelectedColumn();
        
                // Check for right-click to delete date
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (column == startDateColumn || column == endDateColumn) {
                        personalTable.setValueAt("", row, column); // Clear the date
                        // Reset time spent and status
        
                        saveDataToCSV(path);
                    }
                } else if (row != -1 && column != -1) {
                    if (column == startDateColumn || column == endDateColumn) {
                        // Display a dialog to choose the date
                        String selectedDate = JOptionPane.showInputDialog(frame,
                                "Enter the date (YYYY-MM-DD):", "Date", JOptionPane.PLAIN_MESSAGE);
        
                        if (selectedDate != null && !selectedDate.isEmpty()) {
                            String[] parts = selectedDate.split("-");
                            try {
                                int year = Integer.parseInt(parts[0]);
                                int month = Integer.parseInt(parts[1]);
                                int day = Integer.parseInt(parts[2]);
        
                                if (Time.isValidFormat(year, month, day)) {
                                    personalTable.setValueAt(selectedDate, row, column);
                                } else {
                                    System.out.println("mispreseny");
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(frame, "Invalid date format", "Error",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(frame, "Invalid date", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
        
                    String startDate = (String) personalTable.getValueAt(row, startDateColumn);
                    String endDate = (String) personalTable.getValueAt(row, endDateColumn);
        
                    if (startDate != null && endDate != null) {
                        if (endDate.equals("0") && !startDate.equals("0")) {
                            // End date is "0", indicating the task has started but not finished
                            String status = "Ongoing";
                            personalTable.setValueAt(status, row, statusColumn);
                        } else if (!endDate.isEmpty()) {
                            long timeSpent = Time.calculateTimeSpent(startDate, endDate);
                            personalTable.setValueAt(timeSpent + " minutes", row, timeSpentColumn);
        
                            // Check status based on dates
                            LocalDate startDateParsed = LocalDate.parse(startDate);
                            LocalDate endDateParsed = LocalDate.parse(endDate);
                            LocalDate formattedDateParsed = LocalDate.parse(formattedDate, formatter);
        
                            if (startDateParsed.isAfter(formattedDateParsed)
                                    || (startDate.equals("0") && endDate.equals("0"))) {
                                // Task hasn't started yet
                                String status = "Not Started";
                                personalTable.setValueAt(status, row, statusColumn);
                            } else if (endDateParsed.isAfter(formattedDateParsed)) {
                                // Task started and ongoing
                                String status = "Ongoing";
                                personalTable.setValueAt(status, row, statusColumn);
                            } else {
                                // Task started and finished before or on the current date
                                String status = "Finished";
                                personalTable.setValueAt(status, row, statusColumn);
                            }
                        }
                        saveDataToCSV(path);
                    }
                }
            }
        });
        

    }

    // Load data from CSV file
    @Override
    public void loadDataFromCSV(String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Skip the first line
            reader.readLine();
            // Read the rest of the lines into a list of strings
            List<String> lines = reader.lines().collect(Collectors.toList());

            // Process each line and add data to the personalTable model
            lines.forEach(line -> {
                String[] rowData = line.split(",");
                model.addRow(rowData);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRating(String title, String author, String rating) {
        // Fetch the rating from the general database
        String generalRating = GeneralDatabase.getRatingFromGeneralDatabase(GeneralDatabase.getData(), title, author);

        if (generalRating != null) {
            DefaultTableModel personalModel = (DefaultTableModel) personalTable.getModel();
            for (int i = 0; i < personalModel.getRowCount(); i++) {
                String rowTitle = (String) personalModel.getValueAt(i, 0);
                String rowAuthor = (String) personalModel.getValueAt(i, 1);
                if (rowTitle.equals(title) && rowAuthor.equals(author)) {
                    // Update the rating in the personal table with the fetched rating
                    personalModel.setValueAt(generalRating, i, 3); // Assuming the rating column is at index 2 in
                                                                   // personalTable
                    saveDataToCSV("persons/" + Login.getLoggedInUsername() + ".csv"); // Save the changes to the CSV
                                                                                      // file
                    break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Rating not found in general database.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Main method to launch the application
    @Override
    public void saveDataToCSV(String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // Write column headers
            writer.write(Arrays.stream(columnHeaders)
                    .collect(Collectors.joining(",")) + "\n");

            // Write data
            model.getDataVector().stream().forEach(row -> {
                try {
                    writer.write(row.stream()
                            .map(Object::toString)
                            .collect(Collectors.joining(",")) + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}