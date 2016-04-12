package com.bls.patronage.db.model;

import java.util.UUID;

public class Deck extends IdentifiableEntity {

    private String name;
    private Boolean isPublic;
    private String authorEmail;

    public Deck() {
    }

    public Deck(UUID id, String name, String authorEmail) {
        super(id);
        this.name = name;
        this.isPublic = false;
        this.authorEmail = authorEmail;
    }

    public Deck(String id, String name, String authorEmail) {
        super(id);
        this.name = name;
        this.isPublic = false;
        this.authorEmail = authorEmail;
    }

    public Deck(UUID id, String name, Boolean isPublic, String authorEmail) {
        super(id);
        this.name = name;
        this.isPublic = isPublic;
        this.authorEmail = authorEmail;
    }

    public Deck(String id, String name, Boolean isPublic, String authorEmail) {
        super(id);
        this.name = name;
        this.isPublic = isPublic;
        this.authorEmail = authorEmail;
    }

    public Deck(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deck deck = (Deck) o;

        if (!getId().equals(deck.getId())) return false;
        if (getIsPublic() != deck.getIsPublic()) return false;
        if (!getAuthorEmail().equals(deck.authorEmail)) return false;
        return getName().equals(deck.getName());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getAuthorEmail().hashCode();
        return 31 * result + getId().hashCode();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "UUID=" + getId() +
                ", name='" + getName() + '\'' +
                ", public=" + getIsPublic() + '\'' +
                ", authorEmail=" + getAuthorEmail() +
                '}';
    }
}
