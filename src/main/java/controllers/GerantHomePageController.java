package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.AbonnementType;
import models.Feature;
import services.GerantService;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.EventObject;
import java.util.List;


public class GerantHomePageController {
    @FXML
    private HBox subscriptionTypesContainer;


    @FXML
    private void handleSelectBasic(ActionEvent event) {
        System.out.println("Basic subscription selected.");
    }

    @FXML
    private void handleSelectStandard(ActionEvent event) {
        System.out.println("Standard subscription selected.");
    }

    @FXML
    private void handleSelectPremium(ActionEvent event) {
        System.out.println("Premium subscription selected.");
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        SessionManager.clearSession();

        try {
            Parent showroomParent = FXMLLoader.load(getClass().getResource("/Showroom.fxml"));
            Scene showroomScene = new Scene(showroomParent);

            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            window.setScene(showroomScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML private HBox subscriptionCardsContainer;

    @FXML
    public void initialize() {
        try {
            List<AbonnementType> abonnementTypes = new GerantService().getAbonnementTypesWithFeatures();
            for (AbonnementType type : abonnementTypes) {
                VBox card = createSubscriptionCard(type);
                subscriptionCardsContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error (e.g., show an alert to the user)
        }
    }

    private VBox createSubscriptionCard(AbonnementType type) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");

        Label nameLabel = new Label(type.getName());
        nameLabel.getStyleClass().add("title");

        Label priceLabel = new Label("Price: $" + type.getPrice());
        priceLabel.getStyleClass().add("price");

        card.getChildren().addAll(nameLabel, priceLabel);

        for (Feature feature : type.getFeatures()) {
            Label featureLabel = new Label(feature.getFeature());
            featureLabel.getStyleClass().add("feature");
            card.getChildren().add(featureLabel);
        }

        Button selectButton = new Button("Select Offre");
        selectButton.getStyleClass().add("button");
        selectButton.setOnAction(event -> handleSelectOffer(type));

        card.getChildren().add(selectButton);

        return card;
    }

    private int selectedAbonnementTypeId;

    private void handleSelectOffer(AbonnementType type) {
        selectedAbonnementTypeId = type.getId();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaiementGerantPage.fxml"));
            Parent paymentPageParent = loader.load();
            PaiementGerantController paymentController = loader.getController();
            paymentController.setSelectedOfferPrice(type.getPrice());
            paymentController.setSelectedAbonnementTypeId(selectedAbonnementTypeId);
            Stage stage = (Stage) subscriptionCardsContainer.getScene().getWindow();
            stage.setScene(new Scene(paymentPageParent));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


