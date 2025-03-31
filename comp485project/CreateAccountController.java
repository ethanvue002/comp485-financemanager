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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
    public void createAccount(ActionEvent event) {
        String username = inputUsername.getText().trim();
        String email = inputEmail.getText().trim();
        String phone = emailPhoneNumber.getText().trim();
        String name = inputName.getText().trim();
        String password = inputPass.getText();
        String confirmPassword = inputPassAgain.getText();
        if (username.isEmpty() || email.isEmpty() || phone.isEmpty() || name.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert("All fields must be filled!");
            return;
        }
        if (!password.equals(confirmPassword)) {
            {
                showAlert("Passwords do not match!");
                return;
            }
        }


        String sql = "INSERT INTO users (username, email, phone_number, name, password) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setString(4, name);
            stmt.setString(5, password);

            stmt.executeUpdate();
            showAlert("User registered successfully!");

            switchToLogin(event);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Input Error");
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
