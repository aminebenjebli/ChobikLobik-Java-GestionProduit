package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.Client;
import java.io.IOException;
import javafx.scene.control.cell.PropertyValueFactory;
import services.Clientservices;
import java.sql.SQLException;
import java.util.List;

public class ClientTableController {

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
    private TableView<Client> tableViewClients;
    @FXML
    private TableColumn<Client, Integer> columnId;
    @FXML
    private TableColumn<Client, String> columnNom, columnPrenom, columnEmail, columnUsername, columnAdresse;
    @FXML
    private TableColumn<Client, Integer> columnNumTel;
    @FXML
    private TableColumn<Client, Void> columnActions;

    private Clientservices clientService = new Clientservices();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadClientsData();
    }

    private void setupTableColumns() {
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        columnPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        columnAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        columnNumTel.setCellValueFactory(new PropertyValueFactory<>("numTel"));
        columnActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox pane = new HBox(editButton, deleteButton);
            {
                editButton.setOnAction(event -> {
                    Client client = getTableView().getItems().get(getIndex());
                    openClientModification(client, event);
                });
                deleteButton.setOnAction(event -> {
                    Client client = getTableView().getItems().get(getIndex());
                    try {
                        clientService.supprimer(client.getId());
                        loadClientsData();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }

    private void loadClientsData() {
        try {
            List<Client> clientsList = clientService.afficher();
            ObservableList<Client> observableList = FXCollections.observableArrayList(clientsList);
            tableViewClients.setItems(observableList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void openClientModification(Client client, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminModifyClient.fxml"));
            Parent root = loader.load();
            AdminModifyClientController controller = loader.getController();
            controller.setClient(client);
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
