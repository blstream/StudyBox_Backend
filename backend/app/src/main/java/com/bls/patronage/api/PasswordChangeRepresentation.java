package com.bls.patronage.api;

import com.bls.patronage.db.model.ResetPasswordToken;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class PasswordChangeRepresentation extends EmailRepresentation{

    @Length(min = 8)
    private final String password;
    @NotNull
    private final UUID token;

    public PasswordChangeRepresentation(@JsonProperty("email") String email,
                                        @JsonProperty("password") String password,
                                        @JsonProperty("token") UUID token) {
        super(email);
        this.password = password;
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public UUID getToken() {
        return token;
    }
}
