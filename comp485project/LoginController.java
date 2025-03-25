package com.example.comp485project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LoginController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    @FXML
    private Text loginError;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private Button toCreateAccount;

    @FXML
    private Button loginButton;

    @FXML
    private TextField usernameInput;



    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @FXML
    public void switchToCreate(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("CreateAccount.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
