package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FlashcardRepresentation {

    private String question;
    private String answer;

    public FlashcardRepresentation() {
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
