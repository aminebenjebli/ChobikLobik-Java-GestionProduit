package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import models.Category;
import models.Plat;
import services.PlatServices;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;


public class AjouterPlatController {
    @FXML private TextField txtNomPlat, txtImagePlat, txtDescriptionPlat, txtPrixPlat;
    @FXML private ComboBox<Category> comboCategory;
    @FXML private Text actionStatus;

    private PlatServices platServices = new PlatServices();

    @FXML
    public void initialize() {
        try {
            ObservableList<Category> categoryList = FXCollections.observableArrayList(platServices.fetchCategories());
            comboCategory.setItems(categoryList);
        } catch (SQLException e) {
            actionStatus.setText("Error loading categories: " + e.getMessage());
        }
    }

    @FXML
    private void handleBrowseAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            txtImagePlat.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    void handleAjouterPlatAction(ActionEvent event) {
        try {
            String nom = txtNomPlat.getText();
            String image = txtImagePlat.getText();
            String description = txtDescriptionPlat.getText();
            float prix = Float.parseFloat(txtPrixPlat.getText());
            Category selectedCategory = comboCategory.getValue();
            if (selectedCategory != null) {
                Plat newPlat = new Plat(nom, description, prix, image, selectedCategory.getId());
                platServices.ajouter(newPlat);
                actionStatus.setText("Plat added successfully.");
                Parent tablePlatParent = FXMLLoader.load(getClass().getResource("/TablePlat.fxml"));
                Scene tablePlatScene = new Scene(tablePlatParent);
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(tablePlatScene);
                window.show();
            } else {
                actionStatus.setText("Please select a category.");
            }
        } catch (NumberFormatException e) {
            actionStatus.setText("Invalid number format in price.");
        } catch (SQLException | IOException e) {
            actionStatus.setText("Error adding plat: " + e.getMessage());
        }
    }
    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            Parent adminHomePageParent = FXMLLoader.load(getClass().getResource("/TablePlat.fxml"));
            Scene adminHomePageScene = new Scene(adminHomePageParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(adminHomePageScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
