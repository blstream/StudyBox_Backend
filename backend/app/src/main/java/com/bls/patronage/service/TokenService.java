package com.bls.patronage.service;

import com.bls.patronage.db.model.ResetPasswordToken;

import java.util.UUID;

public interface TokenService {
    ResetPasswordToken generate(String email);
    void sendMessage(String email);
}
