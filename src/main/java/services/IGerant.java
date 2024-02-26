package services;

import models.Gerant;
import java.sql.SQLException;
import java.util.List;

public interface IGerant {
    void ajouter(Gerant gerant) throws SQLException;
    void modifier(Gerant gerant) throws SQLException;
    void supprimer(int id) throws SQLException;
    List<Gerant> afficher() throws SQLException;
}
