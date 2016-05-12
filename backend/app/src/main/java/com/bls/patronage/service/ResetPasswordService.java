package com.bls.patronage.service;

import com.bls.patronage.db.model.ResetPasswordToken;
import com.bls.patronage.exception.PasswordResetException;
import com.bls.patronage.service.configuration.ResetPasswordConfiguration;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.ws.rs.core.UriBuilder;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

public class ResetPasswordService implements TokenService {

    private final ResetPasswordConfiguration config;

    public ResetPasswordService(ResetPasswordConfiguration config) {
        this.config = config;
    }

    @Override
    public ResetPasswordToken generate(String userEmail) {
        return new ResetPasswordToken(UUID.randomUUID(), userEmail, Date.from(Instant.now()
                .plus(1, ChronoUnit.DAYS)), true);
    }

    @Override
    public void sendMessage(String email, UUID token) {

        final Properties properties = configMail();
        final Session session = authenticate(properties);
        sendMail(session, email, token);
    }

    private Properties configMail() {

        final Properties properties = new Properties();
        properties.put("mail.smtp.auth", config.getMail().getEnableAuth());
        properties.put("mail.smtp.starttls.enable", config.getMail().getEnableTls());
        properties.put("mail.smtp.host", config.getMail().getHost());
        properties.put("mail.smtp.port", config.getMail().getPort());

        return properties;
    }

    private Session authenticate(Properties properties) {
        return Session.getInstance(properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(
                                config.getMail().getUsername(),
                                config.getMail().getPassword());
                    }
                });
    }

    private void sendMail(Session session, String address, UUID token) throws PasswordResetException {
        try {

            final Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(config.getMail().getFromAddress()));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(address));
            message.setSubject("Studybox password reset.");
            message.setText(
                    UriBuilder.fromPath(config.getResetPasswordUrl())
                    .queryParam("token", token.toString())
                    .queryParam("email", address.trim())
                    .build()
                    .toString()
            );

            Transport.send(message);

        } catch (MessagingException e) {
            throw new PasswordResetException("Unable to send email message.");
        }
    }
}
