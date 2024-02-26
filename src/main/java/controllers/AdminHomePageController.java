package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.SessionManager;

import java.io.IOException;



public class AdminHomePageController {


    @FXML
    private ImageView logoImage;

    public void initialize() {
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/Choubikloubik.png"));
            logoImage.setImage(logo);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading image.");
        }
    }

    public void handleClientTable(ActionEvent event) {
        loadScene("ClientTable.fxml", event);
    }

    public void handleRestaurantTable(ActionEvent event) {
        loadScene("RestaurantTable.fxml", event);
    }
    public void handleLivreurTable(ActionEvent event) {
        loadScene("LivreurTable.fxml", event);
    }

    private void loadScene(String fxmlFile, ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/" + fxmlFile));
            Stage stage = (Stage) ((MenuItem)event.getSource()).getParentPopup().getOwnerWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: " + fxmlFile);
        }
    }



    @FXML
    private void handleLogoutAction(MouseEvent event) {
        SessionManager.clearSession();

        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/Showroom.fxml"));
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}
