package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class ResultRepresentation {
    private UUID flashcardId;
    private boolean isCorrectAnswer;

    public ResultRepresentation(@JsonProperty("flashcardId") UUID flashcardId,
                                @JsonProperty("isCorrectAnswer") boolean correctAnswer) {
        this.flashcardId = flashcardId;
        this.isCorrectAnswer = correctAnswer;
    }

    public UUID getFlashcardId() {
        return flashcardId;
    }

    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }
}
