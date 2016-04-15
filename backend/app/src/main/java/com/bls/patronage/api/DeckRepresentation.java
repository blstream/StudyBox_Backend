package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class DeckRepresentation {

    @NotEmpty
    @Length(min = 1, max = 50)
    final private String name;
    @NotNull
    final private Boolean publicVisible;

    private int flashcardsNumber;
    @Email
    private String creatorEmail;


    public DeckRepresentation(@JsonProperty("name") String name,
                              @JsonProperty("isPublic") Boolean publicVisible) {
        this.name = name;
        this.publicVisible = publicVisible;
    }


    public Boolean isPublicVisible() {
        return publicVisible;
    }

    public String getName() {
        return name;
    }

    public int getFlashcardsNumber() {
        return flashcardsNumber;
    }

    public void setFlashcardsNumber(int flashcardsNumber) {
        this.flashcardsNumber = flashcardsNumber;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }
}
