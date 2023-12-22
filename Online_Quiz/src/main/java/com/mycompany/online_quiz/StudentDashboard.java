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
import java.sql.ResultSet;


public class StudentDashboard extends JFrame {

    private final Connection connection;
    private String loggedInUsername;
    private JPanel quizPanel; // Reference to the quiz panel
    private JButton logOutButton; // Added logout button

    public StudentDashboard(Connection connection, String username) {
        this.connection = connection;
        this.loggedInUsername = username;
        initComponents();

        setTitle("Quizzes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
         setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel("Welcome,Student " + loggedInUsername);
        welcomeLabel.setHorizontalAlignment(JLabel.CENTER);

        quizPanel = new JPanel(); // No need for GridLayout
        quizPanel.setBackground(Color.decode("#9AD0C2")); // Set background color

        add(welcomeLabel, BorderLayout.NORTH);
        add(welcomeLabel, BorderLayout.SOUTH);
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

                    addQuizPanel(title, hours, minutes, seconds, quizId, score);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }

    private void addQuizPanel(String title, int hours, int minutes, int seconds, int quizId, int score) {
        JPanel quizItemPanel = new JPanel(new BorderLayout());
        quizItemPanel.setBackground(Color.decode("#9AD0C2"));
       quizItemPanel.setPreferredSize(new Dimension(250, 150));// Set background color

        JLabel titleLabel = new JLabel("Quiz Title: " + title);
        JLabel timeLabel = new JLabel("Time: " + hours + " hours, " + minutes + " minutes, " + seconds + " seconds");
        JLabel scoreLabel = new JLabel("Your Score: " + score);

        JButton attemptButton = new JButton("Attempt Quiz");
        attemptButton.addActionListener(e -> attemptQuiz(quizId));

        JPanel innerPanel = new JPanel(new GridLayout(3, 1));
        innerPanel.add(titleLabel);
        innerPanel.add(timeLabel);
        innerPanel.add(scoreLabel);

        quizItemPanel.add(innerPanel, BorderLayout.CENTER);
        quizItemPanel.add(attemptButton, BorderLayout.SOUTH);

        quizItemPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        quizPanel.add(quizItemPanel);
    }

    private void attemptQuiz(int quizId) {
    System.out.println("Attempting Quiz: " + quizId);
    
    // Open the AttemptQuiz window with the selected quizId
    new AttemptQuiz(connection, quizId);
}

    private int getLoggedInStudentId() {
        return 456;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Connection connection = getDatabaseConnection();
            String studentUsername = getLoggedInStudentUsername();

            System.out.println("Connection: " + connection);
            System.out.println("Student Username: " + studentUsername);

            new StudentDashboard(connection, studentUsername);
        });
    }

    private static String getLoggedInStudentUsername() {
        return "Student123";
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

    private static int getLoggedInTeacherId() {
        return 123;
    }
}
