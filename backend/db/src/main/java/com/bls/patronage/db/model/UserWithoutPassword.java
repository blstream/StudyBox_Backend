package com.bls.patronage.db.model;

import com.google.common.base.Objects;

import java.util.UUID;

public class UserWithoutPassword extends IdentifiableEntity {
    private String email;
    private String name;

    public UserWithoutPassword(String id, String email, String name) {
        super(id);
        this.email = email;
        this.name = name;
    }

    public UserWithoutPassword(UUID id, String email, String name) {
        super(id);
        this.email = email;
        this.name = name;
    }

    public UserWithoutPassword() {
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserWithoutPassword user = (UserWithoutPassword) o;
        return Objects.equal(getId(), user.getId()) &&
                Objects.equal(getEmail(), user.getEmail()) &&
                Objects.equal(getName(), user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getEmail(), getName());
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + getId() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", name='" + getName() + '\'' +
                '}';
    }
}
