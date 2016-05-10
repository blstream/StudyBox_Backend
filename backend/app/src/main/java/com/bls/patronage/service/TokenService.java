package com.bls.patronage.service;

import com.bls.patronage.db.model.ResetPasswordToken;

public interface TokenService {
    ResetPasswordToken generate(String email);
    void sendMessage(String email);
}
