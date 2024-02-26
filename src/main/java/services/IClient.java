package services;

import models.Client;
import java.sql.SQLException;
import java.util.List;

public interface IClient {
    void ajouter(Client client) throws SQLException;
    void modifier(Client client) throws SQLException;
    void supprimer(int id) throws SQLException;
    List<Client> afficher() throws SQLException;
}
