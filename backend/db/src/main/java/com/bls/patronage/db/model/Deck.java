package com.bls.patronage.db.model;

import java.sql.Timestamp;
import java.util.UUID;

public class Deck extends AuditableEntity {
    private String name;
    private Boolean isPublic;

    public Deck() {
    }

    public Deck(UUID id, String name) {
        super(id);
        this.name = name;
        this.isPublic = false;
    }

    public Deck(String id, String name) {
        super(id);
        this.name = name;
        this.isPublic = false;
    }

    public Deck(UUID id, String name, Boolean isPublic) {
        super(id);
        this.name = name;
        this.isPublic = isPublic;
    }

    public Deck(UUID id, String name, Boolean isPublic, Timestamp createdAt, Timestamp modifiedAt, UUID createdBy, UUID modifiedBy) {
        super(id, createdAt, modifiedAt, createdBy, modifiedBy);
        this.name = name;
        this.isPublic = isPublic;
    }

    public Deck(String id, String name, Boolean isPublic) {
        super(id);
        this.name = name;
        this.isPublic = isPublic;
    }

    public Deck(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Deck setName(String name) {
        this.name = name;
        return this;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public Deck setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deck deck = (Deck) o;

        if (!getId().equals(deck.getId())) return false;
        if (getIsPublic() != deck.getIsPublic()) return false;
        return getName().equals(deck.getName());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        return 31 * result + getId().hashCode();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "UUID=" + getId() +
                ", name='" + getName() + '\'' +
                ", public=" + getIsPublic() +
                '}';
    }
}
