package com.bls.patronage.service;

import com.bls.patronage.db.model.ResetPasswordToken;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.UriBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class ResetPasswordService implements TokenService {

    private static final String USERNAME = "studybox.test@gmail.com";
    private static final String PASSWORD = "KqcyQEktd29xnzqu";
    private static final String FROM_ADDRESS = "no.reply@studybox.com";
    private final String resetPasswordUri;

    public ResetPasswordService(String uri) {
        this.resetPasswordUri = uri;
    }

    @Override
    public ResetPasswordToken generate(String userEmail) {
        final Date date = computeExpirationDate();
        return new ResetPasswordToken(UUID.randomUUID(), userEmail, date, true);
    }

    private Date computeExpirationDate() {
        final Date date = new Date();
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    @Override
    public void sendMessage(String email, UUID token) {

        Properties properties = configMail();
        Session session = authenticate(properties);
        sendMail(session, email, token);
    }

    private Properties configMail() {

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        return properties;
    }

    private Session authenticate(Properties properties) {
        return Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(USERNAME, PASSWORD);
                    }
                });
    }

    private void sendMail(Session session, String address, UUID token) {
        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_ADDRESS));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(address));
            message.setSubject("Studybox password reset.");
            message.setText(
                    UriBuilder.fromPath(resetPasswordUri)
                    .queryParam("token", token.toString())
                    .queryParam("email", address.trim())
                    .build()
                    .toString()
            );

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
