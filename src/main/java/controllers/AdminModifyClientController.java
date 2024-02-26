package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Client;
import services.Clientservices;

import java.sql.SQLException;

public class AdminModifyClientController {

    @FXML private TextField nomField;
    @FXML private TextField prenomField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private TextField adresseField;
    @FXML private TextField numTelField;

    private Clientservices clientService = new Clientservices();
    private Client currentClient;

    public void initialize() {
    }

    public void setClient(Client client) {
        this.currentClient = client;
        populateClientDetails();
    }

    private void populateClientDetails() {
        nomField.setText(currentClient.getNom());
        prenomField.setText(currentClient.getPrenom());
        emailField.setText(currentClient.getEmail());
        usernameField.setText(currentClient.getUsername());
        adresseField.setText(currentClient.getAdresse());
        numTelField.setText(Integer.toString(currentClient.getNumTel()));
    }

    @FXML
    private void saveClient() {
        currentClient.setNom(nomField.getText());
        currentClient.setPrenom(prenomField.getText());
        currentClient.setEmail(emailField.getText());
        currentClient.setUsername(usernameField.getText());
        currentClient.setAdresse(adresseField.getText());
        try {
            currentClient.setNumTel(Integer.parseInt(numTelField.getText()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for Num Tel.");
            return;
        }

        try {
            clientService.modifier(currentClient);
            System.out.println("Client updated successfully.");
            redirectToClientTable();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error updating client.");
        }
    }
    private void redirectToClientTable() {
        Platform.runLater(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/clientTable.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) nomField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error loading clienttable.fxml");
            }
        });
    }
}
