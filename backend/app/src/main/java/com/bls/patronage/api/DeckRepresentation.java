package com.bls.patronage.api;

import com.bls.patronage.db.model.AuditableEntity;
import com.bls.patronage.db.model.Deck;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Date;
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
    private final Date createdAt;
    private final Date modifiedAt;
    private final UUID createdBy;
    private final UUID modifiedBy;

    private DeckRepresentation(DeckRepresentationBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.isPublic = builder.isPublic;
        this.flashcardsCount = builder.flashcardsCount;
        this.creationDate = builder.creationDate;
        this.creatorEmail = builder.creatorEmail;
        this.createdAt = builder.createdAt;
        this.modifiedAt = builder.modifiedAt;
        this.createdBy = builder.createdBy;
        this.modifiedBy = builder.modifiedBy;
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

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getModifiedAt() {
        return modifiedAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
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

        private Date createdAt;
        private Date modifiedAt;
        private UUID createdBy;
        private UUID modifiedBy;

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

        public DeckRepresentationBuilder withcreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DeckRepresentationBuilder withmodifiedAt(Timestamp modifiedAt) {
            this.modifiedAt = modifiedAt;
            return this;
        }

        public DeckRepresentationBuilder withcreatedBy(UUID createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public DeckRepresentationBuilder withModifiedBy(UUID modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this;
        }

        public DeckRepresentationBuilder withAuditFields(AuditableEntity auditableEntity) {
            this.createdAt = auditableEntity.getCreatedAt();
            this.modifiedAt = auditableEntity.getModifiedAt();
            this.createdBy = auditableEntity.getCreatedBy();
            this.modifiedBy = auditableEntity.getModifiedBy();
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
                ", createdAt='" + createdAt + '\'' +
                ", modifiedAt='" + modifiedAt + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", modifiedBy='" + modifiedBy + '\'' +
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
                Objects.equals(creationDate, that.creationDate) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(modifiedAt, that.modifiedAt) &&
                Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(modifiedBy, that.modifiedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isPublic, id, flashcardsCount, creatorEmail, creationDate, createdAt, modifiedAt, createdBy, modifiedBy);
    }
}

