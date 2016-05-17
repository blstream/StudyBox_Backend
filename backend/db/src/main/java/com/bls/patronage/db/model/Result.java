package com.bls.patronage.db.model;

import com.google.common.base.Objects;

import java.util.UUID;

public class Result extends AuditableEntity {
    private int correctAnswers;
    private UUID userId;

    public Result() {
    }

    public Result(UUID id) {
        super(id);
    }

    public Result(String id) {
        super(id);
    }

    public Result(UUID id, int correctAnswers, UUID userId) {
        super(id);
        this.correctAnswers = correctAnswers;
        this.userId = userId;
    }

    public Result(String id, int correctAnswers, UUID userId) {
        super(id);
        this.correctAnswers = correctAnswers;
        this.userId = userId;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public UUID getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return getId().equals(result.getId()) &&
                getCorrectAnswers() == result.getCorrectAnswers() &&
                getUserId().equals(result.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getCorrectAnswers(), getUserId());
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + getId() +
                "correctAnswers=" + correctAnswers +
                '}';
    }
}
