package adminPage;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import user.FilterData;
import user.Sort;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;


public class AdminUserDeletion extends JFrame {

    private static final String USER_FILE = "files/user.csv";
    private static final String DATABASE_DIRECTORY = "persons/";

    private DefaultTableModel tableModel;
    private JTable table;
    private JButton deleteButton;
    private JButton homepageButton;
    int titleClicks=0;
    JFrame frame = new JFrame();

    public AdminUserDeletion() {
        
        ImageIcon img = new ImageIcon("src/icon4.jpg");

        setTitle("User Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setIconImage(img.getImage());
        // Create and configure the table
        tableModel = new DefaultTableModel(new String[] { "Users" }, 0);
        table = new JTable(tableModel);
        table.setRowHeight(50); // Set the row height
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
        JTableHeader header = table.getTableHeader();
        header.setBackground(Color.WHITE);
        header.setFont(new Font("Times New Roman", Font.BOLD, 30));
        header.setForeground(Color.RED);
        
        enableSorting();
        
       


        // Set font and alignment for table data
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer();
        cellRenderer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.setDefaultRenderer(Object.class, cellRenderer);
        table.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

               // Create search field
               JTextField searchField = new JTextField(20); // Adjust the size as needed

               // Add a key listener to the search field for live filtering
               searchField.addKeyListener(new KeyAdapter() {
                   @Override
                   public void keyReleased(KeyEvent e) {
                       String query = searchField.getText().trim();
                       FilterData.filterData(query, table, frame);
                   }
               });
       
               // Create a panel to hold the search field
               JPanel searchPanel = new JPanel();
               searchPanel.add(new JLabel("Search: "));
               searchPanel.add(searchField);
       
               // Add the search panel and the scroll pane containing the table to the frame
               add(searchPanel, BorderLayout.NORTH);
               add(new JScrollPane(table), BorderLayout.CENTER);

        // Create the delete button and add action listener
        deleteButton = new JButton("Delete User");
        deleteButton.setForeground(new Color(255, 255, 255));
        deleteButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        deleteButton.setFont(new Font("Times New Roman", Font.BOLD, 24));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedUser();
            }
        });
        deleteButton.setBackground(new Color(26, 24, 82));
        deleteButton.setForeground(Color.WHITE);
        buttonPanel.add(deleteButton);
        

        // Create the homepage button and add action listener
        homepageButton = new JButton("Home");
        homepageButton.setForeground(new Color(255, 255, 255));
        homepageButton.setBackground(Color.getHSBColor(0.6f, 0.4f, 0.3f));
        homepageButton.setFont(new Font("Times New Roman", Font.BOLD, 24));
        homepageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new IntroforAdmin().setVisible(true);
            }
        });
        homepageButton.setBackground(new Color(26, 24, 82));
        homepageButton.setForeground(Color.WHITE);
        buttonPanel.add(homepageButton);

        // Add the button panel to the bottom of the window
        add(buttonPanel, BorderLayout.SOUTH);

        // Load user data
        loadUsers();
    }

 
    private void enableSorting() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        sorter.setSortsOnUpdates(true); // Enable automatic sorting on updates
    
        // Sort the table by default column
        sorter.setSortKeys(java.util.List.of(new RowSorter.SortKey(0, SortOrder.ASCENDING)));
    }
    
    private void loadUsers() {
        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && !parts[0].equals("admin")) { // Exclude admin user
                    tableModel.addRow(new Object[] { parts[0] });
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedUser() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String username = (String) table.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete user '" + username + "'?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            deleteUserData(username); // Delete user's data
            tableModel.removeRow(selectedRow);
            removeFromUsernamesFile(username); // Remove user from usernames.csv
        }
    }

    private void deleteUserData(String username) {
        String databasePath = DATABASE_DIRECTORY + username + ".csv";
        File databaseFile = new File(databasePath);
        if (databaseFile.exists()) {
            if (databaseFile.delete()) {
                System.out.println("User data deleted: " + username);
            } else {
                System.err.println("Failed to delete user data: " + username);
            }
        } else {
            System.out.println("User data not found: " + username);
        }
    }

    private void removeFromUsernamesFile(String username) {
        StringBuilder updatedContent = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(USER_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2 && !parts[0].equals(username)) {
                    updatedContent.append(line).append(System.lineSeparator());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating usernames: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(USER_FILE))) {
            bw.write(updatedContent.toString());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating usernames: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
       
                new AdminUserDeletion().setVisible(true);
          
    }
}
