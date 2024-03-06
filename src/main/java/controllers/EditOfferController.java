package controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Offre;
import services.OffreServices;
import utils.SessionManager;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class EditOfferController {
    @FXML
    private TextField txtPercentage, txtNewPrice, txtStartDate, txtEndDate;
    private Runnable onSuccessCallback;

    private Offre currentOffer;
    private OffreServices offreServices = new OffreServices();

    public void initData(Offre offer, Runnable callback) {
        this.currentOffer = offer;
        txtPercentage.setText(String.valueOf(offer.getPourcentage()));
        txtNewPrice.setText(String.format("%.2f", offer.getNew_price()));
        txtStartDate.setText(offer.getDate_debut() != null ? offer.getDate_debut().toString() : "");
        txtEndDate.setText(offer.getDate_fin() != null ? offer.getDate_fin().toString() : "");
        txtStartDate.setDisable(true);
        txtEndDate.setDisable(true);
        txtNewPrice.setDisable(true);
        txtPercentage.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateAndDisplayNewPrice();
        });
        this.onSuccessCallback = callback;
    }

    private void calculateAndDisplayNewPrice() {
        try {
            double percentage = Double.parseDouble(txtPercentage.getText());
            double originalPrice = currentOffer.getNew_price();
            double newPrice = originalPrice * (1 - (percentage / 100.0));
            NumberFormat format = DecimalFormat.getInstance(Locale.US);
            txtNewPrice.setText(format.format(newPrice));
        } catch (NumberFormatException e) {
            txtNewPrice.setText("Invalid number format.");
        }
    }

    @FXML
    private void handleSave(ActionEvent event) {
        try {
            int newPercentage = Integer.parseInt(txtPercentage.getText());
            float newPrice = Float.parseFloat(txtNewPrice.getText());

            currentOffer.setPourcentage(newPercentage);
            currentOffer.setNew_price(newPrice);

            offreServices.updateOffer(currentOffer);

            if (onSuccessCallback != null) {
                onSuccessCallback.run();
            }
            closeStage();
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "Please ensure the percentage and new price fields are correctly filled.");
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while saving the offer: " + e.getMessage());
        }
    }



    @FXML
    private void handleCancel(ActionEvent event) {
        closeStage();
    }

    private void closeStage() {
        Stage stage = (Stage) txtPercentage.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

