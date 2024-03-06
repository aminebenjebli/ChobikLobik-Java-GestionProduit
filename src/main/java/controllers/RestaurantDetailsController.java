package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import models.Gerant;
import models.Offre;
import models.Plat;
import services.OffreServices;
import services.PlatServices;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class RestaurantDetailsController {

    @FXML
    private VBox cardsContainer;
    private OffreServices offreServices = new OffreServices();

    private Gerant gerant;

    public void setGerant(Gerant gerant) {
        this.gerant = gerant;
        loadOffers();
        loadPlats();

    }
    @FXML
    private void handleReserveTable(ActionEvent event) {
        System.out.println("Reserve Table button clicked for Gerant ID: " + gerant.getId());

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/reservationclient.fxml"));
            Parent root = loader.load();

            ReservationClientController controller = loader.getController();
            controller.setGerantId(gerant.getId()); // Pass the gerantId to the ReservationClientController

            Stage stage = new Stage();
            stage.setTitle("Reserve Table for " + gerant.getName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadOffers() {
        List<Offre> offers = offreServices.getOffersForGerant(gerant.getId());
        for (Offre offer : offers) {
            VBox offerCard = createOfferCard(offer);
            cardsContainer.getChildren().add(offerCard);
        }
    }

    private VBox createOfferCard(Offre offer) {
        VBox card = new VBox(5);
        Label offerLabel = new Label("Offer on " + offer.getPlatName() + ": " + offer.getPourcentage() + "% off");
        Label newPriceLabel = new Label("New Price: $" + offer.getNew_price());
        Label validLabel = new Label("Valid: " + offer.getDate_debut() + " to " + offer.getDate_fin());

        card.getChildren().addAll(offerLabel, newPriceLabel, validLabel);
        card.getStyleClass().add("offer-card"); // Make sure to define this style in your CSS

        return card;
    }

    private void loadPlats() {
        PlatServices service = new PlatServices();
        try {
            List<Plat> plats = service.fetchPlatsByGerant(gerant.getId());
            HBox row = new HBox(10);
            int counter = 0;
            for (Plat plat : plats) {
                if (counter % 5 == 0) {
                    row = new HBox(10);
                    cardsContainer.getChildren().add(row);
                }
                VBox card = createPlatCard(plat);
                row.getChildren().add(card);
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle error
        }
    }

    private VBox createPlatCard(Plat plat) {
        VBox card = new VBox(5);
        card.setPrefWidth(120); // Set preferred width for the card
        card.getStyleClass().add("plat-card"); // Ensure this style class is defined in your CSS

        ImageView imageView = new ImageView();
        String imagePath = plat.getImage();
        if (!imagePath.toLowerCase().startsWith("http")) {
            imagePath = "file:///" + imagePath.replace("\\", "/");
        }
        imageView.setImage(new Image(imagePath));
        imageView.setFitWidth(100);
        imageView.setPreserveRatio(true);

        Label nameLabel = new Label(plat.getNom());
        Label priceLabel = new Label(String.format("$%.2f", plat.getPrix()));

        card.getChildren().addAll(imageView, nameLabel, priceLabel);
        return card;
    }


    @FXML
    private void initialize() {
        // Initialization code can go here
    }
}
