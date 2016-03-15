package com.bls.patronage.db.model;

import java.util.UUID;

public class DeckWithFlashcardsNumber extends IdentifiableEntity {
    private String name;
    private boolean publicAccessible;
    private int flashcardsNumber;

    public DeckWithFlashcardsNumber() {
        this.name = null;
        this.publicAccessible = false;
        flashcardsNumber = 0;
    }

    public DeckWithFlashcardsNumber(UUID id, String name,
                                    boolean publicAccessible,
                                    int flashcardsNumber) {
        super(id);
        this.name = name;
        this.publicAccessible = publicAccessible;
        this.flashcardsNumber = flashcardsNumber;
    }

    public boolean isPublic() {
        return publicAccessible;
    }

    public void setPublic(boolean publicAccessible) {
        this.publicAccessible = publicAccessible;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return flashcardsNumber;
    }

    public void setCount(int flashcardsNumber) {
        this.flashcardsNumber = flashcardsNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeckWithFlashcardsNumber deck = (DeckWithFlashcardsNumber) o;

        if (!getId().equals(deck.getId())) return false;
        if (isPublic() != deck.isPublic()) return false;
        if (getCount() != deck.getCount()) return false;
        return getName().equals(deck.getName());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getCount();
        return 31 * result + getId().hashCode();
    }

    @Override
    public String toString() {
        return "Deck{" +
                "UUID=" + getId() +
                ", name='" + getName() + '\'' +
                ", public=" + isPublic() +
                ", count=" + getCount() +
                '}';
    }
}
