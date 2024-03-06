package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import models.Offre;
import services.OffreServices;
import services.SubscriptionService;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;


public class TableOffresController {
    @FXML
    private VBox offersContainer;
    @FXML
    private Button addOfferButton;

    private OffreServices offreServices = new OffreServices();
    private SubscriptionService subscriptionService = new SubscriptionService(); // Assuming SubscriptionService has the required methods

    @FXML
    private void handleAddOfferAction(ActionEvent event) {
        try {
            Parent selectPlatParent = FXMLLoader.load(getClass().getResource("/SelectPlat.fxml")); // Update the path to your FXML file
            Scene selectPlatScene = new Scene(selectPlatParent);

            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();

            window.setScene(selectPlatScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void initialize() {
        loadOffers();
        updateAddOfferButtonStatus();

    }
    private void updateAddOfferButtonStatus() {
        int gerantId = SessionManager.getCurrentGerant().getId();
        try {
            if (!subscriptionService.canCreateOffer(gerantId)) {
                addOfferButton.setDisable(true);
                addOfferButton.setText("Offer Limit Reached"); // Update button text to show status
            } else {
                addOfferButton.setDisable(false);
                addOfferButton.setText("Add Offer"); // Reset button text to default
            }
        } catch (SQLException e) {
            showAlert("Database access error: " + e.getMessage());
            e.printStackTrace();
        }
    }



    private void loadOffers() {
        offersContainer.getChildren().clear();
        int idResto = SessionManager.getCurrentGerant().getId();
        List<Offre> offers = offreServices.getOffersForGerant(idResto);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Offre offer : offers) {
            VBox offerCard = new VBox(10);
            offerCard.setPadding(new Insets(15));
            offerCard.setStyle("-fx-border-style: solid inside; -fx-border-width: 2; -fx-border-insets: 5; -fx-border-radius: 5; -fx-border-color: blue;");

            Text offerDetails = new Text(String.format("Plat name: %s, Discount: %d%%, New Price: %.2f, Start: %s, End: %s",
                    offer.getPlatName(), offer.getPourcentage(), offer.getNew_price(), sdf.format(offer.getDate_debut()), sdf.format(offer.getDate_fin())));

            HBox buttonBox = new HBox(10);
            Button btnModifier = new Button("Modifier");
            Button btnSupprimer = new Button("Supprimer");
            Button btnBooster = new Button("Boost");

            final int offerId = offer.getId(); // Use final or effectively final variable inside lambda
            final int subscriptionTypeId = getSubscriptionTypeIdForCurrentGerant();

            btnBooster.setVisible(subscriptionTypeId == 2 || subscriptionTypeId == 3);
            btnBooster.setDisable(!canBoostOfferWrapper(idResto, subscriptionTypeId));

            btnBooster.setOnAction(event -> boostOfferWrapper(offerId, subscriptionTypeId));
            btnModifier.setOnAction(event -> handleModifyOffer(offer));
            btnSupprimer.setOnAction(event -> handleDeleteOffer(offer.getId()));

            buttonBox.getChildren().addAll(btnModifier, btnSupprimer, btnBooster);
            offerCard.getChildren().addAll(offerDetails, buttonBox);

            offersContainer.getChildren().add(offerCard);
        }
    }

    private boolean canBoostOfferWrapper(int gerantId, int subscriptionTypeId) {
        try {
            return offreServices.canBoostOffer(gerantId, subscriptionTypeId);
        } catch (SQLException e) {
            showAlert("Failed to check offer boost eligibility: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void boostOfferWrapper(int offerId, int subscriptionTypeId) {
        handleBoostOffer(offerId, subscriptionTypeId);
    }

    private int getSubscriptionTypeIdForCurrentGerant() {
        try {
            return subscriptionService.getSubscriptionTypeId(SessionManager.getCurrentGerant().getId());
        } catch (SQLException e) {
            showAlert("Database access error: " + e.getMessage());
            e.printStackTrace();
            return 0; // Default or error value
        }
    }


    private void handleBoostOffer(int offerId, int subscriptionTypeId) {
        int gerantId = SessionManager.getCurrentGerant().getId();
        try {
            offreServices.boostOffer(gerantId, offerId, subscriptionTypeId);
            showAlert("Offer boosted successfully!");
            loadOffers(); // Reload offers to update the UI
        } catch (SQLException e) {
            showAlert("Failed to boost offer: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void handleModifyOffer(Offre offer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditOffer.fxml"));
            Parent root = loader.load();

            EditOfferController controller = loader.getController();
            controller.initData(offer, this::loadOffers); // Correctly use initData instead of setOffer

            Stage stage = new Stage();
            stage.setTitle("Edit Offer");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadOffers(); // Refresh the offers list
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error loading the edit offer view.");
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void handleDeleteOffer(int offerId) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this offer?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                offreServices.deleteOffer(offerId);
                loadOffers(); // Refresh offers list
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            Parent adminHomePageParent = FXMLLoader.load(getClass().getResource("/dashboard_restaurant.fxml"));
            Scene adminHomePageScene = new Scene(adminHomePageParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(adminHomePageScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
