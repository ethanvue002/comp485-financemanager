package com.example.comp485project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;

public class SavingsController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    @FXML
    private ProgressBar standardGoal;

    @FXML
    private Button toDashboard;

    @FXML
    private Button editExpenses;

    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.show();
    }

}
