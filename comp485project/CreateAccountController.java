package com.example.comp485project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateAccountController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField emailPhoneNumber;

    @FXML
    private TextField inputEmail;

    @FXML
    private TextField inputName;

    @FXML
    private PasswordField inputPass;

    @FXML
    private PasswordField inputPassAgain;

    @FXML
    private TextField inputUsername;

    @FXML
    private Button toDashboardFromCreate;

    @FXML
    private Button toLoginFromCreate;

    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @FXML
    public void switchToLogin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
