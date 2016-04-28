package com.bls.patronage.api;

import com.bls.patronage.db.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserRepresentation implements DbMappable<User> {
    @Email
    @NotEmpty
    private final String email;
    private UUID id;
    private String name;
    @NotEmpty
    @Length(min = 8)
    private String password;

    public UserRepresentation(String email) {
        this.email = email;
    }

    public UserRepresentation(@JsonProperty("email") String email,
                              @JsonProperty("name") String name,
                              @JsonProperty("password") String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public UserRepresentation(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserRepresentation(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
    }

    @Override
    public User map() {
        return new User(id, email, name, password);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public UserRepresentation setName(String name) {
        this.name = name;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRepresentation setPassword(String password) {
        this.password = password;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public UserRepresentation setId(UUID id) {
        this.id = id;
        return this;
    }
}
