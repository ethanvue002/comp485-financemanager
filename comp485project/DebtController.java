package com.example.comp485project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.IOException;

public class DebtController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    @FXML
    private Text debtOwed, interestOfDebt, totalDebtDisplay;

    @FXML
    private ProgressBar debtProgress;

    @FXML
    private Button toDashboard, saveDebt;

    @FXML
    private TextField debtInput, interestInput;

    private static final BigDecimal MAX_DEBT = new BigDecimal("10000"); // Max debt for progress bar

    @FXML
    public void initialize() {
        loadDebtData(); // Load existing debt data when the page initializes
    }

    private void loadDebtData() {
        String username = LoginController.loggedInUsername;
        String fetchDebtSQL = "SELECT debt_amount, interest FROM debt WHERE username = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(fetchDebtSQL)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                BigDecimal debtAmount = rs.getBigDecimal("debt_amount");
                BigDecimal interest = rs.getBigDecimal("interest");

                debtOwed.setText("Debt Owed: $" + debtAmount);
                interestOfDebt.setText("Interest: " + interest + "%");

                BigDecimal totalDebt = calculateTotalDebt(debtAmount, interest);
                totalDebtDisplay.setText("Total Debt: $" + totalDebt);

                updateProgressBar(totalDebt);
            } else {
                debtOwed.setText("No Debt Data");
                interestOfDebt.setText("Enter Details");
                totalDebtDisplay.setText("Total Debt: $0");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to load debt data.");
        }
    }

    @FXML
    public void saveDebtDetails(ActionEvent event) {
        String username = LoginController.loggedInUsername;
        String debtStr = debtInput.getText();
        String interestStr = interestInput.getText();

        if (debtStr.isEmpty() || interestStr.isEmpty()) {
            showAlert("Please enter both debt amount and interest.");
            return;
        }

        try {
            BigDecimal debtAmount = new BigDecimal(debtStr);
            BigDecimal interest = new BigDecimal(interestStr);
            BigDecimal totalDebt = calculateTotalDebt(debtAmount, interest);

            if (debtExists(username)) {
                updateDebt(username, debtAmount, interest);
            } else {
                insertDebt(username, debtAmount, interest);
            }

            debtOwed.setText("Debt Owed: $" + debtAmount);
            interestOfDebt.setText("Interest: " + interest + "%");
            totalDebtDisplay.setText("Total Debt: $" + totalDebt);

            updateProgressBar(totalDebt);

        } catch (NumberFormatException e) {
            showAlert("Invalid input. Please enter numeric values.");
        }
    }

    private boolean debtExists(String username) {
        String checkDebtSQL = "SELECT * FROM debt WHERE username = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(checkDebtSQL)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void insertDebt(String username, BigDecimal debtAmount, BigDecimal interest) {
        String insertDebtSQL = "INSERT INTO debt (username, debt_amount, interest) VALUES (?, ?, ?)"
                             + "ON DUPLICATE KEY UPDATE debt_amount = VALUES(debt_amount), interest = VALUES(interest)";;
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(insertDebtSQL)) {

            stmt.setString(1, username);
            stmt.setBigDecimal(2, debtAmount);
            stmt.setBigDecimal(3, interest);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Debt details saved successfully!");
            } else {
                showAlert("No rows were inserted. Possible primary key conflict.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("SQL Error: " + e.getMessage());
        }
    }


    private void updateDebt(String username, BigDecimal debtAmount, BigDecimal interest) {
        String updateDebtSQL = "UPDATE debt SET debt_amount = ?, interest = ? WHERE username = ?";
        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(updateDebtSQL)) {

            stmt.setBigDecimal(1, debtAmount);
            stmt.setBigDecimal(2, interest);
            stmt.setString(3, username);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Debt details updated successfully!");
            } else {
                showAlert("Failed to update debt details.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Failed to update debt details.");
        }
    }

    private BigDecimal calculateTotalDebt(BigDecimal debtAmount, BigDecimal interest) {
        return debtAmount.add(debtAmount.multiply(interest.divide(new BigDecimal("100"))));
    }

    private void updateProgressBar(BigDecimal totalDebt) {
        BigDecimal progress = totalDebt.divide(MAX_DEBT, 2, BigDecimal.ROUND_HALF_UP);
        progress = progress.min(BigDecimal.ONE); // Cap at 100%
        debtProgress.setProgress(progress.doubleValue());
        debtProgress.setStyle("-fx-accent: green;"); // Make progress bar green
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
    }

}
