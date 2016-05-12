package com.bls.patronage.cv;

import com.bls.patronage.api.FlashcardRepresentation;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CVResponse {
    @JsonProperty
    public List<FlashcardRepresentation> flashcards;
    @JsonProperty
    public Integer status;

    public CVResponse() {
        //Jackson deserialization
    }

    public CVResponse(List<FlashcardRepresentation> flashcards, Integer status) {
        this.flashcards = flashcards;
        this.status = status;
    }

    public List<FlashcardRepresentation> getFlashcards() {
        return flashcards;
    }

    public CVResponse setFlashcards(List<FlashcardRepresentation> flashcards) {
        this.flashcards = flashcards;
        return this;
    }

    public Integer getStatus() {
        return status;
    }

    public CVResponse setStatus(Integer status) {
        this.status = status;
        return this;
    }
}

