package com.bls.patronage.db.model;

import com.google.common.base.Objects;

import java.util.UUID;

public class User extends UserWithoutId {
    private String password;

    public User(String id, String email, String name, String password) {
        super(id, email, name);
        this.password = password;
    }

    public User(UUID id, String email, String name, String password) {
        super(id, email, name);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (super.equals(o)) {
            return Objects.equal(getPassword(), ((User) o).getPassword());
        } else {
            return false;
        }
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
