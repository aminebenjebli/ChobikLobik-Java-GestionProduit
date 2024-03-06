package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Plat;
import services.PlatServices;
import utils.SessionManager;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class SelectPlatController {

    @FXML
    private TableView<Plat> tableView;
    public Button logoutButton;
    @FXML
    private TableColumn<Plat, Void> columnActions;
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
    public void initialize() {
        setupActionsColumn(columnActions);
        int currentGerantId = SessionManager.getCurrentGerant().getId();
        try {
            List<Plat> plats = new PlatServices().fetchPlatssByGerant(currentGerantId);
            ObservableList<Plat> platObservableList = FXCollections.observableArrayList(plats);
            tableView.setItems(platObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setupActionsColumn(TableColumn<Plat, Void> columnActions) {
        columnActions.setCellFactory(param -> new TableCell<Plat, Void>() {
            private final Button selectOfferButton = new Button("Select Plat");

            {
                selectOfferButton.setOnAction(event -> {
                    Plat selectedPlat = getTableView().getItems().get(getIndex());
                    selectOfferAction(selectedPlat);
                });
            }

            public void selectOfferAction(Plat plat) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/OffersPlat.fxml")); // Make sure the path is correct
                    Parent root = loader.load();
                    OffersPlatController offersPlatController = loader.getController();
                    offersPlatController.initData(plat);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Add Offer for " + plat.getNom());
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : selectOfferButton);
            }
        });
    }
    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
