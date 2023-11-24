/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.online_quiz;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author TechMacky
 */
class LoginRegistration1  extends JFrame implements ActionListener {

    private JLabel usernameLabel, passwordLabel, messageLabel, titleLabel;
    private JTextField usernameField, passwordField;
    private JButton loginButton;
    private JLabel registerLabel; // Updated

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    public LoginRegistration1() {
        setBounds(0, 0, 3000, 700);
        setTitle("Login and Register");
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
        registerLabel = new JLabel("Don't have an account? Click here"); // Updated
        

        messageLabel = new JLabel();

        titleLabel = new JLabel("USER LOGIN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 50));
        titleLabel.setBounds(90, 0, 500, 150);

        usernameLabel.setBounds(130, 150, 100, 30);
        passwordLabel.setBounds(130, 200, 100, 30);

        usernameField.setBounds(220, 150, 150, 30);
        passwordField.setBounds(220, 200, 150, 30);

        loginButton.setBounds(200, 300, 100, 30);
        registerLabel.setBounds(160, 350, 200, 30); // Updated

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
        registerLabel.setForeground(Color.BLUE); // Set the color of the label to blue
        registerLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)); // Set the cursor to hand when hovering
        registerLabel.addMouseListener(new MouseAdapter() {
           /* @Override
            public void mouseClicked(MouseEvent e) {
                // Perform the desired action for registration
                // For example, open the registration page or show a registration dialog
                JOptionPane.showMessageDialog(LoginRegistration1.this,
                        "Registration page will open here.", "Registration",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
*/
          
    @Override
    public void mouseClicked(MouseEvent e) {
        // Open the registration page or perform any desired action
        Online_Quiz accountCreationGUI = new Online_Quiz();
        accountCreationGUI.setVisible(true);
        dispose(); // Close the current login page
    }
});
        getContentPane().add(panel);
        getContentPane().setBackground(new Color(147, 102, 57));
        setVisible(true);

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:C:\\Users\\TechMacky\\Documents\\NetBeansProjects\\OOP2 Final Project\\dbinventorymanagementsystem.db");
        } catch(SQLException e) {
e.printStackTrace();
}
}
   /* public void actionPerformed(ActionEvent e) {
    if (e.getSource() == loginButton) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            preparedStatement.setString(1, usernameField.getText());
            preparedStatement.setString(2, passwordField.getText());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                messageLabel.setText("Login successful");
            } else {
                messageLabel.setText("Invalid username or password");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
} */
    public void actionPerformed(ActionEvent e
    if (e.getSource() == loginButton) {
        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM tblUser WHERE username = ? AND password = ?");
            preparedStatement.setString(1, usernameField.getText());
            preparedStatement.setString(2, passwordField.getText());

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Login successful, open the InventoryManagementSystem
                InventoryManagementSystem inventoryManagementSystem = new InventoryManagementSystem();
                inventoryManagementSystem.setVisible(true);
                dispose(); // Close the current login page
            } else {
                messageLabel.setText("Invalid username or password");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}


public static void main(String[] args) {
    LoginRegistration1 loginRegistrationSystem1 = new LoginRegistration1();
}
}
