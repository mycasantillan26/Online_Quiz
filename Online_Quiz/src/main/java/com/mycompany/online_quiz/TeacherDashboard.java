/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.online_quiz;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import javax.swing.border.Border;

public class TeacherDashboard extends JFrame {

    private final Connection connection;
    private String loggedInUsername;
    private JPanel quizPanel; // Reference to the quiz panel
    private JButton logOutButton; // Added logout button
     private JButton createButton;

   public TeacherDashboard(Connection connection, String username) {
    this.connection = connection;  // Make sure this line is present
    this.loggedInUsername = username;
    initComponents();

    setTitle("Teacher Dashboard");

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setExtendedState(JFrame.MAXIMIZED_BOTH); // Set the frame to fullscreen

    setLocationRelativeTo(null);
    setVisible(true);
}


    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUsername);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        quizPanel = new JPanel(); // No need for GridLayout
        quizPanel.setBackground(Color.decode("#9AD0C2")); // Set background color

        add(welcomeLabel, BorderLayout.NORTH);
        add(quizPanel, BorderLayout.CENTER);

        // Retrieve data from the database and populate quiz panels
        retrieveAndPopulateQuizzes();

        // Add logout button
        logOutButton = new JButton("Log Out");
        logOutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginRegistration1 loginRegistrationSystem1 = new LoginRegistration1();
                loginRegistrationSystem1.setVisible(true);
            }
        });

        add(logOutButton, BorderLayout.SOUTH);
        
        
        createButton = new JButton("Create");
        createButton.addActionListener(e -> openCreateQuizWindow());

        add(createButton, BorderLayout.WEST); 
    }
    
     private void openCreateQuizWindow() {
        SwingUtilities.invokeLater(() -> new CreateQuiz());
    }

  private void retrieveAndPopulateQuizzes() {
    try {
        String query = "SELECT * FROM TimeNTitle";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String title = resultSet.getString("Title");
                int hours = resultSet.getInt("Hours");
                int minutes = resultSet.getInt("Minutes");
                int seconds = resultSet.getInt("Seconds");
                int quizId = resultSet.getInt("id");
                int score = 0; // You can retrieve the actual score if needed
                int questionCount = resultSet.getInt("countQuestion"); // Retrieve countQuestion

                addQuizPanel(title, hours, minutes, seconds, quizId, score, questionCount);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the exception as needed
    }
}
private void addQuizPanel(String title, int hours, int minutes, int seconds, int quizId, int score, int questionCount) {
    JPanel quizItemPanel = new JPanel(new BorderLayout());
    quizItemPanel.setBackground(Color.decode("#9AD0C2")); // Set background color
    quizItemPanel.setPreferredSize(new Dimension(250, 150));

    // Set the border with corner radius
    Border border = BorderFactory.createLineBorder(Color.decode("#001B79"), 2, true);
    quizItemPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Arial", Font.PLAIN, 30)); // Set font size

    JLabel timeLabel = new JLabel("Time: " + hours + " hour " + minutes + " min " + seconds + " sec");
    timeLabel.setFont(new Font("Arial", Font.PLAIN, 15)); // Set font size

    JLabel numItem = new JLabel("Number of Items: " + questionCount);
    numItem.setFont(new Font("Arial", Font.PLAIN, 15)); // Set font size

    JButton editButton = new JButton("Edit");
    editButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Assuming quizId is available in the scope where this ActionListener is defined.
            openEditQNA(quizId);
        }
    });
    

    JButton deleteButton = new JButton("Delete");
    deleteButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Assuming quizId is available in the scope where this ActionListener is defined.
            deleteQuiz(quizId);
        }
    });

    JPanel innerPanel = new JPanel(new GridLayout(4, 1)); // Increase grid rows to 4
    innerPanel.add(titleLabel);
    innerPanel.add(timeLabel);
    innerPanel.add(numItem);

    quizItemPanel.add(innerPanel, BorderLayout.CENTER);

    // Create a panel for south buttons
    JPanel southButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    southButtonPanel.add(editButton);
    southButtonPanel.add(deleteButton);

    quizItemPanel.add(southButtonPanel, BorderLayout.SOUTH);

    // Set the border for the inner panel
    innerPanel.setBorder(BorderFactory.createLineBorder(Color.decode("#001B79"), 2, true));

    quizPanel.add(quizItemPanel);
}

 private void openEditQNA(int quizId) {
    System.out.println("Opening EditQNA for quizId: " + quizId);
    SwingUtilities.invokeLater(() -> {
        EditQNA editQNA = new EditQNA(connection, quizId);
        editQNA.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                // This code will be executed when the EditQNA window is closed
                refreshQuizPanel();
            }
        });
        editQNA.setVisible(true);
    });
}




// Method to delete a quiz
private void deleteQuiz(int quizId) {
    try {
        // Delete the corresponding record from the TimeNTitle table
        String deleteQuery = "DELETE FROM TimeNTitle WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, quizId);
            preparedStatement.executeUpdate();
        }

        // Refresh the quiz panel after deleting
        refreshQuizPanel();
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle the exception as needed
    }
}
// Method to refresh the quiz panel after editing or deleting
private void refreshQuizPanel() {
    quizPanel.removeAll();
    retrieveAndPopulateQuizzes();
    revalidate();
    repaint();
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Connection connection = getDatabaseConnection();
            String teacherUsername = getLoggedInTeacherUsername();

            TeacherDashboard teacher = new TeacherDashboard(connection, teacherUsername);
        });
    }

    private static String getLoggedInTeacherUsername() {
        return "Teacher123"; // Replace with your logic to get the username
    }

    private static Connection getDatabaseConnection() {
        try {
            String url = "jdbc:sqlite:C:\\Users\\TechMacky\\Documents\\NetBeansProjects\\OOP2 FinalProj(Online Quiz)\\Online_Quiz\\onlinequiz.db";
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
