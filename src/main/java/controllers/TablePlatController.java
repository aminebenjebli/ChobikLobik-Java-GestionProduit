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
import models.Client;
import models.Plat;
import services.PlatServices;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TablePlatController {
    public Button logoutButton;
    @FXML
    private TextField txtSearchName;

    @FXML
    private TableColumn<Plat, Void> columnActions;

    public Button addPlatButton;

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
    private void handleAddPlatAction() {
        try {
            Parent addPlatParent = FXMLLoader.load(getClass().getResource("/AjouterPlats.fxml"));
            Scene addPlatScene = new Scene(addPlatParent);

            Stage window = (Stage) addPlatButton.getScene().getWindow();
            window.setScene(addPlatScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private PlatServices platServices;
    @FXML
    private TableView<Plat> tableView;

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
        this.platServices = new PlatServices();
        this.platServices = new PlatServices();
        setupActionsColumn(columnActions);
        try {
            List<Plat> plats = platServices.fetchPlatssByGerant(currentGerantId);
            ObservableList<Plat> platObservableList = FXCollections.observableArrayList(plats);
            tableView.setItems(platObservableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        txtSearchName.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ObservableList<Plat> plats = FXCollections.observableArrayList(platServices.searchPlatsByName(newValue));
                tableView.setItems(plats);
            } catch (SQLException e) {
                System.out.println("Error searching plats by category: " + e.getMessage());
            }
        });
    }

    private void setupActionsColumn(TableColumn<Plat, Void> columnActions) {
        columnActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox hbox = new HBox(10, editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Plat plat = getTableView().getItems().get(getIndex());
                    handleEditAction(plat);
                });
                deleteButton.setOnAction(event -> {
                    Plat plat = getTableView().getItems().get(getIndex());
                    handleDeleteAction(plat);
                });
            }

            private void handleEditAction(Plat plat) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditPlat.fxml"));
                    Parent parent = loader.load();

                    EditPlatController controller = loader.getController();
                    controller.initializeForm(plat, TablePlatController.this);

                    Stage stage = new Stage();
                    stage.setScene(new Scene(parent));
                    stage.setTitle("Edit Plat");
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            private void handleDeleteAction(Plat plat) {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation de suppression");
                confirmationAlert.setHeaderText(null);
                confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer ce plat ?");

                Optional<ButtonType> action = confirmationAlert.showAndWait();

                if (action.isPresent() && action.get() == ButtonType.OK) {
                    try {
                        new PlatServices().supprimer(plat.getIdPlat()); // Assuming PlatServices has a method `supprimer(int id)`
                        tableView.getItems().remove(plat); // Remove the plat from the TableView
                        tableView.refresh(); // Refresh the TableView to show updated list
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.setTitle("Erreur");
                        errorAlert.setHeaderText(null);
                        errorAlert.setContentText("Problème lors de la suppression du plat.");
                        errorAlert.showAndWait();
                    }
                }
            }


            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    public void updateTableView(Plat updatedPlat) {
        ObservableList<Plat> plats = tableView.getItems();
        for (int i = 0; i < plats.size(); i++) {
            if (plats.get(i).getIdPlat() == updatedPlat.getIdPlat()) {
                plats.set(i, updatedPlat);
                tableView.refresh();
                return;
            }
        }
        plats.add(updatedPlat);
        tableView.refresh();
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
}
