package services;
import models.Avis;
import models.Plat;
import utils.MyDatabase;
import java.sql.*;
public class AvisServices {
    private final Connection connection;

    public AvisServices() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    public void ajouterAvis(Avis avis) throws SQLException {
        String sql = "INSERT INTO avis (id_plat, id_resto, id_client, description, etoile) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setInt(1, avis.getIdPlat());
            pst.setInt(2, avis.getIdResto());
            pst.setInt(3, avis.getIdClient());
            pst.setString(4, avis.getDescription());
            pst.setInt(5, avis.getEtoile());
            pst.executeUpdate();
        }
    }
}
