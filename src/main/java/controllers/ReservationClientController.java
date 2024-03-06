package controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import models.Table;
import services.ReservationService;
import utils.SessionManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReservationClientController {

    @FXML private ComboBox<Table> tableComboBox;
    @FXML private DatePicker datePicker;
    private final ReservationService reservationService = new ReservationService();
    private int gerantId;

    @FXML
    public void initialize() {
        tableComboBox.setCellFactory(lv -> new ListCell<Table>() {
            @Override
            protected void updateItem(Table table, boolean empty) {
                super.updateItem(table, empty);
                setText(empty ? null : "Table Number " + table.getId() + " : " + table.getNombre_p());
            }
        });
        tableComboBox.setButtonCell(new ListCell<Table>() {
            @Override
            protected void updateItem(Table item, boolean empty) {
                super.updateItem(item, empty);
                setText((item == null || empty) ? "" : "Table Number " + item.getId() + " for " + item.getNombre_p() + " people");
            }
        });
    }

    public void setGerantId(int gerantId) {
        this.gerantId = gerantId;
        loadAvailableTables();
    }

    private void loadAvailableTables() {
        try {
            List<Table> availableTables = reservationService.getAvailableTables(this.gerantId);
            tableComboBox.setItems(FXCollections.observableArrayList(availableTables));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load available tables.");
        }
    }

    @FXML
    private void handleReservation(ActionEvent event) {
        int clientId = SessionManager.getCurrentClient().getId();
        Table selectedTable = tableComboBox.getValue();
        LocalDate reservationDate = datePicker.getValue();

        if (selectedTable != null && reservationDate != null) {
            try {
                reservationService.createReservation(clientId, selectedTable.getId(), this.gerantId, reservationDate);
                showAlert(Alert.AlertType.INFORMATION, "Reservation Confirmed", "Your reservation for table " + selectedTable.getId() + " on " + reservationDate + " has been confirmed.");
                loadAvailableTables();
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Reservation Error", "There was an error processing your reservation: " + e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Missing Information", "Please select a table and a date for your reservation.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleGoBack(ActionEvent event) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("/ClientHomePage.fxml"));
            Scene scene = new Scene(parent);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(scene);
            window.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
