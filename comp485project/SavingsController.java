package com.example.comp485project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SavingsController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    @FXML
    private Text savedAmountText;  // Simple Text to display saved amount

    @FXML
    private TextField goalAmountField;  // Input for saving goal amount
    @FXML
    private TextField savedAmountField;  // Input for amount saved so far

    @FXML
    private Button toDashboard;
    @FXML
    private Button saveButton;  // Button to save new data

    @FXML
    public void initialize() {
        loadSavingsData();  // Load the existing savings data when the page initializes
    }

    // Switch back to the Dashboard
    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
    }

    // Load savings data from the database and display it
    private void loadSavingsData() {
        String username = LoginController.loggedInUsername;  // Get the logged-in username
        String fetchSavingsSQL = "SELECT goal_amount, saved_amount FROM savings WHERE username = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(fetchSavingsSQL)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                BigDecimal goalAmount = rs.getBigDecimal("goal_amount");
                BigDecimal savedAmount = rs.getBigDecimal("saved_amount");

                // Update the Text to show the saved amount
                savedAmountText.setText("Amount Saved: $" + savedAmount);
                // Prepopulate the text fields with existing data
                goalAmountField.setText(goalAmount != null ? goalAmount.toString() : "0.00");
                savedAmountField.setText(savedAmount != null ? savedAmount.toString() : "0.00");
            } else {
                savedAmountText.setText("No Savings Data Found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to load savings data.");
        }
    }

    // Save the new goal and saved amount to the database
    @FXML
    public void saveSavingsData(ActionEvent event) {
        String username = LoginController.loggedInUsername;
        String goalAmountStr = goalAmountField.getText();
        String savedAmountStr = savedAmountField.getText();

        // Validate and parse input
        try {
            BigDecimal goalAmount = new BigDecimal(goalAmountStr);
            BigDecimal savedAmount = new BigDecimal(savedAmountStr);

            // Save new data to the database
            String updateSavingsSQL = "UPDATE savings SET goal_amount = ?, saved_amount = ? WHERE username = ?";
            try (Connection conn = Database.connect();
                 PreparedStatement stmt = conn.prepareStatement(updateSavingsSQL)) {

                stmt.setBigDecimal(1, goalAmount);
                stmt.setBigDecimal(2, savedAmount);
                stmt.setString(3, username);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    savedAmountText.setText("Amount Saved: $" + savedAmount);  // Update the display with the new saved amount
                    showAlert("Savings data updated successfully.");
                } else {
                    showAlert("No rows updated. Check if the username exists.");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Failed to update savings data.");
            }

        } catch (NumberFormatException e) {
            showAlert("Invalid input. Please enter valid numbers for the goal and saved amounts.");
        }
    }

    // Method to show alerts if needed
    private void showAlert(String message) {
        // Implement your alert system here (e.g., using Alert or System.out.println)
        System.out.println(message);
    }
}
