package com.bls.patronage.db.model;

import com.google.common.base.Objects;

import java.util.UUID;

public class Result extends IdentifiableEntity {
    private int correctAnswers;

    public Result() {
    }

    public Result(UUID id) {
        super(id);
    }

    public Result(String id) {
        super(id);
    }

    public Result(UUID id, int correctAnswers) {
        super(id);
        this.correctAnswers = correctAnswers;
    }

    public Result(String id, int correctAnswers) {
        super(id);
        this.correctAnswers = correctAnswers;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return getId().equals(result.getId()) && getCorrectAnswers() == result.getCorrectAnswers();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId(), getCorrectAnswers());
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + getId() +
                "correctAnswers=" + correctAnswers +
                '}';
    }
}
