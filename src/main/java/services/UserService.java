package services;

import utils.MyDatabase;
import java.time.format.DateTimeFormatter;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;


public class UserService {
    private final Connection connection;

    public UserService() {
        this.connection = MyDatabase.getInstance().getConnection();
    }
    public Map<String, Integer> fetchUserCountOverTime(String userType, String timeInterval) {
        Map<String, Integer> userCount = new LinkedHashMap<>();
        String tableName = getTableName(userType);
        String sql = buildSqlQuery(tableName, timeInterval);

        try (PreparedStatement stmt = connection.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String time = rs.getString("time");
                int count = rs.getInt("count");
                userCount.put(time, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception, maybe log it or show a user-friendly message
        }
        return userCount;
    }
    private String getTableName(String userType) {
        switch (userType.toLowerCase()) {
            case "client":
                return "client"; // the actual client table name in your DB
            case "gerant":
                return "gerant"; // the actual gerant table name in your DB
            case "livreur":
                return "livreur"; // the actual livreur table name in your DB
            default:
                throw new IllegalArgumentException("Invalid user type provided: " + userType);
        }
    }


    private String buildSqlQuery(String tableName, String timeInterval) {
        String dateFormat;
        switch (timeInterval.toLowerCase()) {
            case "days":
                dateFormat = "%Y-%m-%d";
                break;
            case "weeks":
                dateFormat = "%Y-%u";
                break;
            case "months":
                dateFormat = "%Y-%m";
                break;
            case "years":
                dateFormat = "%Y";
                break;
            default:
                throw new IllegalArgumentException("Invalid time interval: " + timeInterval);
        }

        // Including a WHERE clause to filter records for the current year
        String currentYearFilter = "YEAR(date) = YEAR(CURDATE())";

        return String.format(
                "SELECT DATE_FORMAT(date, '%s') AS time, COUNT(*) AS count FROM %s WHERE %s GROUP BY time ORDER BY time",
                dateFormat, tableName, currentYearFilter
        );
    }


}
