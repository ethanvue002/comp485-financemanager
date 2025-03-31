package com.example.comp485project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import javafx.scene.text.Text;
public class IncomeController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root;


    @FXML
    private Button toDashboard;

    @FXML
    private Text incomeSourceDisplay; // Display income source (e.g., Job, Side business)
    @FXML
    private Text incomeSalaryDisplay; // Display income salary
    @FXML
    private Text incomePayFrequencyDisplay;

    @FXML
    private TextField sourceField;

    @FXML
    private TextField salaryField;

    @FXML
    private TextField payFrequencyField;

    @FXML
    private Button saveIncomeButton;

    public void displayIncomeData() {
        String username = LoginController.loggedInUsername;
        String fetchIncomeSQL = "SELECT * FROM income WHERE username = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(fetchIncomeSQL)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            // Check if data exists for this user
            if (rs.next()) {
                String source = rs.getString("source");
                String salary = rs.getString("salary");
                String payFrequency = rs.getString("pay_frequency");

                // Display the fetched data in the UI
                incomeSourceDisplay.setText(source);
                incomeSalaryDisplay.setText(salary);
                incomePayFrequencyDisplay.setText(payFrequency);
            } else {
                // If no income data found for the user, display a message
                incomeSourceDisplay.setText("No data available.");
                incomeSalaryDisplay.setText("No data available.");
                incomePayFrequencyDisplay.setText("No data available.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to fetch income data.");
        }
    }

    @FXML
    public void saveIncome(ActionEvent event) {
        String username = LoginController.loggedInUsername;
        String source = sourceField.getText();
        String salary = salaryField.getText();
        String payFrequency = payFrequencyField.getText();

        if (source.isEmpty() || salary.isEmpty() || payFrequency.isEmpty()) {
            showAlert("Please fill in all income details.");
            return;
        }

        // Check if income data already exists for the user
        if (incomeExists(username)) {
            // If income exists, update it
            updateIncome(username, source, salary, payFrequency);
        } else {
            // If no income data exists, insert a new record
            insertIncome(username, source, salary, payFrequency);
        }
    }

    private boolean incomeExists(String username) {
        String checkIncomeSQL = "SELECT * FROM income WHERE username = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(checkIncomeSQL)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();  // If a row is returned, income data exists
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void insertIncome(String username, String source, String salary, String payFrequency) {
        String insertIncomeSQL = "INSERT INTO income (username, source, salary, pay_frequency) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement insertStmt = conn.prepareStatement(insertIncomeSQL)) {

            insertStmt.setString(1, username);
            insertStmt.setString(2, source);
            insertStmt.setBigDecimal(3, new BigDecimal(salary));  // Store salary as BigDecimal
            insertStmt.setString(4, payFrequency);

            int rowsAffected = insertStmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Income data added successfully!");
                displayIncomeData();  // Refresh the displayed data
            } else {
                showAlert("Failed to add income data.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to insert income data.");
        }
    }

    private void updateIncome(String username, String source, String salary, String payFrequency) {
        String updateIncomeSQL = "UPDATE income SET source = ?, salary = ?, pay_frequency = ? WHERE username = ?";
        try (Connection conn = Database.connect();
             PreparedStatement updateStmt = conn.prepareStatement(updateIncomeSQL)) {

            updateStmt.setString(1, source);
            updateStmt.setBigDecimal(2, new BigDecimal(salary));  // Update salary as BigDecimal
            updateStmt.setString(3, payFrequency);
            updateStmt.setString(4, username);

            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Income data updated successfully!");
                displayIncomeData();
                clearFields();// Refresh the displayed data
            } else {
                showAlert("Failed to update income data.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to update income data.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        sourceField.clear();
        salaryField.clear();
        payFrequencyField.clear();
    }
    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
    }

}
