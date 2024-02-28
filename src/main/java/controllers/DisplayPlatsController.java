package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import models.Plat;
import services.PlatServices;

import java.sql.SQLException;
import java.util.Stack;

public class DisplayPlatsController {


    @FXML
    private ImageView Logo;
    @FXML
    private TableView<Plat> tableView;
    @FXML
    private TableColumn<Plat, String> nomColumn;
    @FXML
    private TableColumn<Plat, String> categoryColumn;
    @FXML
    private TableColumn<Plat, String> descriptionColumn;
    @FXML
    private TableColumn<Plat, Float> prixColumn;
    @FXML
    private TextField txtSearchName;

    private PlatServices platServices;

    @FXML
    public void initialize() {

        platServices = new PlatServices();

        try {
            ObservableList<Plat> plats = FXCollections.observableArrayList(platServices.fetchAllPlats());
            tableView.setItems(plats);
        } catch (SQLException e) {
            System.out.println("Error fetching plats: " + e.getMessage());
        }

        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        categoryColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getCategory().getType()));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
        // Ajoutez un écouteur sur le champ de texte pour déclencher la recherche à chaque changement
        txtSearchName.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ObservableList<Plat> plats = FXCollections.observableArrayList(platServices.searchPlats(newValue));
                tableView.setItems(plats);
            } catch (SQLException e) {
                System.out.println("Error searching plats by category: " + e.getMessage());
            }
        });
    }

}