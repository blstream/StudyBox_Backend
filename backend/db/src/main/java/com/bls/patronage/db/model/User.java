package com.bls.patronage.db.model;

import com.google.common.base.Objects;

import java.util.UUID;

public class User extends IdentifiableEntity {
    private String email;
    private String name;
    private String password;

    public User(String id, String email, String name, String password) {
        super(id);
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public User(UUID id, String email, String name, String password) {
        super(id);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(getId(), user.getId()) &&
                Objects.equal(getEmail(), user.getEmail()) &&
                Objects.equal(getName(), user.getName()) &&
                Objects.equal(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getEmail(), getName(), getPassword());
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}
