package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Gerant;
import services.GerantService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public class GerantSignupController {
    @FXML private TextField usernameField;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label usernameErrorLabel;
    @FXML private Label nameErrorLabel;
    @FXML private Label descriptionErrorLabel;
    @FXML private Label emailErrorLabel;
    @FXML private Label passwordErrorLabel;

    private File documentFile;
    private File imageFile;

    @FXML
    private void handleUploadDocument(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Document");
        documentFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if (documentFile != null) {
            System.out.println("Document selected: " + documentFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        imageFile = fileChooser.showOpenDialog(((Node)event.getSource()).getScene().getWindow());
        if (imageFile != null) {
            System.out.println("Image selected: " + imageFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleGerantSignUp(ActionEvent event) {
        // Reset error messages
        usernameErrorLabel.setVisible(false);
        nameErrorLabel.setVisible(false);
        descriptionErrorLabel.setVisible(false);
        emailErrorLabel.setVisible(false);
        passwordErrorLabel.setVisible(false);

        // Check for empty fields
        boolean hasError = false;
        if (usernameField.getText().isEmpty()) {
            usernameErrorLabel.setVisible(true);
            hasError = true;
        }
        if (nameField.getText().isEmpty()) {
            nameErrorLabel.setVisible(true);
            hasError = true;
        }
        if (descriptionArea.getText().isEmpty()) {
            descriptionErrorLabel.setVisible(true);
            hasError = true;
        }
        if (emailField.getText().isEmpty()) {
            emailErrorLabel.setVisible(true);
            hasError = true;
        }
        if (passwordField.getText().isEmpty()) {
            passwordErrorLabel.setVisible(true);
            hasError = true;
        }

        // If any field is empty, stop sign-up process
        if (hasError) {
            return;
        }

        // Continue with sign-up process
        try {
            String documentFileName = saveFile(documentFile, "src/main/resources/images/");
            String imageFileName = saveFile(imageFile, "src/main/resources/images/");

            Gerant newGerant = new Gerant(usernameField.getText(), nameField.getText(), descriptionArea.getText(), documentFileName, imageFileName, emailField.getText(), passwordField.getText(), Timestamp.from(Instant.now()));

            GerantService service = new GerantService();
            service.ajouter(newGerant);
            redirectToLogin(event);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }


    private String saveFile(File file, String dir) throws IOException {
        Path dest = Paths.get(dir + file.getName());
        Files.copy(file.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
        return file.getName();
    }
    private void redirectToLogin(ActionEvent event) throws IOException {
        Parent loginParent = FXMLLoader.load(getClass().getResource("/Login.fxml"));
        Scene loginScene = new Scene(loginParent);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(loginScene);
        window.show();
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
}
