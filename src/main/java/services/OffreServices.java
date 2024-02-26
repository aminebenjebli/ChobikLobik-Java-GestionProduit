package services;

import models.Offre;
import utils.MyDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OffreServices implements IOffre<Offre>{

    private PreparedStatement ste;
    private final Connection connection;

    public OffreServices() {
        this.connection = MyDatabase.getInstance().getConnection();
    }

    @Override
    public void ajouter(Offre offre) throws SQLException {
        String req = "INSERT INTO offre_resto (percentage, id_resto, id_plat, image, date_debut, date_fin) VALUES(?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pst = connection.prepareStatement(req)) {
            System.out.println("Date de début avant conversion : " + offre.getDate_debut());
            System.out.println("Date de fin avant conversion : " + offre.getDate_fin());

            pst.setInt(1, offre.getPercentage());
            pst.setInt(2, offre.getId_resto());
            pst.setInt(3, offre.getId_plat());
            pst.setString(4, offre.getImage());
            pst.setTimestamp(5, new Timestamp(offre.getDate_debut().getTime()));
            pst.setTimestamp(6, new Timestamp(offre.getDate_fin().getTime()));

            System.out.println("Date de début après conversion : " + new Timestamp(offre.getDate_debut().getTime()));
            System.out.println("Date de fin après conversion : " + new Timestamp(offre.getDate_fin().getTime()));

            pst.executeUpdate();
        }

    }

    @Override
    public void modifier(Offre offre) throws SQLException {
        String req = "update offre_resto set percentage = ? , id_resto = ? , id_plat = ? , image = ? , date_debut = ? , date_fin = ?   where id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, offre.getPercentage());
            pst.setInt(2, offre.getId_resto());
            pst.setInt(3, offre.getId_plat());
            pst.setString(4, offre.getImage());
            pst.setTimestamp(5, new Timestamp(offre.getDate_debut().getTime()));
            pst.setTimestamp(6, new Timestamp(offre.getDate_fin().getTime()));
            pst.setInt(7, offre.getId());
            pst.executeUpdate();
        }
    }
    public int supprimerOffresExpirees() throws SQLException {
        String req = "DELETE FROM offre_resto WHERE date_fin < NOW()";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
          return   pst.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
            return -1; // Ou une autre valeur pour indiquer une erreur
        }
    }
    @Override
    public void supprimer(int id) throws SQLException {
        String req = "DELETE FROM `offre_resto` WHERE id = ?";
        try (PreparedStatement pst = connection.prepareStatement(req)) {
            pst.setInt(1, id);
            pst.executeUpdate();
        }
    }

    @Override
    public List<Offre> afficher() throws SQLException {
        String req = "SELECT * FROM `offre_resto` ";
        List<Offre> offres = new ArrayList<>();
        Statement st = connection.createStatement();
        ResultSet rs = st.executeQuery(req);
        while (rs.next()) {
            Offre O = new Offre();
            O.setId(rs.getInt("id"));
            O.setPercentage(rs.getInt("percentage"));
            O.setId_resto(rs.getInt("id_resto"));
            O.setId_plat(rs.getInt("id_plat"));
            O.setImage(rs.getString("image"));


            Timestamp timestampDebut = rs.getTimestamp("date_debut");
            Date dateDebut= new Date(timestampDebut.getTime());
            O.setDate_debut(dateDebut);

            Timestamp timestampFin = rs.getTimestamp("date_fin");
            O.setDate_fin(rs.getDate("date_fin"));
            Date dateFin = new Date(timestampFin.getTime());
            // Vérifier si l'offre est expirée
            if (dateFin.toLocalDate().isBefore(LocalDate.now())) {
                // L'offre est expirée, ne l'ajoutez pas à la liste
                continue;
            }
            O.setDate_fin(dateFin);

            offres.add(O);
        }
        return offres;
    }
}
