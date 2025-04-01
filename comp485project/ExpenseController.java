package com.example.comp485project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    @FXML
    private ListView<String> expensesList;

    @FXML
    private PieChart pieChartExpenses;

    @FXML
    private Button toDashboard;

    @FXML
    private TextField expenseNameField;

    @FXML
    private TextField expenseAmountField;

    @FXML
    private Button saveExpenseButton;

    private String username = LoginController.loggedInUsername;

    @FXML
    public void initialize() {
        loadExpenses();
    }

    @FXML
    public void saveExpense(ActionEvent event) {
        String expenseName = expenseNameField.getText();
        String amountText = expenseAmountField.getText();

        if (expenseName.isEmpty() || amountText.isEmpty()) {
            showAlert("Please enter both expense name and amount.");
            return;
        }

        try {
            BigDecimal amount = new BigDecimal(amountText);
            insertExpense(username, expenseName, amount);
            loadExpenses(); // Refresh UI
            clearFields();
        } catch (NumberFormatException e) {
            showAlert("Invalid amount. Please enter a valid number.");
        }
    }

    private void insertExpense(String username, String expenseName, BigDecimal amount) {
        String sql = "INSERT INTO expenses (username, expense_name, amount) VALUES (?, ?, ?)";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, expenseName);
            stmt.setBigDecimal(3, amount);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Expense added successfully!");
            } else {
                showAlert("Failed to add expense.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database error: " + e.getMessage());
        }
    }

    private void loadExpenses() {
        expensesList.getItems().clear();
        pieChartExpenses.getData().clear();

        String sql = "SELECT expense_name, amount FROM expenses WHERE username = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String expenseName = rs.getString("expense_name");
                BigDecimal amount = rs.getBigDecimal("amount");

                expensesList.getItems().add(expenseName + " - $" + amount);
                pieChartExpenses.getData().add(new PieChart.Data(expenseName, amount.doubleValue()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to load expenses.");
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
        expenseNameField.clear();
        expenseAmountField.clear();
    }

    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
    }
}
