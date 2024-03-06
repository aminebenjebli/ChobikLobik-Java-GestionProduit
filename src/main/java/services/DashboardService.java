package services;

import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardService {
    private final Connection connection;

    public DashboardService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }
    public void initialize() {
        scheduleMonthlyDonationUpdates();
    }


    public double fetchTotalIncomeForCurrentMonth() throws SQLException {
        // Adjusted SQL to select individual prices and related information
        String detailedSql = "SELECT abonnement.id AS abonnement_id, " +
                "abonnement_type.price AS price, " +
                "abonnement.date_debut AS start_date, " +
                "abonnement.status AS status " +
                "FROM abonnement " +
                "JOIN abonnement_type ON abonnement_type.id = abonnement.abonnement_type_id " +
                "WHERE abonnement.status = 1 " +
                "AND MONTH(abonnement.date_debut) = MONTH(CURDATE()) " +
                "AND YEAR(abonnement.date_debut) = YEAR(CURDATE())";

        System.out.println("Executing detailed query for total income: " + detailedSql);

        try (PreparedStatement stmt = connection.prepareStatement(detailedSql); ResultSet rs = stmt.executeQuery()) {
            double totalIncome = 0;
            boolean dataFound = false;
            while (rs.next()) {
                int abonnementId = rs.getInt("abonnement_id");
                double price = rs.getDouble("price");
                Date startDate = rs.getDate("start_date");
                int status = rs.getInt("status");

                // Log each row to see the details
                System.out.println("Abonnement ID: " + abonnementId + ", Price: " + price + ", Start Date: " + startDate + ", Status: " + status);

                totalIncome += price;
                dataFound = true;
            }

            if (!dataFound) {
                System.out.println("No detailed income data was found for the current month.");
            } else {
                System.out.println("Total detailed income for current month: $" + totalIncome);
            }

            return totalIncome;
        } catch (SQLException e) {
            System.out.println("SQLException occurred while fetching detailed income for current month.");
            e.printStackTrace();
            throw e;
        }
    }


    public void updateMonthlyDonations() throws SQLException {
        double totalIncome = fetchTotalIncomeForCurrentMonth();
        double donationAmount = totalIncome * 0.05; // Calculate donation based on the total income
        LocalDate today = LocalDate.now();

        String checkSql = "SELECT id, total FROM donations WHERE YEAR(date) = ? AND MONTH(date) = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setInt(1, today.getYear());
            checkStmt.setInt(2, today.getMonthValue());

            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                double existingDonation = rs.getDouble("total");
                int donationId = rs.getInt("id");
                // Update only if there's a discrepancy
                if (existingDonation != donationAmount) {
                    String updateSql = "UPDATE donations SET total = ? WHERE id = ?";
                    try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                        updateStmt.setDouble(1, donationAmount);
                        updateStmt.setInt(2, donationId);
                        updateStmt.executeUpdate();
                        System.out.println("Donation record updated for current month.");
                    }
                }
            } else {
                String insertSql = "INSERT INTO donations (date, total) VALUES (?, ?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                    insertStmt.setDate(1, Date.valueOf(today));
                    insertStmt.setDouble(2, donationAmount);
                    insertStmt.executeUpdate();
                    System.out.println("New donation record inserted for current month.");
                }
            }
        } catch (SQLException e) {
            System.out.println("SQLException occurred during updateMonthlyDonations.");
            e.printStackTrace();
            throw e;
        }
    }




    public void scheduleMonthlyDonationUpdates() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Calculate initial delay until the first day of next month
        LocalDate nextMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfNextMonth());
        long initialDelay = ChronoUnit.SECONDS.between(LocalDateTime.now(), nextMonth.atStartOfDay());

        // Schedule the task to run on the first day of each month
        scheduler.scheduleAtFixedRate(() -> {
            try {
                updateMonthlyDonations();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, initialDelay, TimeUnit.DAYS.toSeconds(30), TimeUnit.SECONDS); // You may need to adjust the period for different month lengths
    }




    public int fetchTotalUsers() throws SQLException {
        int totalUsers = 0;

        String clientCountSql = "SELECT COUNT(*) AS user_count FROM client";
        String gerantCountSql = "SELECT COUNT(*) AS user_count FROM gerant";
        String livreurCountSql = "SELECT COUNT(*) AS user_count FROM livreur";

        // Count clients
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(clientCountSql)) {
            if (rs.next()) {
                totalUsers += rs.getInt("user_count");
            }
        }

        // Count gerants
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(gerantCountSql)) {
            if (rs.next()) {
                totalUsers += rs.getInt("user_count");
            }
        }

        // Count livreurs
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(livreurCountSql)) {
            if (rs.next()) {
                totalUsers += rs.getInt("user_count");
            }
        }

        return totalUsers;
    }

}
