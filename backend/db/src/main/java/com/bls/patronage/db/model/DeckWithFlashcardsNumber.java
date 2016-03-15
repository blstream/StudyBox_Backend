package com.bls.patronage.db.model;

import java.util.UUID;

public class DeckWithFlashcardsNumber extends IdentifiableEntity {
    private String name;
    private boolean isPublic;
    private int flashcardsNumber;

    public DeckWithFlashcardsNumber() {
        this.name = null;
        this.isPublic = false;
        flashcardsNumber = 0;
    }

    public DeckWithFlashcardsNumber(UUID id, String name,
                                    boolean isPublic,
                                    int flashcardsNumber) {
        super(id);
        this.name = name;
        this.isPublic = isPublic;
        this.flashcardsNumber = flashcardsNumber;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
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
        if (getIsPublic() != deck.getIsPublic()) return false;
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
                ", public=" + getIsPublic() +
                ", count=" + getCount() +
                '}';
    }
}
