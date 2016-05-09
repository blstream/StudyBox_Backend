package com.bls.patronage.db.model;

import com.google.common.base.Objects;

import java.util.Date;
import java.util.UUID;

public class ResetPasswordToken {

    UUID token;
    String email;
    Boolean isActive;
    Date expirationDate;

    public ResetPasswordToken() {
    }

    public ResetPasswordToken(UUID token, String email, Date expirationDate, Boolean isActive) {
        this.token = token;
        this.email = email;
        this.expirationDate = expirationDate;
        this.isActive = isActive;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResetPasswordToken)) {
            return false;
        }

        final ResetPasswordToken that = (ResetPasswordToken) o;

        return this.token.equals(that.token) &&
                this.email.equals(that.email) &&
                this.isActive.equals(that.isActive);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(token, email, isActive);
    }

    @Override
    public String toString() {
        return "ResetPasswordToken{" +
                "token=" + token +
                ", email='" + email + '\'' +
                ", expirationDate=" + expirationDate +
                ", isActive=" + isActive +
                '}';
    }
}
