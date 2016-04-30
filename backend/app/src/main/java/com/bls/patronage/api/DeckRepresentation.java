package com.bls.patronage.api;

import com.bls.patronage.db.model.Deck;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeckRepresentation implements DbMappable<Deck> {
    private UUID id;
    @NotEmpty
    @Length(min = 1, max = 50)
    final private String name;
    @NotNull
    final private Boolean publicVisible;
    private Integer flashcardsCount;
    @Email
    private String creatorEmail;
    private String creationDate;

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

    public Deck map() {
        return new Deck(id, name, publicVisible);
    }

    public Boolean isPublicVisible() {
        return publicVisible;
    }

    public String getName() {
        return name;
    }

    public Integer getFlashcardsCount() {
        return flashcardsCount;
    }

    public DeckRepresentation setFlashcardsCount(int flashcardsCount) {
        this.flashcardsCount = flashcardsCount;
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

    public String getCreationDate() {
        return creationDate;
    }

    public DeckRepresentation setCreationDate(String creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    @Override
    public String toString() {
        return "DeckRepresentation{" +
                "name='" + name + '\'' +
                ", publicVisible=" + publicVisible +
                ", id=" + id +
                ", flashcardsCount=" + flashcardsCount +
                ", creatorEmail='" + creatorEmail + '\'' +
                ", creationDate='" + creationDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeckRepresentation that = (DeckRepresentation) o;
        return flashcardsCount == that.flashcardsCount &&
                Objects.equals(name, that.name) &&
                Objects.equals(publicVisible, that.publicVisible) &&
                Objects.equals(id, that.id) &&
                Objects.equals(creatorEmail, that.creatorEmail) &&
                Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, publicVisible, id, flashcardsCount, creatorEmail, creationDate);
    }
}
