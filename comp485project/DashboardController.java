package com.example.comp485project;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class DashboardController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button accountButton;

    @FXML
    private Button debtButton;

    @FXML
    private Button expensesButton;

    @FXML
    private Button incomeButton;

    @FXML
    private Button savingsButton;

    @FXML
    private Text incomeOverview;

    @FXML
    private Text debtOverview;

    @FXML
    private Text expensesOverview;

    @FXML
    private Text savingsOverview;

    private String loggedInUsername;

    // Create a setter method to receive the username
    public void setLoggedInUsername(String username) {
        this.loggedInUsername = username;
        loadUserData();  // Load the data when the username is set
    }
    // Load the data (income, debt, expenses, savings) from the database
    private void loadUserData() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/finance_manager", "root", "password")) {

            // Fetch income (sum of all salaries)
            PreparedStatement incomeStmt = connection.prepareStatement("SELECT SUM(salary) AS total_income FROM income WHERE username = ?");
            incomeStmt.setString(1, loggedInUsername);
            ResultSet incomeResult = incomeStmt.executeQuery();
            if (incomeResult.next()) {
                incomeOverview.setText("$" + incomeResult.getDouble("total_income"));
            } else {
                incomeOverview.setText("$0.00");
                System.out.println("No income data found for user: " + loggedInUsername);
            }

            // Fetch debt amount
            PreparedStatement debtStmt = connection.prepareStatement("SELECT debt_amount FROM debt WHERE username = ?");
            debtStmt.setString(1, loggedInUsername);
            ResultSet debtResult = debtStmt.executeQuery();
            if (debtResult.next()) {
                debtOverview.setText("$" + debtResult.getDouble("debt_amount"));
            } else {
                debtOverview.setText("$0.00");
                System.out.println("No debt data found for user: " + loggedInUsername);
            }

            // Fetch total expenses
            PreparedStatement expensesStmt = connection.prepareStatement("SELECT SUM(amount) AS total_expenses FROM expenses WHERE username = ?");
            expensesStmt.setString(1, loggedInUsername);
            ResultSet expensesResult = expensesStmt.executeQuery();
            if (expensesResult.next()) {
                expensesOverview.setText("$" + expensesResult.getDouble("total_expenses"));
            } else {
                expensesOverview.setText("$0.00");
                System.out.println("No expenses data found for user: " + loggedInUsername);
            }

            // Fetch savings goal and saved amount
            PreparedStatement savingsStmt = connection.prepareStatement("SELECT goal_amount, saved_amount FROM savings WHERE username = ?");
            savingsStmt.setString(1, loggedInUsername);
            ResultSet savingsResult = savingsStmt.executeQuery();
            if (savingsResult.next()) {
                double goalAmount = savingsResult.getDouble("goal_amount");
                double savedAmount = savingsResult.getDouble("saved_amount");
                savingsOverview.setText("$" + savedAmount + " / $" + goalAmount);
            } else {
                savingsOverview.setText("$0.00 / $0.00");
                System.out.println("No savings data found for user: " + loggedInUsername);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Error fetching user data from the database. See console for details.");
        }
    }

    public void initialize() {
        loggedInUsername = SessionManager.getUsername();
        if (loggedInUsername != null) {
            loadUserData();
        } else {
            showAlert("Error", "No user is logged in.");
        }
    }

    @FXML
    public void switchToAccount(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Account.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @FXML
    public void switchToExpense(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Expense.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @FXML
    public void switchToSavings(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Savings.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @FXML
    public void switchToIncome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Income.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @FXML
    public void switchToDebt(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Debt.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
