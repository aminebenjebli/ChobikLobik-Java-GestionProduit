package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import models.Plat;
import utils.SessionManager;

import java.io.IOException;
import java.util.Objects;

public class DashboardRestaurantController {
    public Label logoutLabel;
    public ImageView logoImage;
    @FXML
    private Pane paneGerant;
    @FXML
    private Pane paneGerant1;
    @FXML
    private Pane paneGerant11;
    @FXML
    private MenuItem Plat;
    @FXML
    private MenuItem Offres;
    @FXML
    private MenuItem category;
    @FXML
    private MenuItem reservations;

    @FXML
    private Pane paneReservation;
    @FXML
    private Pane paneTables;

    @FXML
    private MenuItem tables;



    void loadPlat(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TablePlat.fxml"));
            Pane paneResto = loader.load();
            paneGerant.getChildren().clear();
            paneGerant.getChildren().setAll(paneResto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void loadOffres(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TableOffres.fxml"));
            Pane paneResto = loader.load();
            paneGerant1.getChildren().clear();
            paneGerant1.getChildren().setAll(paneResto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void loadCategories(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCat.fxml"));
            Pane paneResto = loader.load();
            paneGerant11.getChildren().clear();
            paneGerant11.getChildren().setAll(paneResto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void loadReservation(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/TableReservationGerant.fxml"));
            Pane paneResto = loader.load();
            paneReservation.getChildren().clear();
            paneReservation.getChildren().setAll(paneResto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    void loadTable(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tableTable.fxml"));
            Pane paneResto = loader.load();
            paneTables.getChildren().clear();
            paneTables.getChildren().setAll(paneResto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    private void handleLogoutAction(MouseEvent event) {
        SessionManager.clearSession();

        try {
            Parent showroomParent = FXMLLoader.load(getClass().getResource("/Showroom.fxml"));
            Scene showroomScene = new Scene(showroomParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(showroomScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openTable(ActionEvent event) {
        if (event.getSource()== Offres) {
            paneGerant1.setStyle("-fx-background-color: #FFFFFF");
            paneGerant1.toFront();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableOffres.fxml"));
            paneGerant1.getChildren().removeAll();
            loadOffres();
            
        } else if (event.getSource()== Plat){
        paneGerant.setStyle("-fx-background-color: #FFFFFF");
        paneGerant.toFront();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TablePlat.fxml"));
        paneGerant.getChildren().removeAll();
        loadPlat();
            
        }else if (event.getSource()== category) {
            paneGerant11.setStyle("-fx-background-color: #FFFFFF");
            paneGerant11.toFront();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AfficherCat.fxml"));
            paneGerant11.getChildren().removeAll();
            loadCategories();
        //loadScene("TablePlat.fxml", event);
    } else if (event.getSource()== reservations) {
            paneReservation.setStyle("-fx-background-color: #FFFFFF");
            paneReservation.toFront();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TableReservationGerant.fxml"));
            paneReservation.getChildren().removeAll();
            loadReservation();
            
        }else if (event.getSource()== tables) {
            paneTables.setStyle("-fx-background-color: #FFFFFF");
            paneTables.toFront();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tableTable.fxml"));
            paneTables.getChildren().removeAll();
            loadTable();

        }
    }



    public void openTableCommandes(ActionEvent event) {
        loadScene("TableCommandes.fxml", event);
    }


    private void loadScene(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/" + fxmlFile)));
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: " + fxmlFile);
        }
    }

    @FXML
    public void initialize() {
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/Choubikloubiik.png"));
            logoImage.setImage(image);
        } catch (NullPointerException e) {
            System.out.println("Image not found");
        }
    }

    public void openTablegerant(ActionEvent event) {
        loadScene("AfficherCat.fxml", event);
    }




}
