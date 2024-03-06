package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Category;
import services.CategoryServices;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class AjouterCategorieAmine {

    @FXML
    private TextField TypeCategoryField;

    private final CategoryServices cs = new CategoryServices();

    @FXML
    public void initialize() {

    }


    @FXML
    private void BtnAjouterCat(MouseEvent event) {
        String categoryType = TypeCategoryField.getText();
        if (categoryType.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Category Type is required.");
            return;
        }

        Category category = new Category();
        category.setType(categoryType);

        try {
            cs.ajouter(category);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Category added successfully.");

            // Close the current window
            ((Node) (event.getSource())).getScene().getWindow().hide();

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding category.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



}
