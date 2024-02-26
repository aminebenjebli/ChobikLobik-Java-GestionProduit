package services;

import models.Subscription;
import utils.MyDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class SubscriptionService {
    private final Connection connection;

    public SubscriptionService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    public void createSubscription(int restaurantId, int subscriptionTypeId) {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusMonths(1);

        String sql = "INSERT INTO abonnement (id_resto, status, date_debut, date_fin, abonnement_type_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, restaurantId);
            preparedStatement.setInt(2, 1);
            preparedStatement.setDate(3, java.sql.Date.valueOf(startDate));
            preparedStatement.setDate(4, java.sql.Date.valueOf(endDate));
            preparedStatement.setInt(5, subscriptionTypeId);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating subscription failed, no rows affected.");
            }
            System.out.println("Subscription added successfully.");
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            // Handle exceptions
        }
    }
    // In SubscriptionService.java
    public void updateExpiredSubscriptions() {
        String sql = "UPDATE abonnement SET status = 0 WHERE date_fin <= CURDATE()";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Expired subscriptions updated successfully.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            // Handle exceptions
        }
    }

}
