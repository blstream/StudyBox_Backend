package com.bls.patronage.api;

import com.bls.patronage.db.model.Result;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

public class ResultRepresentation implements DbMappable<Result> {
    @NotNull
    private UUID flashcardId;
    @NotNull
    private boolean isCorrectAnswer;

    private int correctAnswers;

    public ResultRepresentation() {
    }

    public ResultRepresentation(@JsonProperty("flashcardId") UUID flashcardId,
                                @JsonProperty("isCorrectAnswer") boolean isCorrectAnswer) {
        this.flashcardId = flashcardId;
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public ResultRepresentation readFromDbModel(Result result) {
        this.flashcardId = result.getId();
        this.correctAnswers = result.getCorrectAnswers();
        return this;
    }

    @Override
    public Result map() {
        return new Result(flashcardId, correctAnswers);
    }

    public UUID getFlashcardId() {
        return flashcardId;
    }

    @JsonProperty("isCorrectAnswer")
    public boolean isCorrectAnswer() {
        return isCorrectAnswer;
    }

    public UUID getId() {
        return flashcardId;
    }

    public ResultRepresentation setId(UUID id) {
        this.flashcardId = id;
        return this;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public ResultRepresentation setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResultRepresentation that = (ResultRepresentation) o;
        return isCorrectAnswer == that.isCorrectAnswer &&
                correctAnswers == that.correctAnswers &&
                Objects.equals(flashcardId, that.flashcardId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flashcardId, isCorrectAnswer, correctAnswers);
    }
}
