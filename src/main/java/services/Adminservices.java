package services;

import models.Admin;
import utils.MyDatabase;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class Adminservices implements Iadmin<Admin> {
    private final Connection connection;

    public Adminservices() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    public boolean authenticateByEmail(String email, String password) {
        String query = "SELECT * FROM admin WHERE email = ? AND password = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            pst.setString(2, password);
            try (ResultSet rs = pst.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Admin getAdminByEmail(String email) {
        String query = "SELECT * FROM admin WHERE email = ?";
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setString(1, email);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    return new Admin(
                            rs.getInt("id"),
                            rs.getString("nom"),
                            rs.getString("prenom"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }









    @Override
    public void ajouter(Admin admin) throws SQLException {
        String req = "INSERT INTO admin (nom, prenom, email, password) VALUES(?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, admin.getNom());
            pst.setString(2, admin.getPrenom());
            pst.setString(3, admin.getEmail());
            pst.setString(4, admin.getPassword());
            pst.executeUpdate();
        }
    }

    @Override
    public void modifier(Admin admin) throws SQLException {
        String req = "UPDATE admin SET nom = ?, prenom = ?, email = ?, password = ? WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, admin.getNom());
            pst.setString(2, admin.getPrenom());
            pst.setString(3, admin.getEmail());
            pst.setString(4, admin.getPassword());
            pst.setInt(5, admin.getId());
            pst.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM admin WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public List<Admin> afficher() throws SQLException {
        List<Admin> admins = new ArrayList<>();
        String req = "SELECT * FROM admin";
        try (PreparedStatement pst = connection.prepareStatement(req);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                Admin admin = new Admin(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                admins.add(admin);
            }
        }
        return admins;
    }
    public Admin getAdminById(int id) {
        Admin admin = null;
        String query = "SELECT * FROM admin WHERE id = ?"; // Corrected table name to match the ajouter method
        try (PreparedStatement pst = connection.prepareStatement(query)) {
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                admin = new Admin(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception as appropriate for your application
        }
        return admin;
    }





}

