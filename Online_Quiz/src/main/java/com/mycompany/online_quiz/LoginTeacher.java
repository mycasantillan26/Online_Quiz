/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
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
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginTeacher extends JFrame implements ActionListener {

    private JLabel usernameLabel, passwordLabel, messageLabel, titleLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel registerLabel;

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private String loggedInUsername;

    public LoginTeacher() {
        setSize(1650, 1850);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setTitle("Teacher's Registration");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setBounds(450, 100, 500, 500);
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

        JPanel innerPanel = new JPanel();
        innerPanel.setBounds(50, 100, 400, 200);
        innerPanel.setLayout(null);
        innerPanel.setBackground(new Color(147, 102, 57));

        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        registerLabel = new JLabel("Don't have an account? Click here");

        messageLabel = new JLabel();

        titleLabel = new JLabel("Teacher's LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40));
        titleLabel.setBounds(90, 0, 500, 150);

        usernameLabel.setBounds(130, 150, 100, 30);
        passwordLabel.setBounds(130, 200, 100, 30);

        usernameField.setBounds(220, 150, 150, 30);
        passwordField.setBounds(220, 200, 150, 30);

        loginButton.setBounds(200, 300, 100, 30);
        registerLabel.setBounds(160, 350, 200, 30);

        messageLabel.setBounds(50, 250, 250, 30);

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerLabel);
        panel.add(messageLabel);
        panel.add(titleLabel);

        loginButton.addActionListener(this);
        registerLabel.setForeground(Color.BLUE);
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    CreateAccountTeacher teacherAccount = new CreateAccountTeacher();
                    teacherAccount.setVisible(true);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showMessage("Error opening registration page.");
                }
            }
        });

        getContentPane().add(panel);
        getContentPane().setBackground(new Color(147, 102, 57));
        setVisible(true);

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\TechMacky\\Documents\\NetBeansProjects\\OOP2 FinalProj(Online Quiz)\\Online_Quiz\\onlinequiz.db");
        } catch (SQLException e) {
            e.printStackTrace();
            showMessage("Error connecting to the database.");
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            try {
                preparedStatement = connection.prepareStatement("SELECT * FROM tblTeacher WHERE username = ? AND password = ?");
                preparedStatement.setString(1, usernameField.getText());
                preparedStatement.setString(2, new String(passwordField.getPassword()));

                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Retrieve the username from the result set
                    loggedInUsername = resultSet.getString("username");

                    // Create an instance of TeacherDashboard by passing the username
                    TeacherDashboard teacher = new TeacherDashboard(loggedInUsername);
                    teacher.setVisible(true);
                    dispose();
                } else {
                    messageLabel.setText("Invalid username or password");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginTeacher loginTeacherPage = new LoginTeacher();
        });
    }
}
