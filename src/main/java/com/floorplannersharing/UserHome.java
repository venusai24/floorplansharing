package com.floorplannersharing;
import javax.swing.*;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.WriteMode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.List;
import java.time.LocalDateTime;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class UserHome extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JPanel centerPanel;
    private File selectedFile;
    private DbxClientV2 dropboxClient;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UserHome frame = new UserHome("SampleUser");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public UserHome() {

    }

    /**
     * Create the frame.
     */
    public UserHome(String userName) {
        setTitle("Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        // Left panel for navigation
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(240, 240, 240));
        leftPanel.setPreferredSize(new Dimension(200, 600));

        JButton createNewButton = new JButton("+ Create new");
        createNewButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        createNewButton.setPreferredSize(new Dimension(180, 40));

        String[] navItems = {"All Plans", "Uploaded", "Saved", "Most Popular", "Archived", "Account"};
        for (String item : navItems) {
            JButton navButton = new JButton(item);
            navButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            navButton.setMaximumSize(new Dimension(180, 30));
            leftPanel.add(navButton);
            leftPanel.add(Box.createRigidArea(new Dimension(0, 5)));

            if (item.equals("Account")) {
                navButton.addActionListener(e -> displayAccountDetails(userName));
            } else if (item.equals("Uploaded")) {
                navButton.addActionListener(e -> displayUploadedFiles(userName));
            } else if (item.equals("All Plans")) {
                navButton.addActionListener(e -> displayUploadedFiles(userName));
            }
        }

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        leftPanel.add(createNewButton);

        JButton uploadBottomButton = new JButton("Upload");
        uploadBottomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadBottomButton.setPreferredSize(new Dimension(180, 40));
        leftPanel.add(uploadBottomButton);
        uploadBottomButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadBottomButton.addActionListener(e -> handleFileUpload(userName));

        // Top panel for dashboard title and search
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel dashboardTitle = new JLabel("Dashboard");
        dashboardTitle.setFont(new Font("SansSerif", Font.BOLD, 24));

        JTextField searchBar = new JTextField("Search for documents, files, etc");
        searchBar.setPreferredSize(new Dimension(300, 30));

        topPanel.add(dashboardTitle, BorderLayout.WEST);
        topPanel.add(searchBar, BorderLayout.EAST);

        // Center panel for dashboard content
        centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());

        // File Upload Section
        JPanel uploadPanel = new JPanel(new BorderLayout());
        JLabel uploadLabel = new JLabel("Upload your files");
        uploadLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        uploadLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton uploadButton = new JButton("Choose files");
        uploadButton.setPreferredSize(new Dimension(120, 40));
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(uploadButton);
        uploadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        uploadButton.addActionListener(e -> handleFileUpload(userName));

        uploadPanel.add(uploadLabel, BorderLayout.NORTH);
        uploadPanel.add(buttonPanel, BorderLayout.CENTER);

        centerPanel.add(uploadPanel, BorderLayout.CENTER);

        // Adding components to the main frame
        add(leftPanel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);

        initDropboxClient();
    }

    private void displayAccountDetails(String userName) {
        centerPanel.removeAll();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        JLabel userLabel = new JLabel("Username: " + userName);
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel uploadsLabel = new JLabel("Number of uploads: 5"); // Example data
        uploadsLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        uploadsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel savedFilesLabel = new JLabel("Number of saved files: 3"); // Example data
        savedFilesLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        savedFilesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Logged out successfully!");
                System.exit(0);
            }
        });

        JButton changePasswordButton = new JButton("Change Password");
        changePasswordButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Change password functionality coming soon.");
            }
        });

        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(userLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(uploadsLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(savedFilesLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        centerPanel.add(logoutButton);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        centerPanel.add(changePasswordButton);

        centerPanel.revalidate();
        centerPanel.repaint();
    }
     
    private void initDropboxClient() {
    try {
        // Load config.properties from the resources folder
      ////  InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.properties");
//
        //if (inputStream == null) {
        //    throw new FileNotFoundException("Property file 'config.properties' not found in the classpath");
        //}
//
        //Properties properties = new Properties();
        //properties.load(inputStream);
//
        String accessToken = "sl.CDEETZVyU-dFgjVqOZqD5Vequ39ZOYKRE0LDobnUh-7wE1Cm-LARrdkto5XWz2w9dXHIa0jX80v7yt4ssgsMZty-efMSnLqFqDKS8JxDlZT4ii-gwEg3cERRdjVREXaMn9WAR0QM_IMlCHFSIc7k";
        //if (accessToken == null || accessToken.isEmpty()) {
        //    throw new IllegalArgumentException("DROPBOX_ACCESS_TOKEN is not configured in config.properties");
        //}

        // Initialize Dropbox client
        dropboxClient = new DbxClientV2(new DbxRequestConfig("JavaDropboxClient"), accessToken);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to initialize Dropbox client: " + e.getMessage());
    }
}
    public void downloadFile(String dropboxFilePath, String localFilePath) {
        try {
            // Create an output stream for the local file
            try (FileOutputStream outputStream = new FileOutputStream(localFilePath)) {
                FileMetadata metadata = dropboxClient.files()
                        .downloadBuilder(dropboxFilePath)
                        .download(outputStream);

                System.out.println("File downloaded successfully!");
                System.out.println("Downloaded File Metadata:");
                System.out.println("Name: " + metadata.getName());
                System.out.println("Path: " + metadata.getPathLower());
                System.out.println("Size: " + metadata.getSize() + " bytes");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to download file: " + e.getMessage());
        }
    }

    /**
     * Handle File Selection and Metadata Input
     */
    private void handleFileUpload(String userName) {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();

            JTextField fileNameField = new JTextField(selectedFile.getName(), 20);
            JComboBox<String> accessModeCombo = new JComboBox<>(new String[]{"Public", "Private"});
            JTextField customUsersField = new JTextField();

            centerPanel.removeAll();
            centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

            JButton confirmUpload = new JButton("Confirm Upload");
            confirmUpload.addActionListener(e -> uploadFileToDropbox(
                userName, fileNameField.getText(), (String) accessModeCombo.getSelectedItem(), customUsersField.getText()
            ));

            centerPanel.add(new JLabel("File Name:"));
            centerPanel.add(fileNameField);
            centerPanel.add(new JLabel("Access Mode:"));
            centerPanel.add(accessModeCombo);
            centerPanel.add(new JLabel("Custom Users (comma-separated, if applicable):"));
            centerPanel.add(customUsersField);
            centerPanel.add(confirmUpload);

            centerPanel.revalidate();
            centerPanel.repaint();
        }
    }

    /**
     * Upload File to Dropbox
     */

     private void displayUploadedFiles(String userName) {
        centerPanel.removeAll();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        String url = "jdbc:mysql://localhost:3306/floorplanner_db"; // Replace with your database URL
        String username = "root"; // Replace with your database username
        String password = "venusql2024"; // Replace with your database password
        
        // SQL query to select all rows with a specific value in a column
        String sql = "SELECT * FROM files WHERE uploaded_by = ?"; // Replace with your table and column names
        String valueToMatch = userName; // Replace with the value you want to search for
        
        // JDBC objects
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<UploadedFile> uploadedFiles = new ArrayList<>();

        try {
            // Step 1: Establish the connection
            connection = DriverManager.getConnection(url, username, password);

            // Step 2: Create the PreparedStatement
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, valueToMatch); // Set the parameter in the query

            // Step 3: Execute the query
            resultSet = preparedStatement.executeQuery();

            // Step 4: Process the result
            while (resultSet.next()) {
                LocalDateTime uploadDate = resultSet.getObject("upload_date", LocalDateTime.class);
                UploadedFile uf = new UploadedFile(resultSet.getString("file_name"), uploadDate, resultSet.getString("link"));
                uploadedFiles.add(uf);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Step 5: Clean up
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        for (UploadedFile file : uploadedFiles) {
            JPanel filePanel = new JPanel(new BorderLayout());
            filePanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            JLabel fileNameLabel = new JLabel(file.getFileName());
            fileNameLabel.setForeground(Color.BLUE);
            fileNameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            fileNameLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    SwingUtilities.invokeLater(() -> {
                        dispose();
                        SketchApp app = new SketchApp();
                        app.setVisible(true);

                        // Add specific action logic here.
                    });
                }
            });

            JLabel fileDateLabel = new JLabel(file.getUploadDate().toString());
            fileDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            filePanel.add(fileNameLabel, BorderLayout.WEST);
            filePanel.add(fileDateLabel, BorderLayout.EAST);

            centerPanel.add(filePanel);
        }

        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void uploadFileToDropbox(String userName, String fileName, String accessMode, String customUsers) {
        try {
            if (dropboxClient == null) {
                JOptionPane.showMessageDialog(this, "Dropbox client is not initialized. Please try again.");
                return;
            }
            // Prepare File for Upload
            try (InputStream in = new FileInputStream(selectedFile)) {
                String dropboxPath = "/uploads/" + fileName;

                FileMetadata metadata = dropboxClient.files().uploadBuilder(dropboxPath)
                        .withMode(WriteMode.ADD)
                        .uploadAndFinish(in);

                String fileLink = dropboxClient.sharing().createSharedLinkWithSettings(dropboxPath).getUrl();

                System.out.println("File Uploaded Successfully: " + metadata.getPathDisplay());
                JOptionPane.showMessageDialog(this, "File uploaded successfully!\nFile URL: " + fileLink);

                // Handle Permissions (Custom Users Logic)
                if (accessMode.equals("Private") && !customUsers.isEmpty()) {
                    for (String user : customUsers.split(",")) {
                        System.out.println("Notify custom user: " + user.trim());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "File upload failed: " + e.getMessage());
        }

        String url = "jdbc:mysql://localhost:3306/floorplanner_db"; // Replace with your database URL
        String username = "root"; // Replace with your database username
        String password = "venusql2024"; // Replace with your database password
        
        // SQL query to insert a new row into the table
        String sql = "INSERT INTO files (file_name, uploaded_by) VALUES (?, ?)"; // Replace with your table and column names

        // Data to insert
        String value1 = fileName; // Replace with your data
        String value2 = userName; // Replace with your data
         // Example of LocalDateTime
        
        // JDBC objects
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            // Step 1: Establish the connection
            connection = DriverManager.getConnection(url, username, password);

            // Step 2: Create the PreparedStatement
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, value1); // Set the first parameter
            preparedStatement.setString(2, value2);    // Set the second parameter
             // Set the third parameter (LocalDateTime)

            // Step 3: Execute the query
            int rowsAffected = preparedStatement.executeUpdate();

            // Step 4: Check how many rows were affected
            System.out.println(rowsAffected + " row(s) inserted.");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Step 5: Clean up
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static class UploadedFile {
        private final String fileName;
        private final LocalDateTime uploadDate;
        private String filelink;
        

        public UploadedFile(String fileName, LocalDateTime uploadDate, String filelink) {
            this.fileName = fileName;
            this.uploadDate = uploadDate;
            this.filelink = filelink;
        }

        public String getFileName() {
            return fileName;
        }

        public LocalDateTime getUploadDate() {
            return uploadDate;
        }

        public String getFilelink(){
            return filelink;
        }
    }

}
