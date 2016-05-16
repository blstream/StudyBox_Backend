package com.bls.patronage.api;

import com.bls.patronage.db.model.Tip;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TipRepresentation implements DbMappable<Tip> {
    @NotEmpty
    @Length(max = 1000)
    private final String essence;
    @NotNull
    @Range(min = 0, max = 10)
    private final Integer difficult;
    private UUID id;
    private UUID flashcardId;
    private UUID deckId;
    private String essenceImageURL;


    public TipRepresentation(@JsonProperty("essence") String essence,
                             @JsonProperty("difficult") int difficult) {
        this.essence = essence;
        this.difficult = difficult;
    }

    public TipRepresentation(Tip tip) {
        this.essence = tip.getEssence();
        this.difficult = tip.getDifficult();
        this.deckId = tip.getDeckId();
        this.flashcardId = tip.getFlashcardId();
        this.id = tip.getId();
        this.essenceImageURL = tip.getEssenceImageURL();
    }

    @Override
    public Tip map() {
        return new Tip(id, essence, difficult, flashcardId, deckId);
    }

    public String getEssence() {
        return essence;
    }

    public int getDifficult() {
        return difficult;
    }

    public UUID getDeckId() {
        return deckId;
    }

    public TipRepresentation setDeckId(UUID deckId) {
        this.deckId = deckId;
        return this;
    }

    public UUID getFlashcardId() {
        return flashcardId;
    }

    public TipRepresentation setFlashcardId(UUID flashcardId) {
        this.flashcardId = flashcardId;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public TipRepresentation setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getEssenceImageURL() {
        return essenceImageURL;
    }

    public TipRepresentation setEssenceImageURL(String essenceImageURL) {
        this.essenceImageURL = essenceImageURL;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TipRepresentation that = (TipRepresentation) o;
        return Objects.equals(getEssence(), that.getEssence()) &&
                Objects.equals(getDifficult(), that.getDifficult()) &&
                Objects.equals(getId(), that.getId()) &&
                Objects.equals(getFlashcardId(), that.getFlashcardId()) &&
                Objects.equals(getDeckId(), that.getDeckId()) &&
                Objects.equals(getEssenceImageURL(), that.getEssenceImageURL());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEssence(), getDifficult(), getId(), getFlashcardId(), getDeckId(), getEssenceImageURL());
    }
}
