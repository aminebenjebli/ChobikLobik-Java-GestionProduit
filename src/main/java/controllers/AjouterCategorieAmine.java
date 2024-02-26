package controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private TextField ImageCategoryField;

    @FXML
    private TextField TypeCategoryField;

    private final CategoryServices cs = new CategoryServices();

    @FXML
    public void initialize() {

    }


    @FXML
    private void BtnAjouterCat(MouseEvent event) {
        String categoryType = TypeCategoryField.getText();
        String categoryImage = ImageCategoryField.getText();

        if (categoryType.isEmpty() || categoryImage.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Category Type and Image are required.");
            return;
        }

        Category category = new Category();
        category.setType(categoryType);
        category.setImage(categoryImage);

        try {
            cs.ajouter(category);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Category added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error adding category.");
        }
    }

    @FXML
    void consulterCategorie(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/projets_aminebj/AfficherCat.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher Catégorie");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading AfficherCat.fxml");
        }
    }


    @FXML
    void BtnSelectionnerImageCategory(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
        // Filtrer les types de fichiers pour n'afficher que les images
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Images", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(extFilter);
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            ImageCategoryField.setText(((File) selectedFile).getAbsolutePath());
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
