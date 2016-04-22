package com.bls.patronage.db.model;

import java.util.UUID;

public class Deck extends IdentifiableEntity {
    private String name;
    private Boolean isPublic;
    private String creatorEmail;

    public Deck() {
    }

    public Deck(UUID id, String name, String creatorEmail) {
        super(id);
        this.name = name;
        this.isPublic = false;
        this.creatorEmail=creatorEmail;
    }

    public Deck(String id, String name, String creatorEmail) {
        super(id);
        this.name = name;
        this.isPublic = false;
        this.creatorEmail=creatorEmail;
    }

    public Deck(UUID id, String name, Boolean isPublic, String creatorEmail) {
        super(id);
        this.name = name;
        this.isPublic = isPublic;
        this.creatorEmail=creatorEmail;
    }

    public Deck(String id, String name, Boolean isPublic, String creatorEmail) {
        super(id);
        this.name = name;
        this.isPublic = isPublic;
        this.creatorEmail=creatorEmail;
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

    public String getCreatorEmail(){ return creatorEmail;}

    public void setCreatorEmail(String creatorEmail){this.creatorEmail=creatorEmail;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deck deck = (Deck) o;

        if (!getId().equals(deck.getId())) return false;
        if (getIsPublic() != deck.getIsPublic()) return false;
        if(!getCreatorEmail().equals(deck.getCreatorEmail())) return false;
        return getName().equals(deck.getName());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = result*31 + getCreatorEmail().hashCode();
        return 31 * result + getId().hashCode();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "UUID=" + getId() +
                ", name='" + getName() + '\'' +
                ", public=" + getIsPublic() + '\'' +
                ", creatorEmail=" + getCreatorEmail() +
                '}';
    }
}
