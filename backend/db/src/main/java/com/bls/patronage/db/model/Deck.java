package com.bls.patronage.db.model;

import java.util.UUID;

public class Deck extends IdentifiableEntity {
    private String name;
    private boolean publicAccessible;

    public Deck() {
    }

    public Deck(UUID id, String name) {
        super(id);
        this.name = name;
        this.publicAccessible = false;
    }

    public Deck(String id, String name) {
        super(id);
        this.name = name;
        this.publicAccessible = false;
    }

    public Deck(UUID id, String name, boolean publicAccessible) {
        super(id);
        this.name = name;
        this.publicAccessible = publicAccessible;
    }

    public Deck(String id, String name, boolean publicAccessible) {
        super(id);
        this.name = name;
        this.publicAccessible = publicAccessible;
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

    public boolean isPublic() {
        return publicAccessible;
    }

    public void setPublic(boolean publicAccessible) {
        this.publicAccessible = publicAccessible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deck deck = (Deck) o;

        if (!getId().equals(deck.getId())) return false;
        if (isPublic() != deck.isPublic()) return false;
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
                ", public=" + isPublic() +
                '}';
    }
}
