package com.bls.patronage.model;

public class Deck extends IdentifiableEntity {
    private String name;

    public Deck(String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Deck deck = (Deck) o;

        if (getId() != deck.getId()) return false;
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
                '}';
    }
}
