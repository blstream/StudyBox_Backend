package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    public String getQuestion() {
        return question;
    }

    @JsonProperty
    public String getAnswer() {
        return answer;
    }
}
