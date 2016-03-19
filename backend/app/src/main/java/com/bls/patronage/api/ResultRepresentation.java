package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class ResultRepresentation {
    @NotNull
    private UUID flashcardId;
    @NotNull
    private boolean isCorrectAnswer;

    public ResultRepresentation(@JsonProperty("flashcardId") UUID flashcardId,
                                @JsonProperty("isCorrectAnswer") boolean isCorrectAnswer) {
        this.flashcardId = flashcardId;
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public UUID getFlashcardId() {
        return flashcardId;
    }

    @JsonProperty("isCorrectAnswer")
    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }
}
