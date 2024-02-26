package controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Livreur;
import services.LivreurService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class LivreurTableController {

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
    private TableView<Livreur> livreurTable;
    @FXML
    private TableColumn<Livreur, String> colNom, colPrenom, colEmail, colAdresse, colVehicule, colZoneLivraison;
    @FXML
    private TableColumn<Livreur, Integer> colNumTel;

    private LivreurService service = new LivreurService();
    @FXML
    private TableColumn<Livreur, Void> colAction;

    @FXML
    public void initialize() {
        setupColumns();
        loadLivreurData();
        addActionButtonsToTable();

    }
    private void addActionButtonsToTable() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private final Button btnModify = new Button("Modifier");
            private final Button btnDelete = new Button("Supprimer");
            private final HBox pane = new HBox(btnModify, btnDelete);

            {
                btnModify.setOnAction(event -> {
                    Livreur livreur = getTableView().getItems().get(getIndex());
                    handleModifyAction(livreur);
                });
                btnDelete.setOnAction(event -> {
                    Livreur livreur = getTableView().getItems().get(getIndex());
                    handleDeleteAction(livreur);
                });
                pane.setSpacing(10);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
    }


    private void handleDeleteAction(Livreur livreur) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + livreur.getNom() + "?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            try {
                service.deleteLivreur(livreur);
                livreurTable.getItems().remove(livreur);
                loadLivreurData();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    private void setupColumns() {
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colAdresse.setCellValueFactory(new PropertyValueFactory<>("adresse"));
        colVehicule.setCellValueFactory(new PropertyValueFactory<>("vehiculeName"));
        colZoneLivraison.setCellValueFactory(new PropertyValueFactory<>("zoneLivraisonName"));
        colNumTel.setCellValueFactory(new PropertyValueFactory<>("numTel"));
    }

    private void loadLivreurData() {
        ObservableList<Livreur> livreurData = FXCollections.observableArrayList();
        try {
            var livreurs = service.afficher();
            for (Livreur livreur : livreurs) {
                livreur.setVehiculeName(service.findVehiculeNameById(livreur.getIdVehicule()));
                livreur.setZoneLivraisonName(service.findZoneLivraisonNameById(livreur.getIdZoneLivraison()));
                livreurData.add(livreur);
            }
            livreurTable.setItems(livreurData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private void handleModifyAction(Livreur livreur) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminModifierLivreur.fxml"));
            Parent root = loader.load();

            AdminModifierLivreurController controller = loader.getController();
            controller.setLivreur(livreur);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier Livreur");
            stage.showAndWait();

            loadLivreurData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





}
