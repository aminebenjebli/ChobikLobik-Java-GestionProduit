package controllers;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Gerant;
import utils.SessionManager;

public class CheckProfileGerant {
    @FXML
    private Button editImg;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField emailG;

    @FXML
    private ImageView imageGerant;

    @FXML
    private TextField newPasswordG;

    @FXML
    private TextField passwordG;
    @FXML
    private TextArea descriptionG;
    @FXML
    private TextField usernameG;
    private Gerant gerant;
    @FXML
    void initialize() {
        gerant = SessionManager.getCurrentGerant();

        if (gerant != null) {
            // Bind the fields to the manager's information
            usernameG.setText(gerant.getUsername());
            emailG.setText(gerant.getEmail());
            passwordG.setText(gerant.getPassword());
            descriptionG.setText(gerant.getDescription());

            // Set the manager's image
            Image image = new Image(gerant.getImage());
            imageGerant.setImage(image);
        }
    }
    @FXML
    void selectionnerNewImg(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            try {
                Image image = new Image(selectedFile.toURI().toString());
                imageGerant.setImage(image);

                // Update the manager's image in the database
                gerant.setImage(selectedFile.toURI().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void updateProfileG(MouseEvent event) {
        if (gerant != null) {
            // Update the manager's information in the database
            gerant.setUsername(usernameG.getText());
            gerant.setEmail(emailG.getText());
            gerant.setPassword(passwordG.getText());
            gerant.setDescription(descriptionG.getText());

            // You need to update the gerant object in the session manager or database here
            SessionManager.setCurrentGerant(gerant);
        }
    }
}
