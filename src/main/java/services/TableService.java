package services;

import models.Table;
import utils.MyDatabase;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class    TableService {

    public List<Table> getTablesByRestoId(int idResto) throws SQLException {
        List<Table> tables = new ArrayList<>();
        String query = "SELECT * FROM `table` WHERE id_resto = ?";

        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idResto);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tables.add(new Table(
                        rs.getInt("id"),
                        rs.getInt("nombre_p"),
                        rs.getString("status"),
                        rs.getInt("id_resto")
                ));
            }
        }
        return tables;
    }

    public void addTable(int nombreP, String status, int idResto) throws SQLException {
        String query = "INSERT INTO `table` (nombre_p, status, id_resto) VALUES (?, ?, ?)";
        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, nombreP);
            stmt.setString(2, status);
            stmt.setInt(3, idResto);
            stmt.executeUpdate();
        }
    }

    public void updateTableNombreP(int id, int nombreP) throws SQLException {
        String query = "UPDATE `table` SET nombre_p = ? WHERE id = ?";
        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, nombreP);
            stmt.setInt(2, id);
            stmt.executeUpdate();
        }
    }

    public void deleteTable(int id) throws SQLException {
        String query = "DELETE FROM `table` WHERE id = ?";
        try (Connection conn = MyDatabase.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }}
