/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.online_quiz;

/**
 *
 * @author TechMacky
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AttemptQuiz extends JFrame {


    private final Connection connection;
    private final int quizId;
    private JPanel qaPanel; // Declare qaPanel as a class-level field
    private Timer timer;
    private int timeRemaining;
    private JLabel timerLabel;
     private int totalQuizTimeInSeconds;


     public AttemptQuiz(Connection connection, int quizId) {
    this.connection = connection;
    this.quizId = quizId;

    initComponents();
    fetchQuizTime();
    initializeTimer();
    populateData();  // Call populateData to display questions and answer types

    setTitle("Taking a Quiz");
    setSize(400, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setVisible(true);
}


     private void initComponents() {
        setLayout(new BorderLayout());

        qaPanel = new JPanel(); // Initialize qaPanel
        qaPanel.setLayout(new BoxLayout(qaPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(qaPanel);
        add(scrollPane, BorderLayout.CENTER);

         timerLabel = new JLabel("Timer: ");
        add(timerLabel, BorderLayout.NORTH);

        JButton finishButton = new JButton("Finish Attempt");
        finishButton.addActionListener(e -> finishAttempt());
        add(finishButton, BorderLayout.SOUTH);

        initializeTimer();
    }
  private void finishAttempt() {
        timer.cancel();
        JOptionPane.showMessageDialog(this, "Quiz completed!");
        // Add logic for what happens when the quiz is completed
        dispose();
    }
private int getTotalTimeForQuiz() {
    // Implement logic to retrieve the total time for the quiz
    return 300; // Example: 300 seconds (5 minutes)
}


  

  private void initializeTimer() {
        timeRemaining = totalQuizTimeInSeconds; // Set the initial time remaining
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeRemaining > 0) {
                    timeRemaining--;
                    updateTimerLabel();
                } else {
                    timer.cancel();
                    JOptionPane.showMessageDialog(AttemptQuiz.this, "Time's up!");
                    finishAttempt();
                }
            }
        }, 1000, 1000);
    }   
private void fetchQuizTime() {
        try {
            String selectQuery = "SELECT Hours, Minutes, Seconds FROM TimeNTitle WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, quizId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int hours = resultSet.getInt("Hours");
                        int minutes = resultSet.getInt("Minutes");
                        int seconds = resultSet.getInt("Seconds");

                        // Convert hours, minutes, and seconds to total seconds
                        totalQuizTimeInSeconds = hours * 3600 + minutes * 60 + seconds;
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as needed
        }
    }
    
      private void updateTimerLabel() {
        SwingUtilities.invokeLater(() -> timerLabel.setText("Timer: " + formatTime(timeRemaining)));
    }

// Add this method to your class
  private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    private void populateData() {
    try {
        // Retrieve questions and answers from the QuestionNAnswer and Answer tables
        String selectQuery = "SELECT * FROM QuestionNAnswer WHERE timeNTitleId = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, quizId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int questionNumber = 1;
                while (resultSet.next()) {
                    // Use a String for the question text
                    String questionText = resultSet.getString("Question");
                    List<String> answers = getAnswersForQuestion(resultSet.getInt("id"));

                    displayQuestionAndAnswers(questionNumber, questionText, answers);
                    questionNumber++;
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        // Handle the exception as needed
    }
}


    private List<String> getAnswersForQuestion(int questionId) {
        List<String> answers = new ArrayList<>();
        try {
            String selectQuery = "SELECT * FROM Answer WHERE QuestionNAnswerId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                preparedStatement.setInt(1, questionId);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String answerType = resultSet.getString("AnswerType");
                        String answer = resultSet.getString("Answer");
                        answers.add(answerType + ": " + answer);
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            // Handle the exception as needed
        }
        return answers;
    }

    private void displayQuestionAndAnswers(int questionNumber, String questionText, List<String> answers) {
    JPanel questionPanel = new JPanel();
    questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));

    JLabel questionNumberLabel = new JLabel("Question #" + questionNumber + ": ");
    JLabel answerTypeLabel = new JLabel("AnswerType: ");

    // Use JLabels for non-editable questions
    JLabel questionLabel = new JLabel(questionText);

    // Use JLabels for non-editable answers
    List<JLabel> answerLabels = new ArrayList<>();
    for (String answer : answers) {
        JLabel answerLabel = new JLabel(answer);
        answerLabels.add(answerLabel);
    }

    // Add components to the panel
    questionPanel.add(questionNumberLabel);
    questionPanel.add(questionLabel);

    questionPanel.add(answerTypeLabel);
    for (JLabel answerLabel : answerLabels) {
        questionPanel.add(answerLabel);
    }

    questionPanel.add(Box.createVerticalStrut(10));

    qaPanel.add(questionPanel);
    qaPanel.revalidate();
    qaPanel.repaint();
}


 private void updateData() {
    try {
        connection.setAutoCommit(false); // Set auto-commit to false

        // Iterate over the question panels in qaPanel
        Component[] components = qaPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel) {
                JPanel questionPanel = (JPanel) component;

                // Retrieve components from the questionPanel
                Component[] questionComponents = questionPanel.getComponents();
                JTextField questionTextField = null;
                List<JTextField> answerTextFields = new ArrayList<>();
                String answerType = null;

                for (Component questionComponent : questionComponents) {
                    if (questionComponent instanceof JTextField) {
                        questionTextField = (JTextField) questionComponent;
                    } else if (questionComponent instanceof JLabel && ((JLabel) questionComponent).getText().startsWith("AnswerType: ")) {
                        answerType = ((JLabel) questionComponent).getText().replace("AnswerType: ", "");
                    } else if (questionComponent instanceof JTextField) {
                        answerTextFields.add((JTextField) questionComponent);
                    }
                }

                // Retrieve the question number from the JLabel text
                String questionNumberText = ((JLabel) questionComponents[0]).getText();
                int questionNumber = Integer.parseInt(questionNumberText.replaceAll("\\D+", ""));

                // Retrieve the question and answers from the JTextFields
                String updatedQuestion = questionTextField.getText();
                List<String> updatedAnswers = new ArrayList<>();
                for (JTextField answerTextField : answerTextFields) {
                    updatedAnswers.add(answerTextField.getText());
                }

                // Update the QuestionNAnswer table
                String updateQuestionQuery = "UPDATE QuestionNAnswer SET Question = ? WHERE timeNTitleId = ? AND Question = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuestionQuery)) {
                    preparedStatement.setString(1, updatedQuestion);
                    preparedStatement.setInt(2, quizId);
                    preparedStatement.setString(3, getOldQuestion(questionNumber)); // Use a method to get the actual old question
                    preparedStatement.executeUpdate();
                }

                // Update the Answer table
               // Update the Answer table
String updateAnswerQuery = "UPDATE Answer SET Answer = ? WHERE QuestionNAnswerId = ? AND AnswerType = ? AND Answer = ?";
try (PreparedStatement preparedStatement = connection.prepareStatement(updateAnswerQuery)) {
    for (String updatedAnswer : updatedAnswers) {
        preparedStatement.setString(1, updatedAnswer);
        preparedStatement.setInt(2, questionNumber);
        preparedStatement.setString(3, answerType);
        preparedStatement.setString(4, getOldAnswer(questionNumber, answerType)); // Provide the missing parameter
        preparedStatement.executeUpdate();
    }
}

            }
        }

        connection.commit();
        JOptionPane.showMessageDialog(this, "Successfully updated the Question and Answer", "Success", JOptionPane.INFORMATION_MESSAGE);
    } catch (SQLException ex) {
        ex.printStackTrace();
        try {
            if (connection != null) {
                connection.rollback();
            }
            JOptionPane.showMessageDialog(this, "Error updating data. Changes rolled back.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

private String getOldQuestion(int questionNumber) {
    try {
        String selectQuery = "SELECT Question FROM QuestionNAnswer WHERE timeNTitleId = ? AND id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, quizId);
            preparedStatement.setInt(2, questionNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Question");
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        // Handle the exception as needed
    }
    return null;
}

private String getOldAnswer(int questionNumber, String answerType) {
    try {
        String selectQuery = "SELECT Answer FROM Answer WHERE QuestionNAnswerId = ? AND AnswerType = ? AND id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, questionNumber);
            preparedStatement.setString(2, answerType);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Answer");
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        // Handle the exception as needed
    }
    return null;
}



    public static void main(String[] args) {
        // Replace with your logic to get the database connection and quizId
        Connection connection = getDatabaseConnection();
        int quizId = 1; // Replace with the actual quizId

        SwingUtilities.invokeLater(() -> new EditQuestionsAndAnswers(connection, quizId));
    }

    private static Connection getDatabaseConnection() {
        // Replace with your logic to get the database connection
        return;
    }
}
