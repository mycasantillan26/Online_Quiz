/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.online_quiz;



import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.ResultSet;
import javax.swing.AbstractButton;


public class CreateQuiz extends JFrame {

 private static Connection getDatabaseConnection() {
    Connection connection = null;

    try {
        String url = "jdbc:sqlite:C:\\Users\\TechMacky\\Documents\\NetBeansProjects\\OOP2 FinalProj(Online Quiz)\\Online_Quiz\\onlinequiz.db";
        connection = DriverManager.getConnection(url);
        System.out.println("Database connection established");
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return connection;
}


  private static String getLoggedInTeacherUsername() {
    // Replace this with your actual logic to get the logged-in teacher's username
    // For demonstration purposes, I'm returning a placeholder value.
    return "Teacher123";
}


    private JPanel buttonPanel;
    private JPanel questionOutputPanel;
    private int questionCounter = 1;
    private JPanel currentQuestionPanel; // Track the current question
    private Timer timer;
    private int hours;
    private int minutes;
    private int seconds;
    private JTextField hoursField;
    private JTextField minutesField;
    private JTextField secondsField;
    private JTextField titleField;
    private int timeNTitleId;
    private DocumentListener documentListener;
    private String teacherUsername;
    private JButton addOptionButton;
    private int currentQuestionTimeNTitleId;
    private Object questionType;
     private TeacherDashboard teacherDashboard;
    private Connection connection;
    private Object optionType;
    private OptionPanel choiceTextField;

    public CreateQuiz() {
        setTitle("Create Quiz");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1650, 1850);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 5),
                BorderFactory.createLineBorder(new Color(147, 102, 57), 5)
        ));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                panel.getBorder()
        ));

        JLabel titleLabel = new JLabel("Title/Subject: ");
        titleLabel.setSize(150, 50);
        titleLabel.setLocation(100, 50);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 20));
        panel.add(titleLabel);

        titleField = new JTextField("");
        titleField.setBounds(220, 55, 200, 30);
        panel.add(titleField);

        JButton addQuestionButton = new JButton("+ Add New");
        addQuestionButton.setBounds(100, 141, 150, 32);
        addQuestionButton.addActionListener(e -> handleAddQuestionButtonClick());
        panel.add(addQuestionButton);

        JLabel timeLimitLabel = new JLabel("Time Limit:");
        timeLimitLabel.setBounds(500, 50, 150, 30);
        panel.add(timeLimitLabel);

        hoursField = new JTextField("Hours");
        hoursField.setBounds(600, 50, 50, 30);
        panel.add(hoursField);

        minutesField = new JTextField("Min");
        minutesField.setBounds(670, 50, 50, 30);
        panel.add(minutesField);

        secondsField = new JTextField("Sec");
        secondsField.setBounds(740, 50, 50, 30);
        panel.add(secondsField);

        // Add components for Save and Cancel buttons
        JButton saveButton = new JButton("Save");
        saveButton.setBounds(450, 600, 150, 30);
        saveButton.addActionListener(e -> handleSaveButtonClick());
        panel.add(saveButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(700, 600, 150, 30);
        cancelButton.addActionListener(e -> handleCancelButtonClick());
        panel.add(cancelButton);

        // Initialize the button panel
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBounds(250, 140, 550, 35);
        panel.add(buttonPanel);

        // Initialize the question output panel
        questionOutputPanel = new JPanel();
        questionOutputPanel.setLayout(new BoxLayout(questionOutputPanel, BoxLayout.Y_AXIS));
        questionOutputPanel.setBounds(100, 230, 1100, 400);

        // Wrap the questionOutputPanel in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(questionOutputPanel);
        scrollPane.setBounds(100, 230, 1200, 350);
        panel.add(scrollPane);

        add(panel);
        setVisible(true);
    }

   CreateQuiz(Connection connection, String loggedInUsername, TeacherDashboard teacherDashboard) {
    // Existing constructor code
    this.teacherDashboard = teacherDashboard;
}

    

   private void handleSaveButtonClick() {
    SwingUtilities.invokeLater(() -> {
        // Move these lines inside the lambda
        String title = titleField.getText();
        int enteredHours = Integer.parseInt(hoursField.getText());
        int enteredMinutes = Integer.parseInt(minutesField.getText());
        int enteredSeconds = Integer.parseInt(secondsField.getText());
        int countQuestion = questionCounter - 1;

        // Perform actions related to saving data
        int savedTimeNTitleId = saveTimeNTitleToDatabase(title, enteredHours, enteredMinutes, enteredSeconds, countQuestion);

        // Display a message or perform actions after saving all questions
        System.out.println("Quiz details saved successfully!");

        // Open the TeacherDashboard here (assuming you want to open it)
        TeacherDashboard teacher = new TeacherDashboard(connection, teacherUsername);
        teacher.setVisible(true);

        timeNTitleId = savedTimeNTitleId; // Update the global variable with the saved ID

        // Close the CreateQuiz window
        dispose();
    });
}


  private void handleCancelButtonClick() {
    // Make the existing TeacherDashboard instance visible
    if (teacherDashboard != null) {
        teacherDashboard.setVisible(true);
    }

    // Dispose the current frame (CreateQuiz)
    dispose();
}


    private void startTimer(int hours, int minutes, int seconds) {
        // Set the timer to tick every second
        timer = new Timer(1000, e -> {
            // Update the time remaining
            this.seconds--;

            // Implement the logic for handling each second of the countdown
            // For example, update a JLabel to display the time remaining
            // When the timer reaches 0, perform the necessary actions (e.g., end the quiz)
            if (this.seconds == 0 && this.minutes == 0 && this.hours == 0) {
                timer.stop();
                // Perform actions when the time limit is reached
            }
        });

        // Set the initial time values
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;

        // Start the timer
        timer.start();
    }

    private void handleAddQuestionButtonClick() {
        addButtonToPanel("JRadioButton");
        addButtonToPanel("JTextField");
        addButtonToPanel("JCheckBox");

        revalidate();
        repaint();
    }

    private void addButtonToPanel(String buttonText) {
        JButton button = new JButton(buttonText);
        buttonPanel.add(button);
        button.addActionListener(e -> handleButtonClicked(buttonText));
    }

    private void handleButtonClicked(String buttonType) {
    if ("JRadioButton".equals(buttonType) || "JTextField".equals(buttonType) || "JCheckBox".equals(buttonType)) {
        // Create a panel for the current question
        currentQuestionPanel = new JPanel();
        currentQuestionPanel.setLayout(new BoxLayout(currentQuestionPanel, BoxLayout.Y_AXIS));

        // Add a label for question number
        JTextField questionOutput = createPlaceholderTextField("Question " + questionCounter + ": ");
        questionOutput.setEditable(false);
        currentQuestionPanel.add(questionOutput);

        // Add a text field for the question text
        JTextField questionTextField = createPlaceholderTextField("Type your question here!!!");
        currentQuestionPanel.add(questionTextField);

        // Add a button to add options
        JButton addOptionButton = new JButton("Add Option");
        addOptionButton.addActionListener(e -> handleAddOptionButtonClick(buttonType, questionTextField));
        currentQuestionPanel.add(addOptionButton);

        // Add a button to delete the question and its options
        JButton deleteQuestionButton = new JButton("Delete Question");
    deleteQuestionButton.addActionListener(e -> handleDeleteQuestionButtonClick());
    currentQuestionPanel.add(deleteQuestionButton);
        // Add the current question panel to the main output panel
        questionOutputPanel.add(currentQuestionPanel);

        // Increment the question counter
        questionCounter++;

        // Refresh the layout
        revalidate();
        repaint();
    } else if ("Delete".equals(buttonType)) {
        // If the "Delete" button is clicked, remove the last added question
        if (questionCounter > 1) {
            questionOutputPanel.remove(questionOutputPanel.getComponentCount() - 1);
            questionCounter--;

            // Refresh the layout
            revalidate();
            repaint();
        }
      } else if ("JTextField".equals(buttonType)) {
        // Handle JTextField without an associated button
        handleJTextFieldButtonClick();
    }
}
    
    private void handleJTextFieldButtonClick() {
    // Create a panel for the current question
    currentQuestionPanel = new JPanel();
    currentQuestionPanel.setLayout(new BoxLayout(currentQuestionPanel, BoxLayout.Y_AXIS));

    // Add a label for question number
    JTextField questionOutput = createPlaceholderTextField("Question " + questionCounter + ": ");
    questionOutput.setEditable(false);
    currentQuestionPanel.add(questionOutput);

    // Add a text field for the question text
    JTextField questionTextField = createPlaceholderTextField("Type your question here!!!");
    currentQuestionPanel.add(questionTextField);

    // Add the current question panel to the main output panel
    questionOutputPanel.add(currentQuestionPanel);

    // Increment the question counter
    questionCounter++;

    // Refresh the layout
    revalidate();
    repaint();
}
    private void handleDeleteQuestionButtonClick() {
    // If the "Delete Question" button is clicked, remove the current question
    if (currentQuestionPanel != null) {
        questionOutputPanel.remove(currentQuestionPanel);
        questionCounter--;

        // Refresh the layout
        revalidate();
        repaint();
    }
}


    private JTextField createPlaceholderTextField(String placeholder) {
        JTextField textField = new JTextField(placeholder);
        textField.setPreferredSize(new java.awt.Dimension(500, 30));
        textField.setForeground(Color.GRAY);

        // Add FocusListener to handle placeholder behavior
        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (textField.getText().equals(placeholder)) {
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (textField.getText().isEmpty()) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                }
            }
        });

        // Add DocumentListener to handle text disappearance
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                handleTextChange();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                handleTextChange();
            }

            private void handleTextChange() {
                String currentText = textField.getText().trim();
                if (currentText.isEmpty() || currentText.equals(placeholder)) {
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholder);
                } else {
                    textField.setForeground(Color.BLACK);
                }
            }
        });

        return textField;
    }

    private void ButtonClick(String optionType) {
        if (currentQuestionPanel != null) {
            // Add a new JRadioButton, JTextField, or JCheckBox for choices
            JPanel optionPanel = new JPanel();
            optionPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            JTextField choiceTextField = new JTextField("Type the answer here!");
            choiceTextField.setPreferredSize(new java.awt.Dimension(250, 30));
            optionPanel.add(choiceTextField);

            // ... (existing code)
            // Save question details to QuestionNAnswer table
            int questionId = saveQuestionNAnswerToDatabase(choiceTextField.getText(), "Multiple choices", timeNTitleId);

            // Save answer details to Answer table
            if ("JRadioButton".equals(optionType)) {
                JRadioButton choiceRadioButton = new JRadioButton();
                optionPanel.add(choiceRadioButton);
                String answerType = choiceRadioButton.isSelected() ? "Correct" : "Incorrect";
                saveAnswerToDatabase(choiceTextField.getText(), answerType, questionId);
            } else if ("JTextField".equals(optionType)) {
                // No need to add any extra components for text fields
                saveAnswerToDatabase(choiceTextField.getText(), "Correct", questionId);
            } else {  // "JCheckBox".equals(optionType)
                JCheckBox choiceCheckBox = new JCheckBox();
                optionPanel.add(choiceCheckBox);
                String answerType = choiceCheckBox.isSelected() ? "Correct" : "Incorrect";
                saveAnswerToDatabase(choiceTextField.getText(), answerType, questionId);
            }

            currentQuestionPanel.add(optionPanel);

            revalidate();
            repaint();
        }
    }

    private int saveQuestionNAnswerToDatabase(String questionText, String questionType, int timeNTitleId) {
    int questionId = -1;

    String url = "jdbc:sqlite:C:\\Users\\TechMacky\\Documents\\NetBeansProjects\\OOP2 FinalProj(Online Quiz)\\Online_Quiz\\onlinequiz.db";

    try (Connection connection = DriverManager.getConnection(url)) {
        String query = "INSERT INTO QuestionNAnswer (Question, QuestionType, timeNTitleId) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, questionText);
            preparedStatement.setString(2, questionType);
            preparedStatement.setInt(3, timeNTitleId);

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        questionId = generatedKeys.getInt(1);
                        System.out.println("Generated Question ID: " + questionId);
                    }
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return questionId;
}

    
     private int saveTimeNTitleToDatabase(String title, int hours, int minutes, int seconds, int countQuestion) {
    String url = "jdbc:sqlite:C:\\Users\\TechMacky\\Documents\\NetBeansProjects\\OOP2 FinalProj(Online Quiz)\\Online_Quiz\\onlinequiz.db";

    int existingRecordId = getExistingTimeNTitleId(title);

    try (Connection connection = DriverManager.getConnection(url)) {
        if (existingRecordId != -1) {
            // Update the existing record
            String updateQuery = "UPDATE TimeNTitle SET Hours=?, Minutes=?, Seconds=?, TeacherId=1, countQuestion=? WHERE id=?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, hours);
                updateStatement.setInt(2, minutes);
                updateStatement.setInt(3, seconds);
                updateStatement.setInt(4, countQuestion);
                updateStatement.setInt(5, existingRecordId);

                updateStatement.executeUpdate();
                System.out.println("Existing TimeNTitle record updated. ID: " + existingRecordId);
                return existingRecordId;
            }
        } else {
            // Insert a new record
            String insertQuery = "INSERT INTO TimeNTitle (Title, Hours, Minutes, Seconds, TeacherId, countQuestion) VALUES (?, ?, ?, ?, 1, ?)";
            connection.setAutoCommit(false);
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                insertStatement.setString(1, title);
                insertStatement.setInt(2, hours);
                insertStatement.setInt(3, minutes);
                insertStatement.setInt(4, seconds);
                insertStatement.setInt(5, countQuestion);

                int affectedRows = insertStatement.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = insertStatement.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int generatedId = generatedKeys.getInt(1);
                            System.out.println("Generated TimeNTitle ID: " + generatedId);
                            connection.commit();
                            return generatedId;
                        }
                    }
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return -1; // Return -1 if there is an error
}

// Helper method to check if a record with the given title already exists in the TimeNTitle table
private int getExistingTimeNTitleId(String title) {
    String url = "jdbc:sqlite:C:\\Users\\TechMacky\\Documents\\NetBeansProjects\\OOP2 FinalProj(Online Quiz)\\Online_Quiz\\onlinequiz.db";
    String query = "SELECT id FROM TimeNTitle WHERE Title=?";

    try (Connection connection = DriverManager.getConnection(url);
         PreparedStatement preparedStatement = connection.prepareStatement(query)) {
        preparedStatement.setString(1, title);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return -1; // Return -1 if no existing record is found
}


 private void saveAnswerToDatabase(String answer, String answerType, int questionId) {
    String url = "jdbc:sqlite:C:\\Users\\TechMacky\\Documents\\NetBeansProjects\\OOP2 FinalProj(Online Quiz)\\Online_Quiz\\onlinequiz.db";

    try (Connection connection = DriverManager.getConnection(url)) {
        String query = "INSERT INTO Answer (Answer, AnswerType, QuestionNAnswerId) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, answer);
            preparedStatement.setString(2, answerType);
            preparedStatement.setInt(3, questionId);

            preparedStatement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

 private void handleOptionSelection(JTextField choiceTextField, String optionType, boolean isSelected, int questionId) {
    String answerType;

    // Check the optionType to determine the answerType
    if ("JRadioButton".equals(optionType) || "JCheckBox".equals(optionType)) {
        answerType = isSelected ? "Correct" : "Incorrect";
    } else {
        // For JTextField, always set AnswerType to "Correct"
        answerType = "Correct";
    }

    // Save the answer details to the database
    saveAnswerToDatabase(choiceTextField.getText(), answerType, questionId);
}
 
   class OptionPanel extends JPanel {
    JTextField choiceTextField;
    AbstractButton optionButton;
    JButton deleteOptionButton;
    JButton addAnswerButton;
    int questionNAnswerId;

     public OptionPanel(JTextField choiceTextField, AbstractButton optionButton, int questionNAnswerId) {
        this.choiceTextField = choiceTextField;
        this.optionButton = optionButton;
        this.questionNAnswerId = questionNAnswerId;

        setLayout(new FlowLayout(FlowLayout.LEFT));

        // Add the delete button
        deleteOptionButton = new JButton("Delete");
        deleteOptionButton.addActionListener(e -> handleDeleteOptionButtonClick(this));
        add(deleteOptionButton);

        // Add the choiceTextField
        add(choiceTextField);

        // Add the optionButton (JRadioButton or JCheckBox)
        if (optionButton != null) {
            add(optionButton);
        }

        // Add the "Add Answer" button
        addAnswerButton = new JButton("Add Answer");
        addAnswerButton.addActionListener(e -> handleAddAnswerButtonClick(this));
        add(addAnswerButton);
    }
}
 private void handleAddAnswerButtonClick(OptionPanel optionPanel) {
    JTextField choiceTextField = optionPanel.choiceTextField;
    if (currentQuestionPanel != null && choiceTextField != null) {
        // Retrieve the QuestionNAnswerId from the OptionPanel
        int questionNAnswerId = optionPanel.questionNAnswerId;

        // Save the answer details to the database
        saveAnswerToDatabase(choiceTextField.getText(), "Correct", questionNAnswerId);

        // You can display the QuestionNAnswerId here if needed
        System.out.println("QuestionNAnswerId: " + questionNAnswerId);
    }
}



// Helper method to get the questionId associated with a question panel
// Helper method to get the questionId associated with a question panel
private int getQuestionIdFromPanel(JPanel questionPanel) {
    if (questionPanel != null) {
        // Assuming the questionId is stored as a property in the question panel
        Object questionIdObject = questionPanel.getClientProperty("questionId");

        if (questionIdObject instanceof Integer) {
            return (Integer) questionIdObject;
        }
    }

    return -1; // Return -1 if no valid questionId is found
}


private void handleAddOptionButtonClick(String optionType, JTextField questionTextField) {
    if (currentQuestionPanel != null) {
        // Save timeNTitleId only once
        int timeNTitleId = saveTimeNTitleToDatabase(titleField.getText(),
                Integer.parseInt(hoursField.getText()),
                Integer.parseInt(minutesField.getText()),
                Integer.parseInt(secondsField.getText()),
                questionCounter - 1);

        // Save question details to QuestionNAnswer table only once
        int questionId = saveQuestionNAnswerToDatabase(questionTextField.getText(),
                getQuestionType(optionType),
                timeNTitleId);

        // Specify the number of options to add (adjust as needed)
        int numberOfOptions = 4;

        for (int i = 0; i < numberOfOptions; i++) {
            // Create the choiceTextField inside the loop
            JTextField choiceTextField = new JTextField("Type the answer here!");
            choiceTextField.setPreferredSize(new java.awt.Dimension(250, 30));

            // Add a new JRadioButton, JTextField, or JCheckBox for choices
            AbstractButton optionButton = null;
            if ("JRadioButton".equals(optionType)) {
                JRadioButton choiceRadioButton = new JRadioButton();
                optionButton = choiceRadioButton;

                // Add an ActionListener to handle radio button selection
                choiceRadioButton.addActionListener(e -> handleOptionSelection(choiceTextField, optionType, choiceRadioButton.isSelected(), questionId));
            } else if ("JCheckBox".equals(optionType)) {
                JCheckBox choiceCheckBox = new JCheckBox();
                optionButton = choiceCheckBox;

                // Add an ItemListener to handle checkbox selection
                choiceCheckBox.addItemListener(e -> handleOptionSelection(choiceTextField, optionType, choiceCheckBox.isSelected(), questionId));
            }

            // Create the OptionPanel and add it to the currentQuestionPanel
             OptionPanel optionPanelItem = new OptionPanel(choiceTextField, optionButton, questionId);
            currentQuestionPanel.add(optionPanelItem);
        }

        // Refresh the layout
        revalidate();
        repaint();
    }
}



private void handleDeleteOptionButtonClick(OptionPanel optionPanel) {
    if (currentQuestionPanel != null && optionPanel != null) {
        // Remove the entire OptionPanel from the currentQuestionPanel
        currentQuestionPanel.remove(optionPanel);

        // Refresh the layout
        revalidate();
        repaint();
    }
}
private JPanel findOptionPanelContainingTextField(JPanel currentQuestionPanel, JTextField choiceTextField) {
    if (currentQuestionPanel != null && choiceTextField != null) {
        // Iterate over the components in currentQuestionPanel
        for (Component component : currentQuestionPanel.getComponents()) {
            if (component instanceof JPanel) {
                JPanel optionPanel = (JPanel) component;

                // Iterate over the components in the optionPanel
                for (Component optionComponent : optionPanel.getComponents()) {
                    if (optionComponent.equals(choiceTextField)) {
                        // Found the optionPanel containing the choiceTextField
                        return optionPanel;
                    }
                }
            }
        }
    }

    return null; // Return null if not found
}

    private void initComponents() {
        // Add your components and layout for creating a quiz here
    }

// Helper method to get the question type based on the buttonType
private String getQuestionType(String buttonType) {
    if ("JRadioButton".equals(buttonType)) {
        return "Multiple Choices";
    } else if ("JTextField".equals(buttonType)) {
        return "Identification";
    } else if ("JCheckBox".equals(buttonType)) {
        return "Checkbox";
    }
    return ""; // Default to empty string if the type is not recognized
}

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        Connection connection = getDatabaseConnection();
        String teacherUsername = getLoggedInTeacherUsername();

        TeacherDashboard teacher = new TeacherDashboard(connection, teacherUsername);
        new CreateQuiz(connection, teacherUsername, teacher);
    });
}

}
