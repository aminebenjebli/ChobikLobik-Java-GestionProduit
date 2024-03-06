package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Offre;
import utils.MyDatabase;
import utils.SessionManager;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OffreServices {
    private final Connection connection;

    public OffreServices() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    public void createOffer(int idPlat, double pourcentage, double newPrice, Date startDate, Date endDate, int idResto) throws SQLException {
        String sql = "INSERT INTO offre_resto (id_plat, pourcentage, new_price, date_debut, date_fin, id_resto) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idPlat);
            preparedStatement.setDouble(2, pourcentage);
            preparedStatement.setFloat(3, (float) newPrice);
            preparedStatement.setDate(4, startDate);
            preparedStatement.setDate(5, endDate);
            preparedStatement.setInt(6, idResto);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating offer failed, no rows affected.");
            }
            System.out.println("Offer added successfully.");
        }
    }
    public List<Offre> getOffersForGerant(int idResto) {
        List<Offre> offers = new ArrayList<>();
        String sql = "SELECT offre_resto.*, plat.nom AS plat_name FROM offre_resto JOIN plat ON offre_resto.id_plat = plat.id_plat WHERE offre_resto.id_resto = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, idResto);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    double pourcentage = resultSet.getDouble("pourcentage");
                    Date dateDebut = resultSet.getDate("date_debut");
                    Date dateFin = resultSet.getDate("date_fin");
                    int idPlat = resultSet.getInt("id_plat");
                    double newPrice = resultSet.getDouble("new_price");
                    String platName = resultSet.getString("plat_name");


                    Offre offer = new Offre(id, pourcentage, dateDebut, dateFin, idPlat, newPrice, platName);
                    offers.add(offer);

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return offers;
    }

    public void deleteOffer(int offerId) throws SQLException {
        String sql = "DELETE FROM offre_resto WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, offerId);
            preparedStatement.executeUpdate();
        }
    }


    public void updateOffer(Offre offer) throws SQLException {
        String sql = "UPDATE offre_resto SET pourcentage = ?, new_price = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, offer.getPourcentage());
            preparedStatement.setFloat(2, offer.getNew_price());
            preparedStatement.setInt(3, offer.getId());

            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("Failed to update offer. No rows affected.");
            }
            System.out.println("Offer updated successfully.");
        } catch (SQLException e) {
            System.err.println("Error updating offer: " + e.getMessage());
            throw e; // Re-throw the exception to handle it at the calling site
        }
    }
    public void boostOffer(int gerantId, int offerId, int subscriptionTypeId) throws SQLException {
        // Determine the boost end date based on the subscription type
        LocalDate boostStartDate = LocalDate.now();
        LocalDate boostEndDate = boostStartDate.plusDays(subscriptionTypeId == 2 ? 1 : 2); // 1 day for type 2, 2 days for type 3

        // Check the current number of boosts
        if (!canBoostOffer(gerantId, subscriptionTypeId)) {
            throw new SQLException("Cannot boost offer. Boost limit reached for your subscription type.");
        }

        // Insert the boost record into the boost table
        String sql = "INSERT INTO boost (id_resto, id_offre_plat, boost_start_date, boost_end_date, num_boost, sub_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, gerantId);
            preparedStatement.setInt(2, offerId);
            preparedStatement.setDate(3, java.sql.Date.valueOf(boostStartDate));
            preparedStatement.setDate(4, java.sql.Date.valueOf(boostEndDate));
            preparedStatement.setInt(5, getBoostCount(gerantId) + 1); // Increment the current boost count
            preparedStatement.setInt(6, subscriptionTypeId);
            preparedStatement.executeUpdate();
        }
    }

    public boolean canBoostOffer(int gerantId, int subscriptionTypeId) throws SQLException {
        int allowedBoosts = subscriptionTypeId == 2 ? 1 : (subscriptionTypeId == 3 ? 4 : 0);
        int currentBoostCount = getBoostCount(gerantId);
        return currentBoostCount < allowedBoosts;
    }

    private int getBoostCount(int gerantId) throws SQLException {
        String sql = "SELECT num_boost FROM boost WHERE id_resto = ? ORDER BY boost_start_date DESC LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, gerantId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("num_boost");
                }
            }
        }
        return 0;
    }
    public List<Offre> getBoostedOffers() {
        List<Offre> offers = new ArrayList<>();
        String sql = "SELECT o.id, o.pourcentage, o.date_debut, o.date_fin, o.id_resto, o.id_plat, o.new_price, p.nom AS plat_name " +
                "FROM offre_resto o " +
                "JOIN boost b ON o.id = b.id_offre_plat " +
                "JOIN plat p ON o.id_plat = p.id_plat " +
                "WHERE b.boost_start_date <= CURRENT_DATE " +
                "AND b.boost_end_date >= CURRENT_DATE";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double pourcentage = resultSet.getDouble("pourcentage");
                Date dateDebut = resultSet.getDate("date_debut");
                Date dateFin = resultSet.getDate("date_fin");
                int idPlat = resultSet.getInt("id_plat");
                double newPrice = resultSet.getDouble("new_price");
                String platName = resultSet.getString("plat_name");

                Offre offer = new Offre(id, pourcentage, dateDebut, dateFin, idPlat, newPrice, platName);
                offers.add(offer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offers;
    }

    public List<Offre> getOffersByRestaurant(int restaurantId) {
        List<Offre> offers = new ArrayList<>();
        String sql = "SELECT * FROM offre_resto WHERE id_resto = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, restaurantId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                offers.add(new Offre(
                        rs.getInt("id"),
                        rs.getDouble("pourcentage"),
                        rs.getDate("date_debut"),
                        rs.getDate("date_fin"),
                        rs.getInt("id_plat"),
                        rs.getDouble("new_price"),
                        rs.getString("nom_plat") // Assuming there is a 'nom_plat' field
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offers;
    }



}

