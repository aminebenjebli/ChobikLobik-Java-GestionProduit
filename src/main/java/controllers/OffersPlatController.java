package controllers;

import com.google.common.util.concurrent.Service;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Client;
import models.Offre;
import models.Plat;
import services.OffreServices;
import services.SubscriptionService;
import utils.SessionManager;

import java.io.IOException;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.sql.SQLException;
import java.time.LocalDate;




public class OffersPlatController {

    @FXML private TextField nameplat;
    @FXML private TextField oldprice;
    @FXML private DatePicker dpDateDebut;
    @FXML private DatePicker dpDateFin;
    @FXML private TextField txtPercentage;
    @FXML private TextField txtNewPrice;
    @FXML private Text actionStatus;

    private Plat plat;
    private OffreServices offreServices;
    private SubscriptionService subscriptionService;
    public void initData(Plat plat) {
        this.plat = plat;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        numberFormat.setGroupingUsed(false); // Turn off grouping to avoid commas in thousands
        numberFormat.setMaximumFractionDigits(2); // Set the maximum number of decimal places to 2

        String formattedPrice = numberFormat.format(plat.getPrix());
        nameplat.setText(plat.getNom());
        oldprice.setText(formattedPrice);

        nameplat.setDisable(true);
        oldprice.setDisable(true);
        txtNewPrice.setDisable(true);

        // Initialize your service layer here, if needed
        offreServices = new OffreServices();
        subscriptionService = new SubscriptionService();
        txtPercentage.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateAndDisplayNewPrice();
        });
    }

    @FXML
    private void calculateAndDisplayNewPrice() {
        NumberFormat format = DecimalFormat.getInstance(Locale.FRANCE);
        if (format instanceof DecimalFormat) {
            ((DecimalFormat) format).setParseBigDecimal(true);
        }

        try {
            String oldPriceText = oldprice.getText().replaceAll("[^\\d,.-]", "");
            String percentageText = txtPercentage.getText().replaceAll("[^\\d,.-]", "");

            Number oldPriceNumber = format.parse(oldPriceText);
            Number percentageNumber = format.parse(percentageText);

            // Convert the parsed Numbers to double values
            double oldPriceValue = oldPriceNumber.doubleValue();
            double percentageValue = percentageNumber.doubleValue();

            // Calculate the new price
            double newPriceValue = oldPriceValue * (1 - (percentageValue / 100.0));

            // Format the new price as a string using the same locale-specific format
            txtNewPrice.setText(format.format(newPriceValue));
        } catch (ParseException e) {
            // Handle parsing errors here - possibly notify the user of an input error
            txtNewPrice.setText("Invalid number format.");
        }
    }




    @FXML
    private void handleAjouter(ActionEvent event) {
        try {
            int gerantId = SessionManager.getCurrentGerant().getId();
            double percentage = Double.parseDouble(txtPercentage.getText());
            LocalDate startDate = dpDateDebut.getValue();
            LocalDate endDate = dpDateFin.getValue();
            if (!subscriptionService.canCreateOffer(gerantId)) {
                actionStatus.setText("You have reached the limit of offers for your subscription type.");
                showAlert("Offer Limit Reached", "You cannot create more offers with your current subscription type.");
                return;
            }
            // Check if dates are entered
            if (startDate == null || endDate == null) {
                actionStatus.setText("Please enter both start and end dates.");
                return;
            }
            if (startDate.isAfter(endDate)) {
                actionStatus.setText("Start date must not be later than end date.");
                return; // Stop execution if the start date is after the end date
            }

            // Check if the subscription is valid for the given date range
            LocalDate subscriptionEndDate = subscriptionService.getSubscriptionEndDate(gerantId);
            if (subscriptionEndDate == null) {
                actionStatus.setText("No active subscription found for Gerant ID: " + gerantId);
                return;
            }

            if (endDate.isAfter(subscriptionEndDate)) {
                actionStatus.setText("Your offer cannot exceed the date of your subscription.");
                return;
            }

            // Calculate the new price based on the old price and the percentage
            double oldPriceValue = Double.parseDouble(oldprice.getText());
            double newPrice = oldPriceValue * (1 - (percentage / 100.0));

            offreServices.createOffer(plat.getIdPlat(), percentage, newPrice, Date.valueOf(startDate), Date.valueOf(endDate), gerantId);
            actionStatus.setText("Offer created successfully!");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TableOffres.fxml")); // Adjust the path as necessary
            Parent root = loader.load();
            Stage stage = (Stage) actionStatus.getScene().getWindow(); // Assuming actionStatus is part of the current scene
            stage.setScene(new Scene(root));
            stage.show();
        } catch (NumberFormatException e) {
            actionStatus.setText("Invalid number format: " + e.getMessage());
        } catch (SQLException e) {
            actionStatus.setText("Database access error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            actionStatus.setText("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }


       String recipientEmail = "aminebenjebli@gmail.com";
        String subject = "New Offer Added";
        String messageBody = "Dear Client, a new offer has been added to our restaurant. Check it out!";
        emailOffreResto.sendEmail(recipientEmail, subject, messageBody);
    }
    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            Parent adminHomePageParent = FXMLLoader.load(getClass().getResource("/TableOffres.fxml"));
            Scene adminHomePageScene = new Scene(adminHomePageParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(adminHomePageScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


}
