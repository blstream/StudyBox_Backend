package com.bls.patronage.db.model;

import java.util.UUID;

public class Deck extends IdentifiableEntity {
    private String name;
    private boolean isPublic;

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

    public Deck(UUID id, String name, boolean isPublic) {
        super(id);
        this.name = name;
        this.isPublic = isPublic;
    }

    public Deck(String id, String name, boolean isPublic) {
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

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
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
