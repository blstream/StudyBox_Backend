package com.bls.patronage.db.model;


import java.util.UUID;

public class Tip extends AuditableEntity {

    private String essence;
    private int difficult;
    private UUID flashcardId;
    private UUID deckId;
    private String essenceImageURL;

    public Tip(UUID id, String essence, int difficult, UUID flashcardId, UUID deckId){
        super(id);
        this.essence=essence;
        this.difficult=difficult;
        this.flashcardId=flashcardId;
        this.deckId=deckId;
    }

    public Tip(UUID id, String essence, int difficult, UUID flashcardId, UUID deckId, String essenceImageURL) {
        super(id);
        this.essence = essence;
        this.difficult = difficult;
        this.flashcardId = flashcardId;
        this.deckId = deckId;
        this.essenceImageURL = essenceImageURL;
    }

    public String getEssence() {
        return essence;
    }

    public void setEssence(String essence) {
        this.essence = essence;
    }

    public int getDifficult() {
        return difficult;
    }

    public void setDifficult(int difficult) {
        this.difficult = difficult;
    }

    public UUID getFlashcardId() {
        return flashcardId;
    }

    public void setFlashcardId(UUID flashcardId) {
        this.flashcardId = flashcardId;
    }

    public UUID getDeckId() {
        return deckId;
    }

    public void setDeckId(UUID deckId) {
        this.deckId = deckId;
    }

    public String getEssenceImageURL() {
        return essenceImageURL;
    }

    public Tip setEssenceImageURL(String essenceImageURL) {
        this.essenceImageURL = essenceImageURL;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tip tip = (Tip) o;
        return getDifficult() == tip.getDifficult() &&
                java.util.Objects.equals(getEssence(), tip.getEssence()) &&
                java.util.Objects.equals(getFlashcardId(), tip.getFlashcardId()) &&
                java.util.Objects.equals(getDeckId(), tip.getDeckId()) &&
                java.util.Objects.equals(getEssenceImageURL(), tip.getEssenceImageURL());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(getEssence(), getDifficult(), getFlashcardId(), getDeckId(), getEssenceImageURL());
    }

    @Override
    public String toString() {
        return "Tip{" +
                "essence='" + essence + '\'' +
                ", difficult=" + difficult +
                ", flashcardId=" + flashcardId +
                ", deckId=" + deckId +
                ", essenceImageURL='" + essenceImageURL + '\'' +
                '}';
    }
}
