package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class UserRepresentation {
    @Email
    @NotEmpty
    private final String email;
    @NotEmpty
    private final String name;
    @NotEmpty
    private final String password;

    public UserRepresentation(@JsonProperty("email") String email, @JsonProperty("name") String name, @JsonProperty("password") String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
