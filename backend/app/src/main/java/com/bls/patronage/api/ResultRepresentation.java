package com.bls.patronage.api;

import com.bls.patronage.db.model.Result;
import com.fasterxml.jackson.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultRepresentation implements DbMappable<Result> {
    @NotNull
    private UUID flashcardId;

    private Boolean isCorrectAnswer;
    private Integer correctAnswers;

    public ResultRepresentation() {
    }

    public ResultRepresentation(@JsonProperty("flashcardId") UUID flashcardId,
                                @JsonProperty("isCorrectAnswer") @NotNull Boolean isCorrectAnswer) {
        this.flashcardId = flashcardId;
        this.isCorrectAnswer = isCorrectAnswer;
    }

    public ResultRepresentation(UUID flashcardId, Integer correctAnswers) {
        this.flashcardId = flashcardId;
        this.correctAnswers = correctAnswers;
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


    public UUID getId() {
        return flashcardId;
    }

    public ResultRepresentation setId(UUID flashcardId) {
        this.flashcardId = flashcardId;
        return this;
    }

    public Boolean getCorrectAnswer() {
        return isCorrectAnswer;
    }

    public ResultRepresentation setCorrectAnswer(Boolean correctAnswer) {
        isCorrectAnswer = correctAnswer;
        return this;
    }

    public Integer getCorrectAnswers() {
        return correctAnswers;
    }

    public ResultRepresentation setCorrectAnswers(Integer correctAnswers) {
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
