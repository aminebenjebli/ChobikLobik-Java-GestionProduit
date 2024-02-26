package services;



import models.Category;
import models.Gerant;
import models.Plat;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlatServices implements IPlat<Plat> {

    private final Connection connection;

    public PlatServices() {
        this.connection = MyDatabase.getInstance().getConnection();
    }
    @Override
    public void ajouter(Plat plat, int categoryId) throws SQLException {
        // Assume the category ID is valid and the caller of this method has verified it
        String req = "INSERT INTO plat (id, id_restaurant, nom, image, description, prix) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, categoryId); // Set the category ID
            pst.setInt(2, plat.getId_restaurant());
            pst.setString(3, plat.getNom());
            pst.setString(4, plat.getImage());
            pst.setString(5, plat.getDescription());
            pst.setFloat(6, plat.getPrix());
            pst.executeUpdate();
        }
    }
    public void ajouterWithCategory(Plat plat, int categoryId) throws SQLException {
        String req = "INSERT INTO plat (id_category, id_restaurant, nom, image, description, prix) VALUES(?,?,?,?,?,?)";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, categoryId);
            pst.setInt(2, plat.getId_restaurant());
            pst.setString(3, plat.getNom());
            pst.setString(4, plat.getImage());
            pst.setString(5, plat.getDescription());
            pst.setFloat(6, plat.getPrix());
            pst.executeUpdate();
        }
    }

    @Override
    public void ajouter(Plat plat) throws SQLException {

    }


    @Override
    public void modifier(Plat plat) throws SQLException {
        String sql = "update plat set id = ? , id_restaurant= ? ,nom=?,image=?, description=?,prix=? where id_plat = ?";
        PreparedStatement pst = connection.prepareStatement(sql);
        pst.setInt(1,plat.getId());
        pst.setInt(2,plat.getId_restaurant());
        pst.setString(3,plat.getNom());
        pst.setString(4,plat.getImage());
        pst.setString(5,plat.getDescription());
        pst.setFloat(6,plat.getPrix());
        pst.setInt(7,plat.getId_plat());
        pst.executeUpdate();

    }

    @Override
    public void supprimer(int id_plat) throws SQLException {
        String req = "DELETE FROM `plat` WHERE id_plat = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1,id_plat);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Plat> afficher() throws SQLException {
        List<Plat> plats = new ArrayList<>();
        String req = "SELECT * FROM plat";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Plat p = new Plat(rs.getInt("id_plat"), rs.getInt("id_category"), rs.getInt("id_restaurant"),
                        rs.getString("nom"), rs.getString("image"), rs.getString("description"), rs.getFloat("prix"));
                plats.add(p);
            }
        }
        return plats;
    }
    public List<Category> fetchCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String req = "SELECT id, type FROM category";
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                categories.add(new Category(rs.getInt("id"), rs.getString("type"), rs.getString("image")));
            }
        }
        return categories;
    }
// In PlatServices.java

    public List<Gerant> fetchGerants() throws SQLException {
        List<Gerant> gerants = new ArrayList<>();
        String sql = "SELECT * FROM gerant"; // Replace with your actual gerant table name
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                Gerant gerant = new Gerant(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getString("document"),
                        rs.getString("image"),
                        rs.getString("email"),    // Additional email parameter
                        rs.getString("password"), // Additional password parameter
                        rs.getTimestamp("date")   // Additional date parameter
                );
                gerants.add(gerant);
            }
        }
        return gerants;
    }


    public List<Plat> fetchPlatsByGerant(int gerantId) throws SQLException {
        List<Plat> plats = new ArrayList<>();
        String sql = "SELECT * FROM plat WHERE id_restaurant = ?";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, gerantId);
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Plat plat = new Plat(
                            rs.getInt("id_plat"),
                            rs.getInt("id_category"),
                            rs.getInt("id_restaurant"),
                            rs.getString("nom"),
                            rs.getString("image"),
                            rs.getString("description"),
                            rs.getFloat("prix")
                    );
                    plats.add(plat);
                }
            }
        }
        return plats;
    }



}
