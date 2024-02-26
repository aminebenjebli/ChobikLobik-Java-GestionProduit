package services;



import models.Category;

import java.sql.SQLException;
import java.util.List;

public interface ICategory<T>{
    public void ajouter(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public void supprimer(int id) throws SQLException;
    public List<T> afficher() throws SQLException;


    List<Category> rechercher(String keyword) throws SQLException;
}
