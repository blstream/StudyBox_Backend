package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class FlashcardRepresentation {
    private final String question;
    private final String answer;

    public FlashcardRepresentation() {
        question = null;
        answer = null;
    }

    public FlashcardRepresentation(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    @JsonProperty
    @NotNull
    @Length(min = 1)
    public String getQuestion() {
        return question;
    }

    @JsonProperty
    @NotNull
    @Length(min = 1)
    public String getAnswer() {
        return answer;
    }
}
