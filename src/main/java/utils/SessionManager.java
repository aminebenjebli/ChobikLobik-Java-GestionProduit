package utils;

import models.Admin;
import models.Client;
import models.Gerant;
import models.Livreur;

public class SessionManager {
    private static Client currentClient;
    private static Admin currentAdmin;
    private static Livreur currentLivreur;

    public static void setCurrentClient(Client client) {
        currentClient = client;
    }

    public static Client getCurrentClient() {
        return currentClient;
    }

    public static void setCurrentAdmin(Admin admin) {
        currentAdmin = admin;
    }

    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public static void setCurrentLivreur(Livreur livreur) {
        currentLivreur = livreur;
    }

    public static Livreur getCurrentLivreur() {
        return currentLivreur;
    }
    private static Gerant currentGerant;

    public static void setCurrentGerant(Gerant gerant) {
        currentGerant = gerant;
    }

    public static Gerant getCurrentGerant() {
        return currentGerant;
    }



    public static void clearSession() {
        currentClient = null;
        currentAdmin = null;
        currentLivreur = null;
        currentGerant = null; // Assuming you add support for Gerant in SessionManager
    }

}
