package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Gerant;
import services.GerantService;

public class AdminModifyRestaurantController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField emailField;

    private Gerant currentGerant;
    private final GerantService gerantService = new GerantService();

    public void setGerant(Gerant gerant) {
        this.currentGerant = gerant;
        populateFields();
    }

    private void populateFields() {
        usernameField.setText(currentGerant.getUsername());
        nameField.setText(currentGerant.getName());
        descriptionField.setText(currentGerant.getDescription());
        emailField.setText(currentGerant.getEmail());
    }

    @FXML
    private void updateGerant() {
        currentGerant.setUsername(usernameField.getText());
        currentGerant.setName(nameField.getText());
        currentGerant.setDescription(descriptionField.getText());
        currentGerant.setEmail(emailField.getText());

        try {
            gerantService.modifier(currentGerant);
            closeStage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeStage() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
