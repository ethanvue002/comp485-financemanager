package com.example.comp485project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Parent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.text.Text;
import static com.example.comp485project.LoginController.loggedInUsername;

public class AccountController {
    private Stage primaryStage;
    private Scene scene;
    private Parent root;

    @FXML
    private Text displayName;

    @FXML
    private Text displayEmail;

    @FXML
    private Text displayPhoneNumber;

    @FXML
    private Text displayUsername;

    @FXML
    public void initialize() {
        loadUserData();
    }

    private void loadUserData() {
        String query = "SELECT name, email, phone_number, username FROM users WHERE username = ?";

        try (Connection conn = Database.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Set the username to retrieve the correct user data
            stmt.setString(1, loggedInUsername);
            ResultSet resultSet = stmt.executeQuery();

            if (resultSet.next()) {
                // Print out the fetched data to check
                System.out.println("Name: " + resultSet.getString("name"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Phone: " + resultSet.getString("phone_number"));
                System.out.println("Username: " + resultSet.getString("username"));

                // Display the data in the corresponding Text fields
                displayName.setText(resultSet.getString("name"));
                displayEmail.setText(resultSet.getString("email"));
                displayPhoneNumber.setText(resultSet.getString("phone_number"));
                displayUsername.setText(resultSet.getString("username"));
            } else {
                System.out.println("No data found for username: " + loggedInUsername);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToDashboard(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
        primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }





}
