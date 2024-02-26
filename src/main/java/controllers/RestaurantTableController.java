package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.layout.VBox;


import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Gerant;
import services.GerantService;

import java.sql.SQLException;

public class RestaurantTableController {



    @FXML
    private Button backButton;

    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            VBox root = FXMLLoader.load(getClass().getResource("/AdminHomePage.fxml")); // Make sure this path is correct
            Scene scene = backButton.getScene();
            scene.setRoot(root); // Set the VBox as the root
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading FXML file: AdminHomePage.fxml");
        }
    }
    @FXML
    private TableView<Gerant> tableViewGerants;
    @FXML
    private TableColumn<Gerant, String> columnUsername, columnName, columnDescription, columnDocument, columnImage, columnEmail, columnPassword;
    @FXML
    private TableColumn<Gerant, java.sql.Timestamp> columnDate;
    @FXML
    private TableColumn<Gerant, Void> columnActions;

    public void initialize() {
        initializeTableColumns();
        loadGerantsData();
    }

    private void initializeTableColumns() {
        columnUsername.setCellValueFactory(cellData -> cellData.getValue().usernameProperty());
        columnName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        columnDescription.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        columnDocument.setCellValueFactory(cellData -> cellData.getValue().documentProperty());
        columnImage.setCellValueFactory(cellData -> cellData.getValue().imageProperty());
        columnEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        columnPassword.setCellValueFactory(cellData -> cellData.getValue().passwordProperty());
        columnDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        columnActions.setCellFactory(param -> new TableCell<Gerant, Void>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");

            private final HBox pane = new HBox(editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Gerant gerant = getTableView().getItems().get(getIndex());
                    handleEditAction(gerant);
                });
                deleteButton.setOnAction(event -> {
                    Gerant gerant = getTableView().getItems().get(getIndex());
                    handleDeleteAction(gerant.getId());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadGerantsData() {
        GerantService service = new GerantService();
        ObservableList<Gerant> gerantsList = FXCollections.observableArrayList();
        try {
            gerantsList.addAll(service.afficher());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tableViewGerants.setItems(gerantsList);
    }

    private void handleEditAction(Gerant gerant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminModifyRestaurant.fxml"));
            Parent root = loader.load();

            AdminModifyRestaurantController controller = loader.getController();
            controller.setGerant(gerant);

            Stage stage = new Stage();
            stage.setTitle("Edit Gerant");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            loadGerantsData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handleDeleteAction(int id) {
        System.out.println("Deleting gerant with ID: " + id);
        try {
            new GerantService().supprimer(id);
            loadGerantsData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}



