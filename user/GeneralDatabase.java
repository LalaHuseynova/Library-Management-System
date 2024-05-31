package user;

import javax.swing.*;
import javax.swing.table.*;

import loginSign.Login;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

public class GeneralDatabase extends PersonalDatabase {
    public JFrame jf;
    public JScrollPane js;
    public JTable jt;
    public String[] col;
    public Object[][] data;
    public JTextField searchField;
    int authorClicks, titleClicks, ratingClicks = 0;

    public GeneralDatabase() {
        frame.dispose();
        ImageIcon img = new ImageIcon("src/icon4.jpg");

        jf = new JFrame("General Database");
        col = new String[] { "Title", "Author", "Rating", "Review" };
        data = getData();

        ratingCalculator();
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
                frame.dispose();
                new IntroforUser();
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String query = searchField.getText().trim();
                FilterData.filterData(query, jt, frame);
            }
        });

        JButton selecButton = new JButton("Select");
        selecButton.setForeground(new Color(255, 255, 255));
        selecButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        selecButton.setFont(new Font("Times New Roman", Font.BOLD, 24));
        selecButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {

                    int selectedRow = jt.getSelectedRow();
                    if (selectedRow != -1) {

                        String username = Login.getLoggedInUsername();
                        showAddPersonal(selectedRow, username, jf);
                    }
                } catch (AlreadyTaken ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }

            }

            private void showAddPersonal(int selectedRow, String username, JFrame jf) {
                DefaultTableModel model = (DefaultTableModel) jt.getModel();
                Vector rowData = (Vector) model.getDataVector().get(selectedRow);

                String title = (String) rowData.get(0);
                String author = (String) rowData.get(1);
                String review = (String) rowData.get(2);
                String rating = (String) rowData.get(3);

                String confirmationMessage = String.format(
                        "Arfe you sure you want to add the following data to your personal database?\n\nTitle: %s\nAuthor: %s\nReview: %s\nRating: %s",
                        title, author, review, rating);

                int confirmationResult = JOptionPane.showConfirmDialog(jf, confirmationMessage, "Confirm Addition",
                        JOptionPane.YES_NO_OPTION);

                if (confirmationResult == JOptionPane.YES_OPTION) {
                    String personalDatabaseFilename = username + ".csv";

                    String personalDatabaseFolderPath = "persons";

                    File folder = new File(personalDatabaseFolderPath);

                    File[] files = folder.listFiles();

                    File[] filteredFiles = Arrays.stream(files)
                            .filter(file -> file.getName().equals(personalDatabaseFilename))
                            .toArray(File[]::new);

                    if (filteredFiles.length > 0) {
                        File personalDatabaseFile = filteredFiles[0];

                        try {
                            String dataLine = String.format("%s,%s,%s,%s,Not Started,0,0,0,Add rating,Add review\n",
                                    title, author, rating, review);

                            FileWriter fw = new FileWriter(personalDatabaseFile, true);
                            BufferedWriter bw = new BufferedWriter(fw);
                            bw.write(dataLine);
                            bw.close();
                            fw.close();

                            JOptionPane.showMessageDialog(jf, "Data added to personal database successfully!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);

                        } catch (IOException e) {
                            e.printStackTrace();
                            JOptionPane.showMessageDialog(jf, "Error occurred while adding data to personal database.",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(jf, "Personal database file not found.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
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
                        // If there is a review, display it
                        displayUserReview(title, author, username);
                    }
                }
            }
        });
        // Add buttons to buttonPanel
        buttonPanel.add(homeButton);
        buttonPanel.add(selecButton);

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

    public static String getRatingFromGeneralDatabase(Object[][] data, String title, String author) {
        for (int i = 0; i < data.length; i++) {
            String dbTitle = (String) data[i][0];
            String dbAuthor = (String) data[i][1];
            if (dbTitle.equalsIgnoreCase(title) && dbAuthor.equalsIgnoreCase(author)) {
                // Return the rating from the general database
                return (String) data[i][2];
            }
        }
        // If no matching title and author are found, return null
        return null;
    }

    private void setRowHeight(int height) {
        jt.setRowHeight(height);
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
                    // Append the review to the StringBuilder
                    if (review.length() > 0) {
                        review.append(", "); // Add comma and space if not the first review
                    }
                    review.append(parts[3].trim()); // Append the review part
                }
            }
            // Check if any reviews were found
            if (review.length() > 0) {
                return review.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "No review"; // Return "No review" if no matching title is found or no reviews were found
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

    private String removeQuotationMarks(String s) {
        return s.replace("\"", ""); // Remove all occurrences of double quotation marks
    }

    private void ratingCalculator() {
        RatingCalculator ratingCalculator = new RatingCalculator();
        Map<String, Object[]> ratingInfo = RatingCalculator.calculateAverageRatingsAndCountsForAllBooks();

        for (int i = 0; i < data.length; i++) {
            String title = (String) data[i][0];
            if (ratingInfo.containsKey(title)) {
                Object[] info = ratingInfo.get(title);
                String averageRating = String.valueOf(info[0]);
                int countOfRatings = (int) info[1];
                String ratingInfoString = averageRating + " (" + countOfRatings + ")";
                data[i][2] = ratingInfoString;
                String author = (String) data[i][1];
                String rating = averageRating;
                updateRating(title, author, ratingInfoString);
            }
        }
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

    private void reapplyCellRenderer() {
        // Center-align text in cells
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < jt.getColumnCount(); i++) {
            jt.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            jt.getTableHeader().setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));

            jt.getTableHeader().setForeground(Color.WHITE);
        }
    }

    public static void main(String[] args) {
        new GeneralDatabase();
    }
}