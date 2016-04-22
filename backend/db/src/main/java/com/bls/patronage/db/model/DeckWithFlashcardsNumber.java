package com.bls.patronage.db.model;

import java.util.UUID;

public class DeckWithFlashcardsNumber extends Deck {
    private int flashcardsNumber;

    public DeckWithFlashcardsNumber() {
    }

    public DeckWithFlashcardsNumber(UUID id,
                                    String name,
                                    boolean isPublic,
                                    String creatorEmail,
                                    int flashcardsNumber) {
        super(id, name, isPublic, creatorEmail);
        this.flashcardsNumber = flashcardsNumber;
    }

    public DeckWithFlashcardsNumber(Deck deck, int flashcardsNumber) {
        super(deck.getId(), deck.getName(), deck.getIsPublic(), deck.getCreatorEmail());
        this.flashcardsNumber = flashcardsNumber;
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
        if(getCreatorEmail().equals(deck.getCreatorEmail())) return true;
        if (getCount() != deck.getCount()) return false;
        return getName().equals(deck.getName());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getCount();
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
                ", count=" + getCount() +
                '}';
    }
}
