package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import models.Client;
import services.Clientservices;
import utils.MyDatabase;
import utils.SessionManager;

import java.io.IOException;
import java.sql.*;


public class SignUpClientController {

    @FXML private TextField nomClient;
    @FXML private TextField prenomClient;
    @FXML private TextField emailClient;
    @FXML private PasswordField passwordClient;
    @FXML private TextField usernameClient;
    @FXML private ComboBox<String> adresseClient;
    @FXML private TextField numTelClient;
    @FXML
    private ImageView myImageView;


    private Clientservices clientService = new Clientservices();


    private void loadCityNames() {
        String query = "SELECT city FROM cities";
        try {
            Connection conn = MyDatabase.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String cityName = rs.getString("city");
                adresseClient.getItems().add(cityName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void initialize() {
        loadCityNames();
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/food.jpg"));
            myImageView.setImage(image);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading image.");
        }
    }

    @FXML
    private void handleSignUpClient(ActionEvent event) {
        String nom = nomClient.getText();
        String prenom = prenomClient.getText();
        String email = emailClient.getText();
        String password = passwordClient.getText();
        String username = usernameClient.getText();
        String selectedCity = adresseClient.getSelectionModel().getSelectedItem();
        String numTelString = numTelClient.getText();

        // Validate the form input
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty() || username.isEmpty() || selectedCity == null || numTelString.isEmpty()) {
            // Display an error message to the user
            System.out.println("All fields are required.");
            return;
        }

        int numTel;
        try {
            numTel = Integer.parseInt(numTelString);
        } catch (NumberFormatException e) {
            System.out.println("Invalid phone number format.");
            return;
        }


        Client newClient = new Client(
                nom,
                prenom,
                email,
                password,
                username,
                selectedCity,
                numTel,
                new java.util.Date()
        );

        try {
            clientService.ajouter(newClient);
            System.out.println("Client successfully signed up.");
            redirectToLogin(nomClient);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sign up the client.");
        }
    }
    private void redirectToLogin(Node node) {
        try {
            System.out.println("Attempting to redirect to login page...");

            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

            System.out.println("Redirection to login page successful.");
        } catch (Exception e) {
            System.out.println("Redirection to login page failed.");
            e.printStackTrace();
        }
    }
    public void handleBackAction(ActionEvent event) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/Showroom.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleLogoutAction(ActionEvent event) {
        SessionManager.clearSession();

        try {
            Parent showroomParent = FXMLLoader.load(getClass().getResource("/Showroom.fxml"));
            Scene showroomScene = new Scene(showroomParent);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(showroomScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
