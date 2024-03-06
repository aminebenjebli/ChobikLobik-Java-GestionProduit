package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import models.Table;
import services.TableService;
import utils.SessionManager;

import java.sql.SQLException;
import java.util.Optional;

public class TableController {

    @FXML
    private TableView<Table> tableView;
    @FXML
    private TableColumn<Table, Integer> columnNombreP;
    @FXML
    private TableColumn<Table, String> columnStatus;
    @FXML
    private TableColumn<Table, Void> columnActions;

    private TableService service = new TableService();

    @FXML
    public void initialize() {
        columnNombreP.setCellValueFactory(new PropertyValueFactory<>("nombre_p"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        setupActionColumn();
        refreshTableList();
    }

    private void setupActionColumn() {
        columnActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button("Modifier");
            private final Button deleteButton = new Button("Supprimer");
            private final HBox pane = new HBox(editButton, deleteButton);

            {
                editButton.setOnAction(event -> {
                    Table table = getTableView().getItems().get(getIndex());
                    handleEditTable(table);
                });
                deleteButton.setOnAction(event -> {
                    Table table = getTableView().getItems().get(getIndex());
                    handleDeleteTable(table);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : pane);
            }
        });
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
        }    }

    @FXML
    private void handleAddTable(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Ajouter Table");
        dialog.setHeaderText("Ajouter une nouvelle table");
        dialog.setContentText("Entrez le nombre de places:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(nombreP -> {
            try {
                int idResto = SessionManager.getCurrentGerant().getId();
                service.addTable(Integer.parseInt(nombreP), "not reserved", idResto);
                refreshTableList();
            } catch (NumberFormatException | SQLException e) {
                // Handle exceptions
            }
        });
    }

    private void handleEditTable(Table table) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(table.getNombre_p()));
        dialog.setTitle("Modifier Table");
        dialog.setHeaderText("Modifier le nombre de places pour la table");
        dialog.setContentText("Entrez le nouveau nombre de places:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(nombreP -> {
            try {
                service.updateTableNombreP(table.getId(), Integer.parseInt(nombreP));
                refreshTableList();
            } catch (NumberFormatException | SQLException e) {
                // Handle exceptions
            }
        });
    }

    private void handleDeleteTable(Table table) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Supprimer cette table?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    service.deleteTable(table.getId());
                    refreshTableList();
                } catch (SQLException e) {
                    // Handle exceptions
                }
            }
        });
    }

    private void refreshTableList() {
        try {
            int idResto = SessionManager.getCurrentGerant().getId();
            tableView.setItems(FXCollections.observableList(service.getTablesByRestoId(idResto)));
        } catch (SQLException e) {
            // Handle exceptions
        }
    }
}
