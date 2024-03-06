package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;


public class NavigationsController {

    public static void refreshAfficherCategories(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationsController.class.getResource("/AfficherCat.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Liste des Catégories");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    // integration
   /* public static void OpenInterfaceAddOffer(MouseEvent event, String fxmlFile) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(NavigationsController.class.getResource(fxmlFile));
            root = loader.load();
           AddOffer addOffer = loader.getController();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("ChoubikLoubik : Ajouter une Offre");
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void OpenInterfaceAfficherOffre(MouseEvent event, String fxmlFile) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(NavigationsController.class.getResource(fxmlFile));
            root = loader.load();
            ConsulterOffre affOffer = loader.getController();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("ChoubikLoubik : Offer List");
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }*/
    /******************************************/
    public static void OpenInterfcaeAfficherCategories(MouseEvent event, String fxmlFile) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(NavigationsController.class.getResource(fxmlFile));
            root = loader.load();
            AfficherCat AffCat = loader.getController();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("ChoubikLoubik : Liste des Catégories");
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }


   /* public static void OpenInterfaceAfficherPlats(MouseEvent event, String fxmlFile) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(NavigationsController.class.getResource(fxmlFile));
            root = loader.load();
            DisplayPlatsController AffPlat = loader.getController();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("ChoubikLoubik : Liste des Plats");
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }*/

    public static void OpenInterfaceAjouetrCategorie(MouseEvent event, String fxmlFile) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(NavigationsController.class.getResource(fxmlFile));
            root = loader.load();
            AjouterCategorieAmine Addproduct = loader.getController();
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("ChoubikLoubik : Ajouter une nouvelle Catégorie de Palts");
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }



    /*public static void OpenInterfaceAjouterPlatAmine(MouseEvent event, String fxmlFile) {
        Parent root = null;
        try {
            FXMLLoader loader = new FXMLLoader(NavigationsController.class.getResource(fxmlFile));
            root = loader.load();
            AjouterPlatController Addproduct = loader.getController();
            //Addproduct.setInformation(P);
            Scene scene = new Scene(root);
            Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            primaryStage.setScene(scene);
            primaryStage.setTitle("ChoubikLoubik : Ajouter Un plat");
            primaryStage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }*/


}