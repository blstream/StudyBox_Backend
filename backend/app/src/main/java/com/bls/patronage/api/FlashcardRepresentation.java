package com.bls.patronage.api;

import com.bls.patronage.db.model.Flashcard;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.UUID;

public class FlashcardRepresentation implements DbModelRepresentation<Flashcard> {
    @NotEmpty
    @Length(max = 1000)
    private final String question;
    @NotEmpty
    @Length(max = 1000)
    private final String answer;

    private UUID id;
    private UUID deckId;

    public FlashcardRepresentation(@JsonProperty("question") String question, @JsonProperty("answer") String answer) {
        this.question = question;
        this.answer = answer;
    }

    public FlashcardRepresentation(Flashcard flashcard) {
        this.id = flashcard.getId();
        this.question = flashcard.getQuestion();
        this.answer = flashcard.getAnswer();
        this.deckId = flashcard.getDeckId();
    }

    @Override
    public Flashcard buildDbModel() {
        return new Flashcard(id, question, answer, deckId);
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
}
