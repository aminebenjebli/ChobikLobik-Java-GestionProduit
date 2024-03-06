package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import models.Gerant;
import services.GerantService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;


public class AllRestaurantPageController {

    @FXML
    private VBox cardsContainer;
    @FXML
    private FlowPane restaurantCardsContainer;
    private GerantService gerantService = new GerantService();

    @FXML
    public void initialize() {
        loadRestaurants();
    }

    private void loadRestaurants() {
        try {
            List<Gerant> gerants = gerantService.getAllGerants(); // Make sure this method correctly fetches all Gerants.
            for (Gerant gerant : gerants) {
                VBox card = createGerantCard(gerant);
                restaurantCardsContainer.getChildren().add(card); // Use restaurantCardsContainer here.
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Consider showing an error message to the user.
        }
    }


    private VBox createGerantCard(Gerant gerant) {
        VBox card = new VBox(5);
        card.getStyleClass().add("gerant-card"); // Ensure you have this style class defined in your CSS
        Label nameLabel = new Label(gerant.getName());
        // You might want to add an ImageView for the restaurant image here as well
        card.getChildren().add(nameLabel);

        // Set a mouse click event on the card
        card.setOnMouseClicked(event -> showRestaurantDetails(gerant));

        return card;
    }

    private void showRestaurantDetails(Gerant gerant) {
        try {
            System.out.println("Opening restaurant details for Gerant ID: " + gerant.getId());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RestaurantDetails.fxml"));
            Parent root = loader.load();

            RestaurantDetailsController controller = loader.getController();
            controller.setGerant(gerant); // Pass the selected Gerant to the details controller

            Stage stage = new Stage(); // Create a new stage or use an existing one
            stage.setScene(new Scene(root));
            stage.setTitle("Restaurant Details"); // Optional
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exceptions (e.g., file not found)
        }
    }



    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            Parent adminHomePageParent = FXMLLoader.load(getClass().getResource("/ClientHomePage.fxml"));
            Scene adminHomePageScene = new Scene(adminHomePageParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();

            window.setScene(adminHomePageScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
