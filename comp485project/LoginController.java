package com.example.comp485project;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static String loggedInUsername = "";

    public void login(ActionEvent event) {
        // Get user input and trim whitespace
        String username = usernameInput.getText().trim();
        String password = passwordInput.getText();

        // Validation: Check if fields are empty
        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error");
            return;
        }

        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);  // Ideally, hash this for security

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                loggedInUsername = username;
                showAlert("Login successful!");

                switchToDashboard(event);
            } else {
                showAlert("Invalid username or password.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            loginError.setText("Database error.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setLoggedInUsername(loggedInUsername);
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
