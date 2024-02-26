package services;


import models.Plat;

import java.sql.SQLException;
import java.util.List;

public interface IPlat<T>{

    void ajouter(Plat plat, int categoryId) throws SQLException;

    public void ajouter(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public void supprimer(int id_plat) throws SQLException;
    public List<T> afficher() throws SQLException;



}
