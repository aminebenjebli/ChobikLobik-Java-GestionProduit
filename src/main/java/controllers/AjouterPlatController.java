package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import models.Category;
import models.Plat;
import services.PlatServices;

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

    private PlatServices platServices;

    @FXML
    public void initialize() {
        platServices = new PlatServices();
        try {
            ObservableList<Category> categoryList = FXCollections.observableArrayList(platServices.fetchCategories());
            comboCategory.setItems(categoryList);
        } catch (SQLException e) {
            actionStatus.setText("Error loading categories: " + e.getMessage());
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
    void handleAjouterPlatAction(ActionEvent event) {
        try {
            Category selectedCategory = comboCategory.getValue();
            if (selectedCategory == null) {
                actionStatus.setText("Please select a category.");
                return;
            }
            Plat newPlat = new Plat();
            newPlat.setNom(txtNomPlat.getText());
            newPlat.setImage(txtImagePlat.getText());
            newPlat.setDescription(txtDescriptionPlat.getText());
            newPlat.setPrix(Float.parseFloat(txtPrixPlat.getText()));
            newPlat.setId_restaurant(getSelectedRestaurantId());
            platServices.ajouter(newPlat, selectedCategory.getId());
            actionStatus.setText("Plat added successfully.");
        } catch (NumberFormatException e) {
            actionStatus.setText("Invalid number format in price.");
        } catch (SQLException e) {
            actionStatus.setText("Error adding plat: " + e.getMessage());
        } catch (Exception e) {
            actionStatus.setText("Error: " + e.getMessage());
        }
    }

    private int getSelectedRestaurantId() {
        return 1;
    }
}
