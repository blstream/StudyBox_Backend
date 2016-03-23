package com.bls.patronage.db.model;


import java.util.UUID;
import com.google.common.base.Objects;

/**
 * Created by arek on 3/22/16.
 */
public class Tip extends IdentifiableEntity {

    private String essence;
    private int difficult;
    private UUID flashcardId;
    private UUID deckId;

    public Tip(){
    }

    public Tip(UUID id, String essence, int difficult, UUID flashcardId, UUID deckId){
        super(id);
        this.essence=essence;
        this.difficult=difficult;
        this.flashcardId=flashcardId;
        this.deckId=deckId;
    }

    public Tip(String id, String essence, int difficult, UUID flashcardId, UUID deckId){
        super(id);
        this.essence=essence;
        this.difficult=difficult;
        this.flashcardId=flashcardId;
        this.deckId=deckId;
    }
    public Tip(UUID id, String essence, int difficult, String flashcardId, UUID deckId){
        super(id);
        this.essence=essence;
        this.difficult=difficult;
        this.flashcardId=UUID.fromString(flashcardId);
        this.deckId=deckId;
    }

    public Tip(String id, String essence, int difficult, String flashcardId, UUID deckId){
        super(id);
        this.essence=essence;
        this.difficult=difficult;
        this.flashcardId=UUID.fromString(flashcardId);
        this.deckId=deckId;
    }

    public Tip(UUID id, String essence, int difficult, UUID flashcardId, String deckId){
        super(id);
        this.essence=essence;
        this.difficult=difficult;
        this.flashcardId=flashcardId;
        this.deckId=UUID.fromString(deckId);
    }

    public Tip(String id, String essence, int difficult, UUID flashcardId, String deckId){
        super(id);
        this.essence=essence;
        this.difficult=difficult;
        this.flashcardId=flashcardId;
        this.deckId=UUID.fromString(deckId);
    }
    public Tip(UUID id, String essence, int difficult, String flashcardId, String deckId){
        super(id);
        this.essence=essence;
        this.difficult=difficult;
        this.flashcardId=UUID.fromString(flashcardId);
        this.deckId=UUID.fromString(deckId);
    }

    public Tip(String id, String essence, int difficult, String flashcardId, String deckId){
        super(id);
        this.essence=essence;
        this.difficult=difficult;
        this.flashcardId=UUID.fromString(flashcardId);
        this.deckId=UUID.fromString(deckId);
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tip tip = (Tip) o;

        if (!getId().equals(tip.getId())) return false;
        if (getDifficult()!=tip.getDifficult()) return false;
        if (!getFlashcardId().equals(tip.getFlashcardId())) return false;
        if (!getDeckId().equals(tip.getDeckId())) return false;
        return getEssence().equals(tip.getEssence());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getEssence(), getDifficult(), getFlashcardId(), getDeckId());
    }

    @Override
    public String toString() {
        return "Tip{" +
                "essence='" + essence + '\'' +
                ", difficult='" + difficult + '\'' +
                ", flashcardId=" + flashcardId + '\'' +
                ", deckId=" + deckId +
                '}';
    }



}
