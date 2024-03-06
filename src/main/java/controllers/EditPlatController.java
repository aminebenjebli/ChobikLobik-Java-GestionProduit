package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Category;
import models.Plat;
import services.CategoryServices;

import java.sql.SQLException;
import java.util.List;

public class EditPlatController {

    @FXML
    private TextField nomTextField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML

    private TextArea descriptionTextField;
    @FXML
    private TextField prixTextField;
    @FXML
    private Button submitButton;

    private Plat platToEdit;
    private TablePlatController tablePlatController;

    public void initializeForm(Plat plat, TablePlatController controller) {
        this.platToEdit = plat;
        this.tablePlatController = controller;
        loadCategoriesIntoComboBox();

        nomTextField.setText(plat.getNom());
        categoryComboBox.setValue(plat.getCategoryName());
        descriptionTextField.setText(plat.getDescription());
        prixTextField.setText(Double.toString(plat.getPrix()));
    }

    private void loadCategoriesIntoComboBox() {
        CategoryServices categoryServices = new CategoryServices();
        try {
            List<Category> categories = categoryServices.afficher();
            categoryComboBox.getItems().clear(); // Clear existing items
            for (Category category : categories) {
                categoryComboBox.getItems().add(category.getType()); // Add category name
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception (e.g., show an error dialog)
        }
    }


    @FXML
    private void handleSubmitAction() {
        platToEdit.setNom(nomTextField.getText());
        platToEdit.setCategoryName(categoryComboBox.getValue());
        platToEdit.setDescription(descriptionTextField.getText());
        platToEdit.setPrix(Double.parseDouble(prixTextField.getText()));

        tablePlatController.updateTableView(platToEdit);

        Stage stage = (Stage) submitButton.getScene().getWindow();
        stage.close();
    }
}
