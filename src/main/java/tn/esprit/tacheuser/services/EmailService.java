package tn.esprit.tacheuser.services;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private static final String SMTP_SERVER = "smtp-relay.brevo.com";
    private static final int SMTP_PORT = 587;
    private static final String SMTP_USER = "866fc7002@smtp-brevo.com"; // Remplace par ton email SMTP
    private static final String SMTP_PASSWORD = "jA8yf9S0rB4UQgFE"; // Remplace par ta clé SMTP
    private static final String SENDER_EMAIL = "orphelincare@gmail.com";
    private static final String SENDER_NAME = "OrphenCare";

    public static void envoyerEmail(String destinataire, String sujet, String contenu) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", SMTP_SERVER);
        properties.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SMTP_USER, SMTP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL, SENDER_NAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            message.setSubject(sujet);
            message.setContent(contenu, "text/html");

            Transport.send(message);
            System.out.println("Email envoyé avec succès à " + destinataire);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
