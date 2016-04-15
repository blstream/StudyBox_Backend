package com.bls.patronage.api;

import com.bls.patronage.db.model.Deck;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

public class DeckRepresentation {
    @NotEmpty
    @Length(min = 1, max = 50)
    final private String name;
    @NotNull
    final private Boolean publicVisible;

    private UUID id;
    private int flashcardsNumber;
    @Email
    private String creatorEmail;


    public DeckRepresentation(@JsonProperty("name") String name,
                              @JsonProperty("isPublic") Boolean publicVisible) {
        this.name = name;
        this.publicVisible = publicVisible;
    }

    public DeckRepresentation(Deck deck) {
        this.id = deck.getId();
        this.name = deck.getName();
        this.publicVisible = deck.getIsPublic();
    }

    public Deck buildDbModel() {
        return new Deck(id, name, publicVisible);
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

    public DeckRepresentation setFlashcardsNumber(int flashcardsNumber) {
        this.flashcardsNumber = flashcardsNumber;
        return this;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public DeckRepresentation setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
        return this;
    }

    public UUID getId() {
        return id;
    }

    public DeckRepresentation setId(UUID id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        return "DeckRepresentation{" +
                "name='" + name + '\'' +
                ", publicVisible=" + publicVisible +
                ", id=" + id +
                ", flashcardsNumber=" + flashcardsNumber +
                ", creatorEmail='" + creatorEmail + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeckRepresentation that = (DeckRepresentation) o;
        return flashcardsNumber == that.flashcardsNumber &&
                Objects.equals(name, that.name) &&
                Objects.equals(publicVisible, that.publicVisible) &&
                Objects.equals(id, that.id) &&
                Objects.equals(creatorEmail, that.creatorEmail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, publicVisible, id, flashcardsNumber, creatorEmail);
    }
}
