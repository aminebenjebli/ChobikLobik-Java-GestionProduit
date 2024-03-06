package services;

import models.AbonnementType;
import models.Feature;
import models.Gerant;

import models.SubscriptionType;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GerantService implements IGerant {
    private final Connection connection;

    public GerantService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Gerant gerant) throws SQLException {
        String req = "INSERT INTO gerant (username, name, description, document, image, email, password, date) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, gerant.getUsername());
            pst.setString(2, gerant.getName());
            pst.setString(3, gerant.getDescription());
            pst.setString(4, gerant.getDocument());
            pst.setString(5, gerant.getImage());
            pst.setString(6, gerant.getEmail());
            pst.setString(7, gerant.getPassword());
            pst.setTimestamp(8, gerant.getDate());

            pst.executeUpdate();
        }
    }

    @Override
    public void modifier(Gerant gerant) throws SQLException {
        String req = "UPDATE gerant SET username = ?, name = ?, description = ?, document = ?, image = ?, email = ?, password = ?, date = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, gerant.getUsername());
            pst.setString(2, gerant.getName());
            pst.setString(3, gerant.getDescription());
            pst.setString(4, gerant.getDocument());
            pst.setString(5, gerant.getImage());
            pst.setString(6, gerant.getEmail());
            pst.setString(7, gerant.getPassword());
            pst.setTimestamp(8, new Timestamp(gerant.getDate().getTime()));
            pst.setInt(9, gerant.getId());

            pst.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM gerant WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public List<Gerant> afficher() throws SQLException {
        List<Gerant> gerants = new ArrayList<>();
        String req = "SELECT * FROM gerant";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Gerant gerant = new Gerant(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("document"),
                        rs.getString("image"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getTimestamp("date")
                );
                gerants.add(gerant);
            }
        }
        return gerants;
    }
    public boolean authenticateByEmail(String email, String password) throws SQLException {
        String req = "SELECT * FROM gerant WHERE email = ? AND password = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, email);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        }
    }
    public Gerant getGerantByEmail(String email) throws SQLException {
        Gerant gerant = null;
        String sql = "SELECT * FROM gerant WHERE email = ?";
        try (PreparedStatement statement = this.connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                String document = resultSet.getString("document");
                String image = resultSet.getString("image");
                String password = resultSet.getString("password");
                Timestamp date = resultSet.getTimestamp("date");

                gerant = new Gerant(id, username, name, description, document, image, email, password, date);
            }
        }
        return gerant;
    }

    public List<AbonnementType> getAbonnementTypesWithFeatures() throws SQLException {
        List<AbonnementType> abonnementTypes = new ArrayList<>();
        String query = "SELECT at.id, at.name, at.price, f.feature " +
                "FROM abonnement_type at " +
                "LEFT JOIN features f ON at.id = f.id_abonnement";

        try (PreparedStatement pst = connection.prepareStatement(query)) {
            ResultSet rs = pst.executeQuery();
            Map<Integer, AbonnementType> typesMap = new HashMap<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                AbonnementType type = typesMap.computeIfAbsent(id, k -> {
                    try {
                        return new AbonnementType(
                                id, rs.getString("name"), rs.getFloat("price"), new ArrayList<>()
                        );
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                String featureDesc = rs.getString("feature");
                if (featureDesc != null) {
                    type.getFeatures().add(new Feature(featureDesc));
                }
            }
            abonnementTypes.addAll(typesMap.values());
        }
        return abonnementTypes;
    }
    private List<Feature> getFeaturesForAbonnement(int abonnementId) throws SQLException {
        List<Feature> features = new ArrayList<>();
        String req = "SELECT * FROM features WHERE id_abonnement = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, abonnementId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                String feature = rs.getString("feature");
                features.add(new Feature(feature));
            }
        }
        return features;
    }
    public List<SubscriptionType> fetchSubscriptionTypes() throws SQLException {
        List<SubscriptionType> subscriptionTypes = new ArrayList<>();
        String query = "SELECT at.id, at.name, at.price, f.feature FROM abonnement_type at " +
                "LEFT JOIN features f ON at.id = f.id_abonnement ORDER BY at.id, f.id";

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            Map<Integer, SubscriptionType> subscriptionMap = new HashMap<>();

            while (rs.next()) {
                int id = rs.getInt("id");
                SubscriptionType type = subscriptionMap.computeIfAbsent(id, k -> {
                    try {
                        return new SubscriptionType(id, rs.getString("name"), rs.getFloat("price"));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                String feature = rs.getString("feature");
                if (feature != null && !feature.trim().isEmpty()) {
                    type.addFeature(feature);
                }
            }
            subscriptionTypes.addAll(subscriptionMap.values());
        }
        return subscriptionTypes;
    }

    // In GerantService.java
    public boolean isGerantSubscriptionActive(int gerantId) throws SQLException {
        String sql = "SELECT status FROM abonnement WHERE id_resto = ? AND date_fin > NOW() ORDER BY date_fin DESC LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, gerantId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int status = resultSet.getInt("status");
                    return status == 1; // Assuming '1' means active
                }
            }
        }
        return false; // Default to not active if no records found
    }

    public List<Gerant> getRandomGerants(int limit) throws SQLException {
        List<Gerant> gerants = new ArrayList<>();
        String sql = "SELECT * FROM gerant ORDER BY RAND() LIMIT ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, limit);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                // Assume Gerant has a constructor that matches this fetching
                Gerant gerant = new Gerant(
                        rs.getInt("id"), rs.getString("username"), rs.getString("name"),
                        rs.getString("description"), rs.getString("document"),
                        rs.getString("image"), rs.getString("email"),
                        rs.getString("password"), rs.getTimestamp("date")
                );
                gerants.add(gerant);
            }
        }
        return gerants;
    }
    public List<Gerant> filterGerantsByKeyword(String keyword) throws SQLException {
        List<Gerant> filteredGerants = new ArrayList<>();
        String sql = "SELECT * FROM gerant WHERE name LIKE ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, "%" + keyword + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                // Construct Gerant object from ResultSet
                Gerant gerant = new Gerant(); // Simplified, fill in details
                filteredGerants.add(gerant);
            }
        }
        return filteredGerants;
    }
    public List<Gerant> getAllGerants() throws SQLException {
        List<Gerant> gerants = new ArrayList<>();
        String sql = "SELECT id, name, description, image FROM gerant";
        try (PreparedStatement pst = connection.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Gerant gerant = new Gerant();
                gerant.setId(rs.getInt("id"));
                gerant.setName(rs.getString("name"));
                gerant.setDescription(rs.getString("description"));
                gerant.setImage(rs.getString("image"));
                gerants.add(gerant);
            }
        }
        return gerants;
    }

    public List<Gerant> searchGerantsByName(String name) throws SQLException {
        List<Gerant> gerants = new ArrayList<>();
        String sql = "SELECT * FROM gerant WHERE name LIKE ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setString(1, "%" + name + "%");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                gerants.add(new Gerant(rs.getInt("id"), rs.getString("username"), rs.getString("name"),
                        rs.getString("description"), rs.getString("document"), rs.getString("image"),
                        rs.getString("email"), rs.getString("password"), rs.getTimestamp("date")));
            }
        }
        return gerants;
    }





}
