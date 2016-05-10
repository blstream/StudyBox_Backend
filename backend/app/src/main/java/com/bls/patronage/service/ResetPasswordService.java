package com.bls.patronage.service;

import com.bls.patronage.db.model.ResetPasswordToken;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ResetPasswordService implements TokenService {

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

        System.out.println(resetPasswordUri + "/" + token.toString());

    }
}
