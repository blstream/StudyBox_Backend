package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

public class FlashcardRepresentation {
    @Length(min = 1, max = 1000)
    private final String question;
    @Length(min = 1, max = 1000)
    private final String answer;

    public FlashcardRepresentation(@JsonProperty("question") String question, @JsonProperty("answer") String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
