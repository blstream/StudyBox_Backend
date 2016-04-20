package com.bls.patronage.api;

import com.bls.patronage.db.model.*;
import com.fasterxml.jackson.annotation.*;
import org.hibernate.validator.constraints.*;

import javax.validation.constraints.*;
import java.util.*;

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
}
