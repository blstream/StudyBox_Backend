package com.bls.patronage.api;

import com.bls.patronage.db.model.Deck;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JsonDeserialize(builder = DeckRepresentation.DeckRepresentationBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeckRepresentation implements DbMappable<Deck> {
    private final UUID id;
    private final String name;
    private final Boolean isPublic;
    private final Integer flashcardsCount;
    private final String creatorEmail;
    private final String creationDate;

    private DeckRepresentation(DeckRepresentationBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.isPublic = builder.isPublic;
        this.flashcardsCount = builder.flashcardsCount;
        this.creationDate = builder.creationDate;
        this.creatorEmail = builder.creatorEmail;
    }

    public Deck map() {
        return new Deck(id, name, isPublic);
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public String getName() {
        return name;
    }

    public Integer getFlashcardsCount() {
        return flashcardsCount;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public UUID getId() {
        return id;
    }

    public String getCreationDate() {
        return creationDate;
    }

    @JsonPOJOBuilder
    public static class DeckRepresentationBuilder {

        @NotEmpty
        @Length(min = 1, max = 50)
        private final String name;
        @NotNull
        private final Boolean isPublic;
        private UUID id;
        private Integer flashcardsCount;
        @Email
        private String creatorEmail;
        private String creationDate;

        public DeckRepresentationBuilder(@JsonProperty("name") String name,
                                         @JsonProperty("isPublic") Boolean isPublic) {
            this.name = name;
            this.isPublic = isPublic;
        }

        public DeckRepresentationBuilder(Deck deck) {
            this.id = deck.getId();
            this.name = deck.getName();
            this.isPublic = deck.getIsPublic();
        }

        public DeckRepresentationBuilder withId(UUID id) {
            this.id = id;
            return this;
        }

        public DeckRepresentationBuilder withFlashcardsCount(Integer flashcardsCount) {
            this.flashcardsCount = flashcardsCount;
            return this;
        }

        public DeckRepresentationBuilder withCreatorEmail(String creatorEmail) {
            this.creatorEmail = creatorEmail;
            return this;
        }

        public DeckRepresentationBuilder withCreationDate(String creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public DeckRepresentation build() {
            return new DeckRepresentation(this);
        }
    }

    @Override
    public String toString() {
        return "DeckRepresentation{" +
                "name='" + name + '\'' +
                ", isPublic=" + isPublic +
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
                Objects.equals(isPublic, that.isPublic) &&
                Objects.equals(id, that.id) &&
                Objects.equals(creatorEmail, that.creatorEmail) &&
                Objects.equals(creationDate, that.creationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isPublic, id, flashcardsCount, creatorEmail, creationDate);
    }
}
