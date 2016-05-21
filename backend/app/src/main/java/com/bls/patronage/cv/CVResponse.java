package com.bls.patronage.cv;

import com.bls.patronage.api.FlashcardRepresentation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CVResponse {
    @JsonProperty
    public List<FlashcardRepresentation> flashcards;
    @JsonProperty
    public Integer status;
    @JsonProperty
    public String error_description;

    public CVResponse() {
        //Jackson deserialization
    }

    public CVResponse(List<FlashcardRepresentation> flashcards, Integer status) {
        this.flashcards = flashcards;
        this.status = status;
    }

    public CVResponse(Integer status, String error_description) {
        this.status = status;
        this.error_description = error_description;
    }

    public CVResponse(List<FlashcardRepresentation> flashcards, Integer status, String error_description) {
        this.flashcards = flashcards;
        this.status = status;
        this.error_description = error_description;
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

    public String getError_description() {
        return error_description;
    }

    public CVResponse setError_description(String error_description) {
        this.error_description = error_description;
        return this;
    }

    @Override
    public String toString() {
        return "CVResponse{" +
                "flashcards=" + flashcards +
                ", status=" + status +
                ", error_description='" + error_description + '\'' +
                '}';
    }
}

