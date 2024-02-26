package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button; // Import statement for Button
import javafx.stage.Stage;

public class MainSignupController {

    @FXML
    private Button btnClientSignup;

    @FXML
    private Button btnLivreurSignup;

    @FXML
    private void navigateToClientSignup() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/SignUpClient.fxml"));
        Stage stage = (Stage) btnClientSignup.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void navigateToLivreurSignup() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/LivreurSignup.fxml"));
        Stage stage = (Stage) btnLivreurSignup.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
    @FXML
    private void navigateToGerantSignup() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/GerantSignup.fxml"));
        Stage stage = (Stage) btnLivreurSignup.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}
