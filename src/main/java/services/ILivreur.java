package services;

import models.Livreur;
import java.sql.SQLException;
import java.util.List;

public interface ILivreur {
    void ajouterLivreur(Livreur livreur) throws SQLException;
    void modifier(Livreur livreur) throws SQLException;
    void supprimer(int id) throws SQLException;
    List<Livreur> afficher() throws SQLException;
}
