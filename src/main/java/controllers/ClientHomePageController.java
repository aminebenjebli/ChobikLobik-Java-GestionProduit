package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import models.Client;
import models.Gerant;
import models.Offre;
import models.Plat;
import services.GerantService;
import services.OffreServices;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import services.PlatServices;


public class ClientHomePageController {
    @FXML
    private HBox offersContainer;
    private OffreServices offreServices = new OffreServices();
    private List<Offre> boostedOffers;
    private int currentPageIndex = 0;
    @FXML
    private HBox restaurantsContainer;
    @FXML
    private Hyperlink viewAllRestaurantsLink;
    private GerantService gerantService = new GerantService();
    @FXML
    private ScrollPane scrollPaneOffers;
    @FXML
    private ImageView logoImage;
    @FXML
    private ImageView profileImage;
    @FXML
    private TextField searchBar;
    @FXML
    private MenuButton profileMenu;
    @FXML
    private VBox searchResultsContainer;
    private PlatServices platServices = new PlatServices(); // Correct spelling
    @FXML
    public void initialize() {
        boostedOffers = offreServices.getBoostedOffers();
        displayOffers();
        setupAutoSwap();
        loadRandomRestaurants();

        scrollPaneOffers.widthProperty().addListener((observable, oldValue, newValue) -> adjustOfferCardSizes());
        adjustOfferCardSizes();
        Image logo = new Image("images/Choubikloubiik.png");
        logoImage.setImage(logo);

        Image profilePic = new Image("images/Choubikloubiik.png");
        profileImage.setImage(profilePic);

        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.trim().isEmpty()) {
                performSearch(newValue);
            } else {
                searchResultsContainer.getChildren().clear(); // Clear results when search bar is cleared
            }
        });

    }
    private void performSearch(String query) {
        searchResultsContainer.getChildren().clear(); // Clear previous search results

        try {
            // Assuming you want to search for plats globally, not just for a specific gerant
            List<Plat> plats = platServices.searchPlatsByName(query);
            List<Gerant> gerants = gerantService.searchGerantsByName(query);

            for (Plat plat : plats) {
                VBox card = createSearchPlatCard(plat);
                searchResultsContainer.getChildren().add(card);
            }

            for (Gerant gerant : gerants) {
                VBox card = createSearchGerantCard(gerant);
                searchResultsContainer.getChildren().add(card);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }

    private VBox createSearchPlatCard(Plat plat) {
        // Create a card for plat search results
        VBox card = new VBox(5); // Adjust spacing
        card.getStyleClass().add("search-result-card");

        // Use the getRestaurantName() to access the name of the restaurant
        String platInfo = plat.getNom() + (plat.getRestaurantName() != null ? " - " + plat.getRestaurantName() : "");
        Label nameLabel = new Label(platInfo);
        nameLabel.getStyleClass().add("search-result-label"); // You might need to define this style class in your CSS


        card.getChildren().add(nameLabel);

        return card;
    }


    private VBox createSearchGerantCard(Gerant gerant) {
        // Create a card for gerant search results
        VBox card = new VBox(5); // Adjust spacing
        card.getStyleClass().add("search-result-card");
        Label nameLabel = new Label(gerant.getName());
        // Add more details as needed
        card.getChildren().add(nameLabel);

        return card;
    }

    private void displayOffers() {
        int startIndex = currentPageIndex * 3;
        int endIndex = Math.min(startIndex + 3, boostedOffers.size());
        offersContainer.getChildren().clear();
        for (int i = startIndex; i < endIndex; i++) {
            offersContainer.getChildren().add(createOfferCard(boostedOffers.get(i)));
        }
        currentPageIndex = endIndex >= boostedOffers.size() ? 0 : currentPageIndex + 1;
        adjustOfferCardSizes();
        scrollPaneOffers.setStyle("-fx-background-color: transparent;");
        scrollPaneOffers.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Never show vertical scrollbar
        scrollPaneOffers.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // Never show horizontal scrollbar
    }


    private VBox createOfferCard(Offre offer) {
        VBox card = new VBox();
        card.getStyleClass().add("card");
        card.getChildren().add(createLabel("Plat: " + offer.getPlatName(), "card-label"));
        card.getChildren().add(createLabel("Discount: " + offer.getPourcentage() + "%", "card-label"));
        card.getChildren().add(createLabel("New Price: " + offer.getNew_price(), "card-label"));
        card.getChildren().add(createLabel("Start: " + offer.getDate_debut().toString(), "card-label"));
        card.getChildren().add(createLabel("End: " + offer.getDate_fin().toString(), "card-label"));
        return card;
    }

    private Label createLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }


    private void setupAutoSwap() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> displayOffers()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void adjustOfferCardSizes() {
        if (!offersContainer.getChildren().isEmpty()) {
            double availableWidth = scrollPaneOffers.getWidth() - 20;
            double cardWidth = availableWidth / 3 - (10 * 2);

            offersContainer.getChildren().forEach(node -> ((VBox) node).setPrefWidth(cardWidth));
        }

        if (!restaurantsContainer.getChildren().isEmpty()) {
            double availableWidth = scrollPaneOffers.getWidth() - 20;
            double cardWidth = availableWidth / 3 - (10 * 2);

            restaurantsContainer.getChildren().forEach(node -> ((VBox) node).setPrefWidth(cardWidth));
        }
    }


    @FXML
    private void handleLogoutAction(ActionEvent event) {
        SessionManager.clearSession();
        try {
            Parent showroomParent = FXMLLoader.load(getClass().getResource("/Showroom.fxml"));
            Scene showroomScene = new Scene(showroomParent);
            Stage window = (Stage) profileMenu.getScene().getWindow(); // Assuming 'profileMenu' is a visible class member
            window.setScene(showroomScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gotoreserve(ActionEvent event) {
        if (SessionManager.getCurrentClient() != null) {
            switchScene("/reservationclient.fxml");
        } else {
            showAlert("Error", "No client is currently logged in.");
        }
    }

    private void switchScene(String fxmlPath) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(fxmlPath));
            Scene scene = new Scene(parent);
            Stage window = (Stage) profileMenu.getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
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


    private void loadRandomRestaurants() {
        try {
            List<Gerant> randomGerants = gerantService.getRandomGerants(3);
            restaurantsContainer.getChildren().clear();
            for (Gerant gerant : randomGerants) {
                VBox gerantCard = createGerantCard(gerant);
                // Set the onMouseClicked event for each card
                gerantCard.setOnMouseClicked(event -> openRestaurantDetails(gerant));
                restaurantsContainer.getChildren().add(gerantCard);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private VBox createGerantCard(Gerant gerant) {
        VBox card = new VBox(10); // Adjust spacing as needed
        card.getStyleClass().add("gerant-card");
        Label nameLabel = new Label(gerant.getName());
        nameLabel.getStyleClass().add("gerant-card-label"); // Apply CSS style for the label
        card.getChildren().add(nameLabel);

        // Make the card clickable
        card.setOnMouseClicked(event -> openRestaurantDetails(gerant));

        return card;
    }

    private void openRestaurantDetails(Gerant gerant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/RestaurantDetails.fxml"));
            Parent root = loader.load();
            RestaurantDetailsController controller = loader.getController();
            controller.setGerant(gerant); // Assuming you have this method in your RestaurantDetailsController
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void handleViewAllRestaurants(ActionEvent event) {
        try {
            Parent adminHomePageParent = FXMLLoader.load(getClass().getResource("/AllRestaurantPage.fxml"));
            Scene adminHomePageScene = new Scene(adminHomePageParent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(adminHomePageScene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleProfileAction(ActionEvent event) {
        try {
            Parent profileParent = FXMLLoader.load(getClass().getResource("/UserProfile.fxml")); // Make sure the path is correct
            Scene profileScene = new Scene(profileParent);

            Stage window = (Stage) profileMenu.getScene().getWindow(); // Assuming 'profileMenu' is a visible class member

            window.setScene(profileScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private WebView mapView;

}
