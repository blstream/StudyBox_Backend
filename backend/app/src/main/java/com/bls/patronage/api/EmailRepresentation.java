package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class EmailRepresentation {
    @Email
    @NotEmpty
    private final String email;

    public EmailRepresentation(@JsonProperty("email") String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
