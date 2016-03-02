package com.bls.patronage.model;

public class Deck {
    private long UUID;
    private String name;

    public Deck(String name) {
        this.name = name;
    }

    public long getUUID() {
        return UUID;
    }

    public void setUUID(long UUID) {
        this.UUID = UUID;
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

        if (getUUID() != deck.getUUID()) return false;
        return getName().equals(deck.getName());

    }

    @Override
    public int hashCode() {
        int result = (int) (getUUID() ^ (getUUID() >>> 32));
        result = 31 * result + getName().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "UUID=" + UUID +
                ", name='" + name + '\'' +
                '}';
    }
}
