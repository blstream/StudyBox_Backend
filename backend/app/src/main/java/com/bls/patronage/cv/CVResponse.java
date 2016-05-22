package com.bls.patronage.cv;

import com.bls.patronage.api.FlashcardRepresentation;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CVResponse {
    public final List<FlashcardRepresentation> flashcards;
    public final Integer status;
    public final String errorDescription;

    @JsonCreator
    public CVResponse(@JsonProperty("flashcards") List<FlashcardRepresentation> flashcards,
                      @JsonProperty("status") Integer status,
                      @JsonProperty("error_description") String errorDescription) {
        this.flashcards = flashcards;
        this.status = status;
        this.errorDescription = errorDescription;
    }

    public List<FlashcardRepresentation> getFlashcards() {
        return flashcards;
    }

    public Integer getStatus() {
        return status;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    @Override
    public String toString() {
        return "CVResponse{" +
                "flashcards=" + flashcards +
                ", status=" + status +
                ", errorDescription='" + errorDescription + '\'' +
                '}';
    }
}

