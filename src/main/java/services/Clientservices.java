package services;

import models.Client;

import java.sql.*;

import utils.MyDatabase;

import java.util.ArrayList;
import java.util.List;

public class Clientservices {
    private final Connection connection;

    public Clientservices() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouter(Client client) throws SQLException {
        String cityName = validateCityName(client.getAdresse());

        String req = "INSERT INTO client (nom, prenom, email, password, username, adresse, num_tel, date) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, client.getNom());
            pst.setString(2, client.getPrenom());
            pst.setString(3, client.getEmail());
            pst.setString(4, client.getPassword());
            pst.setString(5, client.getUsername());
            pst.setString(6, cityName);
            pst.setInt(7, client.getNumTel());
            pst.setTimestamp(8, new Timestamp(System.currentTimeMillis()));

            pst.executeUpdate();
        }
    }

    private String validateCityName(String cityName) throws SQLException {
        String query = "SELECT city FROM cities WHERE city = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, cityName);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getString("city");
            } else {
                throw new SQLException("City not found: " + cityName);
            }
        }
    }

    public boolean authenticate(String usernameOrEmail, String password) {

        String query = "SELECT COUNT(*) FROM client WHERE (username = ? OR email = ?) AND password = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, usernameOrEmail);
            pst.setString(2, usernameOrEmail);
            pst.setString(3, password);

            ResultSet rs = pst.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }



    public Client getClientByUsernameOrEmail(String usernameOrEmail) {
        String query = "SELECT * FROM client WHERE username = ? OR email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, usernameOrEmail);
            pst.setString(2, usernameOrEmail);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String email = rs.getString("email");
                String password = rs.getString("password");
                String username = rs.getString("username");
                String adresse = rs.getString("adresse");
                int numTel = rs.getInt("num_tel");
                Timestamp date = rs.getTimestamp("date");

                return new Client(id, nom, prenom, email, password, username, adresse, numTel, new Date(date.getTime()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<String> getCityNames() throws SQLException {
        List<String> cities = new ArrayList<>();
        String query = "SELECT city FROM cities";
        try (PreparedStatement pst = connection.prepareStatement(query); ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                cities.add(rs.getString("city"));
            }
        }
        return cities;
    }

    public void modifier(Client client) throws SQLException {
        String req = "UPDATE client SET nom = ?, prenom = ?, email = ?, password = ?, username = ?, adresse = ?, num_tel = ?, date = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, client.getNom());
            pst.setString(2, client.getPrenom());
            pst.setString(3, client.getEmail());
            pst.setString(4, client.getPassword());
            pst.setString(5, client.getUsername());
            pst.setString(6, client.getAdresse());
            pst.setInt(7, client.getNumTel());
            pst.setTimestamp(8, new Timestamp(client.getDate().getTime()));
            pst.setInt(9, client.getId());

            pst.executeUpdate();
        }
    }



    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    public List<Client> afficher() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String req = "SELECT * FROM client";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("username"),
                        rs.getString("adresse"),
                        rs.getInt("num_tel"),
                        rs.getDate("date")
                );
                clients.add(client);
            }
        }
        return clients;
    }
    public Client getClientById(int id) throws SQLException {
        String query = "SELECT * FROM client WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return extractClientFromResultSet(rs);
            }
        }
        return null;
    }
    private Client extractClientFromResultSet(ResultSet rs) throws SQLException {
        return new Client(
                rs.getInt("id"),
                rs.getString("nom"),
                rs.getString("prenom"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("username"),
                rs.getString("adresse"),
                rs.getInt("num_tel"),
                new java.util.Date(rs.getTimestamp("date").getTime())
        );
    }




}
