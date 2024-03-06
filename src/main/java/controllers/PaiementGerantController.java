package controllers;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.SubscriptionService;
import utils.SessionManager;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PaiementGerantController {

    @FXML private TextField cardNumberField, expDateField, cvcField;
    @FXML private Label cardNumberError, expDateError, cvcError;
    @FXML private Button payButton;

    private double selectedOfferPrice =  2000; // This should be set based on the subscription selected

    public PaiementGerantController() {
        Stripe.apiKey = "sk_test_51NDzz7BQRNc8ucGL4wXekcQDnGPdEPmJBNQxiQWzlgQl74PueJ8A9E8BjMJXnQahcafVDFI27bDaXIrrxjA2fghm000WDPqShJ";
    }

    @FXML
    public void initialize() {
        setupValidationListeners();
    }

    private void setupValidationListeners() {
        cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                cardNumberError.setVisible(false);
            } else {
                validateCardNumber();
            }
        });

        expDateField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                expDateError.setVisible(false);
            } else {
                if (!newValue.matches("\\d{0,2}/?\\d{0,2}")) {
                    expDateField.setText(oldValue);
                } else if (newValue.matches("\\d{2}") && !oldValue.endsWith("/")) {
                    expDateField.setText(newValue + "/");
                }
                validateExpirationDate();
            }
        });

        cvcField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                cvcError.setVisible(false);
            } else {
                validateCVC();
            }
        });
    }

    private void validateCardNumber() {
        boolean isValid = cardNumberField.getText().matches("^(4|5)\\d{15}$");
        cardNumberError.setVisible(!isValid);
        cardNumberError.setText("Invalid Card Number");
        updatePayButtonState();
    }

    private void validateExpirationDate() {
        boolean isValid = false;
        String date = expDateField.getText();
        if (date.matches("\\d{2}/\\d{2}")) {
            try {
                YearMonth ym = YearMonth.parse("20" + date.substring(3) + "-" + date.substring(0,  2), DateTimeFormatter.ofPattern("yyyy-MM"));
                isValid = !ym.isBefore(YearMonth.now());
            } catch (DateTimeParseException e) {
                isValid = false;
            }
        }
        expDateError.setVisible(!isValid && !date.isEmpty());
        expDateError.setText("Invalid Date");
        updatePayButtonState();
    }

    private void validateCVC() {
        boolean isValid = cvcField.getText().matches("\\d{3}");
        cvcError.setVisible(!isValid);
        cvcError.setText("Invalid CVC");
        updatePayButtonState();
    }

    private void updatePayButtonState() {
        boolean allValid = !cardNumberError.isVisible() && !expDateError.isVisible() && !cvcError.isVisible()
                && !cardNumberField.getText().isEmpty()
                && !expDateField.getText().isEmpty()
                && !cvcField.getText().isEmpty();
        payButton.setDisable(!allValid);
    }
    private int selectedAbonnementTypeId;
    @FXML public void setSelectedAbonnementTypeId(int abonnementTypeId) {
        this.selectedAbonnementTypeId = abonnementTypeId;
    }
    @FXML
    private void handlePayment(ActionEvent event) {
        try {
            PaymentIntent paymentIntent = createPaymentIntent();
            System.out.println("Payment Intent ID: " + paymentIntent.getId());

            SubscriptionService subscriptionService = new SubscriptionService();
            int restaurantId = SessionManager.getCurrentGerant().getId();
            subscriptionService.createSubscription(restaurantId, this.selectedAbonnementTypeId);

            showPaymentSuccessAlert();
            redirectToDashboard();
        } catch (StripeException e) {
            showPaymentFailedAlert(e.getMessage());
        }
    }

    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/GerantHomePage.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) payButton.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: GerantHomePage.fxml");
        }
    }

    public void setSelectedOfferPrice(double price) {
        this.selectedOfferPrice = price;
        updatePayButtonText();
    }

    private void updatePayButtonText() {
        payButton.setText(String.format("Pay $%.2f", selectedOfferPrice));
    }

    private PaymentIntent createPaymentIntent() throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) (selectedOfferPrice *  100)) // Stripe expects amount in cents
                .setCurrency("usd")
                .build();

        return PaymentIntent.create(params);
    }



    private void showPaymentSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Payment Successful");
        alert.setContentText("Your payment was successful.");
        alert.showAndWait();
    }

    private void showPaymentFailedAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Payment Failed");
        alert.setContentText("Payment failed: " + errorMessage);
        alert.showAndWait();
    }

    private void redirectToDashboard() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/dashboard_restaurant.fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) payButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error redirecting to dashboard");
        }
    }
}