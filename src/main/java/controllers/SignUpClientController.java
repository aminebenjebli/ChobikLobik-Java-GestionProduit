package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;

import javafx.scene.control.Label;
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
    @FXML
    private Label nomError;
    @FXML
    private Label prenomError;
    @FXML
    private Label emailError;
    @FXML
    private Label passwordError;
    @FXML
    private Label usernameError;
    @FXML
    private Label adresseError;
    @FXML
    private Label numTelError;

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

        boolean isValid = true;

        clearErrorLabels();

        if (nom.isEmpty()) {
            nomError.setText("Nom is required.");
            isValid = false;
        }
        if (prenom.isEmpty()) {
            prenomError.setText("Prenom is required.");
            isValid = false;
        }
        if (email.isEmpty()) {
            emailError.setText("Email is required.");
            isValid = false;
        }
        if (password.isEmpty()) {
            passwordError.setText("Password is required.");
            isValid = false;
        }
        if (username.isEmpty()) {
            usernameError.setText("Username is required.");
            isValid = false;
        }
        if (selectedCity == null) {
            adresseError.setText("Adresse is required.");
            isValid = false;
        }
        if (numTelString.isEmpty()) {
            numTelError.setText("NumTel is required.");
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        int numTel;
        try {
            numTel = Integer.parseInt(numTelString);
        } catch (NumberFormatException e) {
            numTelError.setText("Invalid phone number format.");
            return;
        }

        Client newClient = new Client(nom, prenom, email, password, username, selectedCity, numTel, new java.util.Date());

        try {
            clientService.ajouter(newClient);
            System.out.println("Client successfully signed up.");
            redirectToLogin(nomClient);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Failed to sign up the client.");
        }
    }

    private void clearErrorLabels() {
        nomError.setText("");
        prenomError.setText("");
        emailError.setText("");
        passwordError.setText("");
        usernameError.setText("");
        adresseError.setText("");
        numTelError.setText("");

        nomError.setStyle("");
        prenomError.setStyle("");
        emailError.setStyle("");
        passwordError.setStyle("");
        usernameError.setStyle("");
        adresseError.setStyle("");
        numTelError.setStyle("");
    }

    private void setError(Label label, String errorMessage) {
        label.setText(errorMessage);
        label.setStyle("-fx-text-fill: red;");
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
