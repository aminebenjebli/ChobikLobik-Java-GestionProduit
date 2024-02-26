package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Admin;
import models.Client;
import models.Gerant;
import models.Livreur;
import services.Adminservices;
import services.Clientservices;
import services.GerantService;
import utils.SessionManager;
import services.LivreurService;
import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML private TextField usernameOrEmail;
    @FXML private PasswordField password;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;
    private final Clientservices clientService = new Clientservices();
    private final Adminservices adminService = new Adminservices();
    private LivreurService livreurService = new LivreurService();
    private GerantService gerantService = new GerantService();



    @FXML
    private void handleLogin(ActionEvent event) {
        String userEmail = usernameOrEmail.getText();
        String userPass = password.getText();
        boolean isAuthenticated = false;

        try {
            if (clientService.authenticate(userEmail, userPass)) {
                Client loggedInClient = clientService.getClientByUsernameOrEmail(userEmail);
                SessionManager.setCurrentClient(loggedInClient);
                redirectToClientHomePage();
                isAuthenticated = true;
            } else if (adminService.authenticateByEmail(userEmail, userPass)) {
                Admin loggedInAdmin = adminService.getAdminByEmail(userEmail);
                SessionManager.setCurrentAdmin(loggedInAdmin);
                redirectToAdminHomePage();
                isAuthenticated = true;
            } else if (livreurService.authenticateByEmail(userEmail, userPass)) {
                Livreur loggedInLivreur = livreurService.getLivreurByEmail(userEmail);
                SessionManager.setCurrentLivreur(loggedInLivreur);
                redirectToLivreurHomePage();
                isAuthenticated = true;
             }
            else if (gerantService.authenticateByEmail(userEmail, userPass)) {
                Gerant loggedInGerant = gerantService.getGerantByEmail(userEmail);
                SessionManager.setCurrentGerant(loggedInGerant);
                redirectToGerantHomePage();
                isAuthenticated = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!isAuthenticated) {
            showError("Invalid username or password.");
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void redirectToLivreurHomePage() {
        try {
            System.out.println("Redirecting to livreur home page...");

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/LivreurHomePage.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Failed to redirect to livreur home page.");
            e.printStackTrace();
        }
    }
    private void redirectToClientHomePage() {
        try {
            System.out.println("Redirecting to client home page...");

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ClientHomePage.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Failed to redirect to client home page.");
            e.printStackTrace();
        }
    }
    private void redirectToAdminHomePage() {
        try {
            System.out.println("Redirecting to admin home page...");

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/AdminHomePage.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.out.println("Failed to redirect to admin home page.");
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
    // In LoginController.java
    private void redirectToGerantHomePage() {
        try {
            Gerant loggedInGerant = SessionManager.getCurrentGerant();
            if (loggedInGerant != null && gerantService.isGerantSubscriptionActive(loggedInGerant.getId())) {
                redirectToDashboardRestaurant();
            } else {
                System.out.println("Redirecting to gerant home page...");
                Stage stage = (Stage) loginButton.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/GerantHomePage.fxml"));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        } catch (IOException | SQLException e) {
            System.out.println("Failed to redirect.");
            e.printStackTrace();
        }
    }

    private void redirectToDashboardRestaurant() {
        try {
            System.out.println("Redirecting to dashboard restaurant...");
            Stage stage = (Stage) loginButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/dashboard_restaurant.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("Failed to redirect to dashboard restaurant.");
            e.printStackTrace();
        }
    }


}
