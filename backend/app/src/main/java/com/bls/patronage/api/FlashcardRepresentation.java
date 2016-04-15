package com.bls.patronage.api;

import com.bls.patronage.db.model.Flashcard;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class FlashcardRepresentation implements DbMappable<Flashcard> {
    @NotEmpty
    @Length(max = 1000)
    private final String question;
    @NotEmpty
    @Length(max = 1000)
    private final String answer;
    @NotNull
    private final Boolean isHidden;

    private UUID id;
    private UUID deckId;

    public FlashcardRepresentation(@JsonProperty("question") String question,
                                   @JsonProperty("answer") String answer,
                                   @JsonProperty("isHidden") Boolean isHidden) {
        this.question = question;
        this.answer = answer;
        this.isHidden = isHidden;
    }

    public FlashcardRepresentation(Flashcard flashcard) {
        this.id = flashcard.getId();
        this.question = flashcard.getQuestion();
        this.answer = flashcard.getAnswer();
        this.deckId = flashcard.getDeckId();
        this.isHidden = flashcard.getIsHidden();
    }

    @Override
    public Flashcard map() {
        return new Flashcard(id, question, answer, deckId, isHidden);
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public UUID getId() {
        return id;
    }

    public FlashcardRepresentation setId(UUID id) {
        this.id = id;
        return this;
    }

    public UUID getDeckId() {
        return deckId;
    }

    public FlashcardRepresentation setDeckId(UUID deckId) {
        this.deckId = deckId;
        return this;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }
}
