package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Category;
import models.Plat;
import services.PlatServices;

import java.io.IOException;
import java.sql.SQLException;

public class AjouterPlatController {

    @FXML
    private TextField txtNomPlat;
    @FXML
    private TextField txtImagePlat;
    @FXML
    private TextField txtDescriptionPlat;
    @FXML
    private TextField txtPrixPlat;
    @FXML
    private ComboBox<Category> comboCategory;
    @FXML
    private Button btnAjouterPlat;
    @FXML
    private Text actionStatus;
    @FXML
    private ImageView imageView;

    @FXML
    private Button consulterPlats;
    private PlatServices platServices;

    @FXML
    public void initialize() {
        platServices = new PlatServices();
        try {
            ObservableList<Category> categoryList = FXCollections.observableArrayList(platServices.fetchCategories());
            comboCategory.setItems(categoryList);
        } catch (SQLException e) {
            actionStatus.setText("Erreur lors du chargement des catégories: " + e.getMessage());
        }
        comboCategory.setCellFactory(lv -> new ListCell<Category>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                setText(empty ? null : category.getType());
            }
        });
        comboCategory.setButtonCell(new ListCell<Category>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                setText(empty ? null : category.getType());
            }
        });

    }
    @FXML
    void consulterPlat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherPlats.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Afficher Catégorie");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading AfficherPlats.fxml");
        }
    }
    @FXML
    void handleAjouterPlatAction(ActionEvent event) {
        try {
            Category selectedCategory = comboCategory.getValue();
            if (selectedCategory == null) {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez sélectionner une catégorie.");
                return;
            }

            String nom = txtNomPlat.getText();
            String image = txtImagePlat.getText();
            String description = txtDescriptionPlat.getText();
            String prixText = txtPrixPlat.getText();

            if (nom.isEmpty() || image.isEmpty() || description.isEmpty() || prixText.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Veuillez remplir tous les champs.");
                return;
            }
            //controle de saisie for price > 0
            float prix = Float.parseFloat(prixText);
            if (prix <= 0) {
                showAlert(Alert.AlertType.WARNING, "Avertissement", "Le prix doit être supérieur à zéro.");
                return;
            }

            Plat newPlat = new Plat();
            newPlat.setNom(nom);
            newPlat.setImage(image);
            newPlat.setDescription(description);
            newPlat.setPrix(prix);
            newPlat.setId_restaurant(getSelectedRestaurantId());

            platServices.ajouter(newPlat, selectedCategory.getId());
            showAlert(Alert.AlertType.INFORMATION, "Succès", "Plat ajouté avec succès.");
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Format de nombre invalide pour le prix.");
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors de l'ajout du plat: " + e.getMessage());
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private int getSelectedRestaurantId() {
        return 1;
    }
}
