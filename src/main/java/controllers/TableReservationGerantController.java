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
import javafx.stage.Stage;
import models.Reservation;
import services.ReservationService;
import utils.SessionManager;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TableReservationGerantController {
    @FXML private TableView<Reservation> reservationTableView;
    @FXML private TableColumn<Reservation, String> clientNameColumn;
    @FXML private TableColumn<Reservation, String> clientSurnameColumn;
    @FXML private TableColumn<Reservation, Integer> tableNumberColumn;
    @FXML private TableColumn<Reservation, LocalDate> reservationDateColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;
    @FXML private TableColumn<Reservation, Void> actionColumn;

    private final ReservationService reservationService = new ReservationService();

    @FXML
    public void initialize() {
        clientNameColumn.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        clientSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("clientSurname"));
        tableNumberColumn.setCellValueFactory(new PropertyValueFactory<>("idTable"));
        reservationDateColumn.setCellValueFactory(new PropertyValueFactory<>("dateReservation"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Reservation selectedReservation = getTableView().getItems().get(getIndex());
                    deleteReservation(selectedReservation);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });

        loadReservations();
    }

    private void loadReservations() {
        try {
            int gerantId = SessionManager.getCurrentGerant().getId(); // This assumes SessionManager can give us the current gerant's ID
            List<Reservation> reservations = reservationService.getReservationsForGerant(gerantId);
            reservationTableView.setItems(FXCollections.observableArrayList(reservations));
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load reservations.");
        }
    }

    private void deleteReservation(Reservation reservation) {
        try {
            int gerantId = SessionManager.getCurrentGerant().getId(); // Retrieve gerant ID again in case it's needed for deletion logic
            reservationService.cancelReservation(reservation.getId(), gerantId); // Pass gerantId to cancelReservation
            loadReservations(); // Refresh the list after deletion
            showAlert("Success", "Reservation cancelled successfully.");
        } catch (SQLException e) {
            showAlert("Database Error", "Error cancelling the reservation.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void handleBackAction(ActionEvent event) {
        try {
            Parent gerantHomePageParent = FXMLLoader.load(getClass().getResource("/dashboard_restaurant.fxml"));
            Scene gerantHomePageScene = new Scene(gerantHomePageParent);

            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(gerantHomePageScene);
            window.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
