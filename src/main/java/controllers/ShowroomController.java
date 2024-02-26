package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
