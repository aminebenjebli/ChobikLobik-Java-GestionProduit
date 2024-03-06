package services;

import models.Reservation;
import models.Table;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationService {

    public List<Table> getAvailableTables(int idResto) throws SQLException {
        List<Table> availableTables = new ArrayList<>();
        String query = "SELECT * FROM `table` WHERE status = 'not reserved' AND id_resto = ?";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idResto);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                availableTables.add(new Table(
                        rs.getInt("id"),
                        rs.getInt("nombre_p"),
                        rs.getString("status"),
                        idResto
                ));
            }
        }
        return availableTables;
    }

    public void createReservation(int idClient, int idTable, int idResto, LocalDate dateReservation) throws SQLException {
        String reservationQuery = "INSERT INTO reservation (id_client, id_restaurant, id_table, date_reservation) VALUES (?, ?, ?, ?)";
        String updateTableQuery = "UPDATE `table` SET status = 'reserved' WHERE id = ? AND id_resto = ?";

        Connection conn = MyDatabase.getInstance().getConnection();

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement reservationStmt = conn.prepareStatement(reservationQuery)) {
                reservationStmt.setInt(1, idClient);
                reservationStmt.setInt(2, idResto); // Fixed to include idResto
                reservationStmt.setInt(3, idTable);
                reservationStmt.setDate(4, Date.valueOf(dateReservation));
                reservationStmt.executeUpdate();
            }

            try (PreparedStatement updateTableStmt = conn.prepareStatement(updateTableQuery)) {
                updateTableStmt.setInt(1, idTable);
                updateTableStmt.setInt(2, idResto); // Ensure the table update is for the correct restaurant
                updateTableStmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<Reservation> getReservationsForGerant(int idResto) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.id, r.id_client, r.id_restaurant, r.id_table, r.date_reservation, " +
                "c.nom AS client_name, c.prenom AS client_surname, t.status " +
                "FROM reservation r " +
                "JOIN client c ON r.id_client = c.id " +
                "JOIN `table` t ON r.id_table = t.id " +
                "WHERE r.id_restaurant = ? AND t.status = 'reserved'";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idResto);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("id"),
                        rs.getInt("id_client"),
                        rs.getInt("id_restaurant"),
                        rs.getInt("id_table"),
                        rs.getDate("date_reservation").toLocalDate(),
                        rs.getString("client_name"),
                        rs.getString("client_surname"),
                        rs.getString("status")
                );
                reservations.add(reservation);
            }
        }
        return reservations;
    }

    public void cancelReservation(int reservationId, int idResto) throws SQLException {
        String updateTableQuery = "UPDATE `table` t JOIN reservation r ON t.id = r.id_table " +
                "SET t.status = 'not reserved' WHERE r.id = ? AND r.id_restaurant = ?";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateTableQuery)) {

            stmt.setInt(1, reservationId);
            stmt.setInt(2, idResto); // Correctly cancels the reservation for the specified restaurant
            stmt.executeUpdate();
        }
    }
}
