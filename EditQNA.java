/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.online_quiz;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class EditQNA extends JFrame {

    private final Connection connection;
    private final int quizId;
    private JTextField titleField;
    private JTextField hoursField;
    private JTextField minutesField;
    private JTextField secondsField;
    
    private JTextField questionTextField;  
    private JFrame questionFrame;
    private JFrame answerFrame;

    public EditQNA(Connection connection, int quizId) {
        this.connection = connection;
        this.quizId = quizId;

        initComponents();
        populateData();

        setTitle("Edit Question and Answer");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        inputPanel.add(titleField);

        inputPanel.add(new JLabel("Hours:"));
        hoursField = new JTextField();
        inputPanel.add(hoursField);

        inputPanel.add(new JLabel("Minutes:"));
        minutesField = new JTextField();
        inputPanel.add(minutesField);

        inputPanel.add(new JLabel("Seconds:"));
        secondsField = new JTextField();
        inputPanel.add(secondsField);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> dispose());

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(e -> performManualUpdate());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(cancelButton);
        buttonPanel.add(updateButton);
        
        JButton updateNEditQNAButton = new JButton("Update and Edit QNA");
        updateNEditQNAButton.addActionListener(e -> updateAndOpenQNAWindow());
        buttonPanel.add(updateNEditQNAButton);

        add(buttonPanel, BorderLayout.SOUTH);
        add(inputPanel, BorderLayout.CENTER);
    }
    
  
   private void updateAndOpenQNAWindow() {
    // Perform the update in the TimeNTitle table
    String editedTitle = titleField.getText();
    int editedHours = Integer.parseInt(hoursField.getText());
    int editedMinutes = Integer.parseInt(minutesField.getText());
    int editedSeconds = Integer.parseInt(secondsField.getText());

    try {
        String updateQueryTimeNTitle = "UPDATE TimeNTitle SET Title = ?, Hours = ?, Minutes = ?, Seconds = ? WHERE id = ?";
        try (PreparedStatement preparedStatementTimeNTitle = connection.prepareStatement(updateQueryTimeNTitle)) {
            preparedStatementTimeNTitle.setString(1, editedTitle);
            preparedStatementTimeNTitle.setInt(2, editedHours);
            preparedStatementTimeNTitle.setInt(3, editedMinutes);
            preparedStatementTimeNTitle.setInt(4, editedSeconds);
            preparedStatementTimeNTitle.setInt(5, quizId);
            preparedStatementTimeNTitle.executeUpdate();
        }

        // Close the EditQNA frame
        dispose();

        // Open the EditQuestionsAndAnswers window
        SwingUtilities.invokeLater(() -> new EditQuestionsAndAnswers(connection, quizId));
    } catch (SQLException ex) {
        ex.printStackTrace();
        // Handle the exception as needed
    }
}

   
    private void populateData() {
        try {
            // Retrieve existing data from the TimeNTitle table and populate the UI fields
            String selectQueryTimeNTitle = "SELECT * FROM TimeNTitle WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQueryTimeNTitle)) {
                preparedStatement.setInt(1, quizId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        titleField.setText(resultSet.getString("Title"));
                        hoursField.setText(String.valueOf(resultSet.getInt("Hours")));
                        minutesField.setText(String.valueOf(resultSet.getInt("Minutes")));
                        secondsField.setText(String.valueOf(resultSet.getInt("Seconds")));
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as needed
        }
    }

    private void performManualUpdate() {
        String editedTitle = titleField.getText();
        int editedHours = Integer.parseInt(hoursField.getText());
        int editedMinutes = Integer.parseInt(minutesField.getText());
        int editedSeconds = Integer.parseInt(secondsField.getText());

        try {
            // Perform the update in the TimeNTitle table
            String updateQueryTimeNTitle = "UPDATE TimeNTitle SET Title = ?, Hours = ?, Minutes = ?, Seconds = ? WHERE id = ?";
            try (PreparedStatement preparedStatementTimeNTitle = connection.prepareStatement(updateQueryTimeNTitle)) {
                preparedStatementTimeNTitle.setString(1, editedTitle);
                preparedStatementTimeNTitle.setInt(2, editedHours);
                preparedStatementTimeNTitle.setInt(3, editedMinutes);
                preparedStatementTimeNTitle.setInt(4, editedSeconds);
                preparedStatementTimeNTitle.setInt(5, quizId);
                preparedStatementTimeNTitle.executeUpdate();
            }

            // Close the frame after updating
            dispose();
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as needed
        }
    }

    public static void main(String[] args) {
        // Replace with your logic to get the database connection and quizId
        Connection connection = getDatabaseConnection();
        int quizId = 1; // Replace with the actual quizId

        SwingUtilities.invokeLater(() -> new EditQNA(connection, quizId));
    }

    private static Connection getDatabaseConnection() {
        // Replace with your logic to get the database connection
        return null;
    }
}
