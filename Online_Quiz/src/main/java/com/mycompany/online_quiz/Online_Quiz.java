/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.online_quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Online_Quiz extends JFrame {
    private JLabel usernameLabel, emailLabel, passwordLabel, confirmLabel;
    private JTextField usernameField, emailField;
    private JPasswordField passwordField, confirmField;
    private JButton createAccountButton;
    private JLabel loginLabel;
    private Connection connection;

    public Online_Quiz() {
        setTitle("Create an Account");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 3000, 700);
        getContentPane().setLayout(null);

        // Initialize the database connection
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\TechMacky\\Documents\\NetBeansProjects\\OOP2 FinalProj(Online Quiz)\\dbinventorymanagementsystem.db", "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }

       
         // Main panel
        JPanel panel = new JPanel();
        panel.setBounds(450, 100, 500, 500);
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 5),
                BorderFactory.createLineBorder(new Color(220, 215, 210), 5)
        ));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20),
                panel.getBorder()
        ));

        // Username
        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(120, 190, 80, 25);
        panel.add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(220, 190, 150, 25);
        panel.add(usernameField);

        // Email
        emailLabel = new JLabel("Email:");
        emailLabel.setBounds(120, 220, 80, 25);
        panel.add(emailLabel);

        emailField = new JTextField();
        emailField.setBounds(220, 220, 150, 25);
        panel.add(emailField);

        // Password
        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(120, 250, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(220, 250, 150, 25);
        panel.add(passwordField);

        // Confirm Password
        confirmLabel = new JLabel("Confirm:");
        confirmLabel.setBounds(120, 280, 80, 25);
        panel.add(confirmLabel);

        confirmField = new JPasswordField();
        confirmField.setBounds(220, 280, 150, 25);
        panel.add(confirmField);

        JLabel titleLabel = new JLabel("CREATE AN ACCOUNT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 37));
        titleLabel.setBounds(45, 20, 500, 150);
        panel.add(titleLabel);

        // Create Account Button
        createAccountButton = new JButton("Create an Account");
        createAccountButton.setBounds(175, 330, 150, 30);
        panel.add(createAccountButton);



        createAccountButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                String confirm = new String(confirmField.getPassword());

                if (password.equals(confirm)) {
                    // Perform database operations here
                    try {
                        // Create a prepared statement with the SQL query
                        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO tblUser (username, email, password) VALUES (?, ?, ?)");
                        preparedStatement.setString(1, username);
                        preparedStatement.setString(2, email);
                        preparedStatement.setString(3, password);

                        // Execute the query
                        int rowsInserted = preparedStatement.executeUpdate();

                        if (rowsInserted > 0) {
                            JOptionPane.showMessageDialog(Online_Quiz.this,
                                    "Successfully created an account!",
                                    "Success",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(Online_Quiz.this,
                                "Failed to create an account. An error occurred.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Account creation unsuccessful
                    JOptionPane.showMessageDialog(Online_Quiz.this,
                            "Failed to create an account. Passwords do not match.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

          // Already have an account? Log in label
        loginLabel = new JLabel("Already have an account? Log in");
        loginLabel.setBounds(160, 370, 200, 25);
        loginLabel.setForeground(Color.BLUE);
        loginLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Open the login page or perform any desired action
                LoginRegistration1 loginPage = new LoginRegistration1();
                loginPage.setVisible(true);
                dispose(); // Close the current account creation page
            }
        });
        panel.add(loginLabel);

        // Add panel to the content pane
        getContentPane().add(panel);
        getContentPane().setBackground(new Color(220, 215, 210));
        setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Online_Quiz quiz = new Online_Quiz();
                quiz.setVisible(true);
            }
        });
    }
}
