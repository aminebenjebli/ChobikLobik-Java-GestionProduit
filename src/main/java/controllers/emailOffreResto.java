package controllers;

import models.Client;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.PasswordAuthentication;

import java.util.Properties;

public class emailOffreResto {
    public emailOffreResto() {
    }

    public static void sendEmail(String recipientEmail, String subject, String messageBody) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        String username = "aminebenjebli@gmail.com";
        String password = "wcfb iiqi ttca uqnv";

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(subject);
            message.setText(messageBody);

            Transport.send(message);

            System.out.println("Message envoyé avec succès à : " + recipientEmail);
        } catch (MessagingException e) {
            throw new RuntimeException("Erreur lors de l'envoi du message : " + e.getMessage());
        }
    }

public static void main(String[] args) {
        // Exemple : Création d'un objet Partenaire avec une adresse e-mail
        Client partenaire = new Client();
        partenaire.setEmail("aminebenjebli@gmail.com"); // Spécifiez l'adresse e-mail du destinataire ici

        // Envoi d'un e-mail à partir de l'e-mail de l'objet Partenaire
        emailOffreResto.sendEmail(partenaire.getEmail(), "Affectation", "Contenu de l'e-mail");
    }

}
