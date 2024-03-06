package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Admin;
import services.Adminservices;
import javafx.event.ActionEvent;

public class AdminProfileController {
    @FXML private Label updateSuccessLabel;
    @FXML private Label labelPrenom;
    @FXML private Label labelNom;
    @FXML private Label labelEmail;
    @FXML private VBox updateForm;
    @FXML private TextField updatePrenom;
    @FXML private TextField updateNom;
    @FXML private TextField updateEmail;
    @FXML private PasswordField updatePassword;

    private Adminservices adminService = new Adminservices();
    private Admin currentAdmin;

    @FXML
    public void initialize() {
        fetchAdminInfo();
    }

    private void fetchAdminInfo() {
        try {
            currentAdmin = adminService.getAdminById(1);
            Platform.runLater(() -> {
                if (currentAdmin != null) {
                    labelPrenom.setText(currentAdmin.getPrenom());
                    labelNom.setText(currentAdmin.getNom());
                    labelEmail.setText(currentAdmin.getEmail());
                } else {
                    labelPrenom.setText("Admin not found");
                    labelNom.setText("");
                    labelEmail.setText("");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> {
                labelPrenom.setText("Error fetching admin information");
                labelNom.setText("");
                labelEmail.setText("");
            });
        }
    }

    @FXML
    private void showUpdateForm() {
        updatePrenom.setText(currentAdmin.getPrenom());
        updateNom.setText(currentAdmin.getNom());
        updateEmail.setText(currentAdmin.getEmail());
        updateForm.setVisible(true);
    }

    @FXML
    private void handleSubmit() {
        String prenom = updatePrenom.getText();
        String nom = updateNom.getText();
        String email = updateEmail.getText();
        String password = updatePassword.getText();

        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showErrorMessage("No field should be empty.");
            return;
        }

        if (prenom.length() < 5 || nom.length() < 5 || password.length() < 5) {
            showErrorMessage("Prenom, Nom, and Password must be at least 5 characters long.");
            return;
        }

        if (containsNumbers(prenom) || containsNumbers(nom)) {
            showErrorMessage("Prenom and Nom should not contain numbers.");
            return;
        }

        if (isNumeric(password)) {
            showErrorMessage("Password should not be purely numerical.");
            return;
        }

        try {
            Admin updatedAdmin = new Admin(currentAdmin.getId(), nom, prenom, email, password);
            adminService.modifier(updatedAdmin);
            fetchAdminInfo();
            updateForm.setVisible(false);
            showSuccessMessage("Update completed successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Update failed.");
        }
    }

    private boolean containsNumbers(String str) {
        return str.matches(".*\\d.*");
    }

    private boolean isNumeric(String str) {
        return str.matches("[+-]?\\d*(\\.\\d+)?");
    }


    private void showSuccessMessage(String message) {
        Platform.runLater(() -> {
            updateSuccessLabel.setText(message);
            updateSuccessLabel.setVisible(true);
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> updateSuccessLabel.setVisible(false));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    private void showErrorMessage(String message) {
        Platform.runLater(() -> {
            updateSuccessLabel.setText(message);
            updateSuccessLabel.setVisible(true);
            new Thread(() -> {
                try {
                    Thread.sleep(5000);
                    Platform.runLater(() -> updateSuccessLabel.setVisible(false));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        });
    }

    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            Parent adminHomePageParent = FXMLLoader.load(getClass().getResource("/AdminDashboard.fxml"));
            Scene adminHomePageScene = new Scene(adminHomePageParent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(adminHomePageScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
