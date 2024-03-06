package services;


import models.Category;
import utils.MyDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryServices implements ICategory<Category> {

    private final Connection connection;

    public CategoryServices() {
        this.connection = MyDatabase.getInstance().getConnection();
    }





    @Override
    public void ajouter(Category category) throws SQLException {
        String req = "INSERT INTO category (type) VALUES(?)";
        try(PreparedStatement pst = connection.prepareStatement(req)){
            pst.setString(1, category.getType());

            pst.executeUpdate();
        }
    }

    @Override
    public void modifier(Category category) throws SQLException {
        String sql = "update category set type = ? where id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1,category.getType());
        preparedStatement.setInt(2,category.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `category` WHERE id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(req);
        preparedStatement.setInt(1,id);
        preparedStatement.executeUpdate();
    }
    @Override
    public List<Category> afficher() throws SQLException {
        String req = "SELECT * FROM category";
        List<Category> categories = new ArrayList<>();
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(req)) {
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setType(rs.getString("type"));
                categories.add(c);
            }
        }
        return categories;
    }
    @Override
    public List<Category> rechercher(String keyword) throws SQLException {
        String req = "SELECT * FROM category WHERE type LIKE ?";
        List<Category> categories = new ArrayList<>();
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setString(1, "%" + keyword + "%");
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    Category c = new Category();
                    c.setId(rs.getInt("id"));
                    c.setType(rs.getString("type"));
                    categories.add(c);
                }
            }
        }
        return categories;
    }

}
