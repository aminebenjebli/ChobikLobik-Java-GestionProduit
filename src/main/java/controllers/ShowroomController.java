package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.event.ActionEvent;

import java.io.IOException;

public class ShowroomController {

    @FXML
    private void handleSignup(ActionEvent event) {
        navigateTo(event, "/main_signup.fxml");
    }

    @FXML
    private void handleSignin(ActionEvent event) {
        navigateTo(event, "/Login.fxml");
    }
    @FXML
    private ImageView logoImageView;

    @FXML
    public void initialize() {
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/Choubikloubiik.png"));
            logoImageView.setImage(image);
        } catch (NullPointerException e) {
            // Handle the case where the image is not found
            System.out.println("Image not found");
        }
    }


    private void navigateTo(ActionEvent event, String fxmlPath) {
        try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
