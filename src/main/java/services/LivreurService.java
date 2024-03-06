package services;

import models.Livreur;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LivreurService {
    private final Connection connection;

    public LivreurService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouterLivreur(Livreur livreur) throws SQLException {
        String query = "INSERT INTO livreur (nom, prenom, email, password, adresse, id_vehicule, id_zone_livraison, num_tel,date) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, livreur.getNom());
            pst.setString(2, livreur.getPrenom());
            pst.setString(3, livreur.getEmail());
            pst.setString(4, livreur.getPassword());
            pst.setString(5, livreur.getAdresse());
            pst.setInt(6, livreur.getIdVehicule());
            pst.setInt(7, livreur.getIdZoneLivraison());
            pst.setInt(8, livreur.getNumTel());
            pst.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            pst.executeUpdate();
        }
    }

    public List<String> getCities() throws SQLException {
        List<String> cities = new ArrayList<>();
        String query = "SELECT city FROM cities"; // Assuming your table is named 'cities' and has a column 'city'
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                cities.add(rs.getString("city"));
            }
        }
        return cities;
    }

    public List<String> getAddresses() throws SQLException {
        List<String> addresses = new ArrayList<>();
        String query = "SELECT DISTINCT adresse FROM livreur";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                addresses.add(rs.getString("adresse"));
            }
        }
        return addresses;
    }

    public List<String> getVehiculeTypes() throws SQLException {
        List<String> vehicules = new ArrayList<>();
        String query = "SELECT DISTINCT type FROM vehicule";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                vehicules.add(rs.getString("type"));
            }
        }
        return vehicules;
    }

    public List<String> getDeliveryZones() throws SQLException {
        List<String> zones = new ArrayList<>();
        String query = "SELECT DISTINCT zone FROM zone_liv";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                zones.add(rs.getString("zone"));
            }
        }
        return zones;
    }

    // Assume these methods return the actual ID from the database
    public int findVehiculeIdByType(String type) throws SQLException {
        String query = "SELECT id FROM vehicule WHERE type = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, type);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Vehicle type not found: " + type);
    }

    public int findZoneLivraisonIdByName(String name) throws SQLException {
        String query = "SELECT id FROM zone_liv WHERE zone = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, name);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }
        throw new SQLException("Delivery zone not found: " + name);
    }


    public boolean authenticateByEmail(String email, String password) {
        String query = "SELECT COUNT(*) FROM livreur WHERE email = ? AND password = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Livreur getLivreurByEmail(String email) throws SQLException {
        String query = "SELECT * FROM livreur WHERE email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Livreur(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("adresse"),
                            rs.getInt("id_vehicule"),
                            rs.getInt("id_zone_livraison"),
                            rs.getInt("num_tel"),
                            rs.getTimestamp("date")
                    );
                }
            }
        }
        return null;
    }


    public String findVehiculeNameById(int id) throws SQLException {
        String vehiculeType = null;
        String query = "SELECT type FROM vehicule WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    vehiculeType = rs.getString("type");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return vehiculeType;
    }

    public String findZoneLivraisonNameById(int id) throws SQLException {
        String zoneName = null;
        String query = "SELECT zone FROM zone_liv WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    zoneName = rs.getString("zone");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return zoneName;
    }

    public void updateLivreur(Livreur livreur, String cityName) throws SQLException {
        int cityId = getCityIdByName(cityName);
        String query = "UPDATE livreur SET nom = ?, prenom = ?, email = ?, adresse = ?, id_vehicule = ?, id_zone_livraison = ?, num_tel = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, livreur.getNom());
            pst.setString(2, livreur.getPrenom());
            pst.setString(3, livreur.getEmail());
            pst.setString(4, cityName);
            pst.setInt(5, livreur.getIdVehicule());
            pst.setInt(6, livreur.getIdZoneLivraison());
            pst.setInt(7, livreur.getNumTel());
            pst.setInt(8, livreur.getId());

            pst.executeUpdate();
        }
    }


    public int getCityIdByName(String cityName) throws SQLException {
        int cityId = 0;
        String query = "SELECT id FROM cities WHERE city = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, cityName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    cityId = rs.getInt("id");
                } else {
                    throw new SQLException("City not found: " + cityName);
                }
            }
        }
        return cityId;
    }


    public void deleteLivreur(Livreur livreur) throws SQLException {
        String query = "DELETE FROM livreur WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, livreur.getId());
            pst.executeUpdate();
        }
    }


    public List<Livreur> afficher() throws SQLException {
        List<Livreur> livreurs = new ArrayList<>();
        String query = "SELECT l.*, v.type AS vehiculeType, z.zone AS zoneName " +
                "FROM livreur l " +
                "JOIN vehicule v ON l.id_vehicule = v.id " +
                "JOIN zone_liv z ON l.id_zone_livraison = z.id";
        try (PreparedStatement pst = connection.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Livreur livreur = new Livreur(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        null,
                        rs.getString("adresse"),
                        rs.getInt("id_vehicule"),
                        rs.getInt("id_zone_livraison"),
                        rs.getInt("num_tel"),
                        rs.getTimestamp("date")
                );
                livreur.setVehiculeName(rs.getString("vehiculeType"));
                livreur.setZoneLivraisonName(rs.getString("zoneName"));
                livreurs.add(livreur);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return livreurs;
    }

    public List<String> getAllVehiculeNames() throws SQLException {
        List<String> vehiculeNames = new ArrayList<>();
        String query = "SELECT type FROM vehicule";
        try (PreparedStatement pst = connection.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                vehiculeNames.add(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return vehiculeNames;
    }

    public List<String> getAllZoneLivraisonNames() throws SQLException {
        List<String> zoneNames = new ArrayList<>();
        String query = "SELECT zone FROM zone_liv";
        try (PreparedStatement pst = connection.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                zoneNames.add(rs.getString("zone"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return zoneNames;
    }
// Inside LivreurService class

    public int getVehiculeIdByName(String vehiculeName) throws SQLException {
        int vehiculeId = 0; // Initialize with a default value or handle null case appropriately
        String query = "SELECT id FROM vehicule WHERE type = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, vehiculeName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    vehiculeId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return vehiculeId;
    }


    public int getZoneIdByName(String zoneName) throws SQLException {
        int zoneId = 0;
        String query = "SELECT id FROM zone_liv WHERE zone = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, zoneName);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    zoneId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return zoneId;
    }


}
