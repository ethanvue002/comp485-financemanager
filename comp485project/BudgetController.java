package com.example.comp485project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class BudgetController {

    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    @FXML
    private Button debtButton;

    @FXML
    private Button expensesButton;

    @FXML
    private Button incomeButton;

    @FXML
    private Button savingsButton;

    @FXML
    private Button toDashboard;

    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
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

    @FXML
    public void switchToExpense(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Expense.fxml"));
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
    public void switchToSavings(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Savings.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
