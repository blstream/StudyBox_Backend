package com.bls.patronage.service;

import com.bls.patronage.db.model.ResetPasswordToken;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class ResetPasswordService implements TokenService {

    @Override
    public ResetPasswordToken generate(String userEmail) {
        Date date = computeExpirationDate();
        return new ResetPasswordToken(UUID.randomUUID(), userEmail, date, true);
    }

    private Date computeExpirationDate() {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        return calendar.getTime();
    }

    @Override
    public void sendMessage(String email) {

    }
}
