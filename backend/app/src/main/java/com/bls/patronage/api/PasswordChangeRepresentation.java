package com.bls.patronage.api;

import com.bls.patronage.db.model.ResetPasswordToken;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.UUID;

public class PasswordChangeRepresentation {

    @Email
    @NotEmpty
    private final String email;
    @Length(min = 8)
    private final String password;
    @NotEmpty
    private final UUID token;

    public PasswordChangeRepresentation(@JsonProperty("email") String email,
                                        @JsonProperty("password") String password,
                                        @JsonProperty("token") UUID token) {
        this.email = email;
        this.password = password;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UUID getToken() {
        return token;
    }
}
