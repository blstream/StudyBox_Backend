package com.bls.patronage.api;

import com.bls.patronage.db.model.AuditableEntity;
import com.bls.patronage.db.model.Flashcard;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlashcardRepresentation implements DbMappable<Flashcard> {
    @NotEmpty
    @Length(max = 1000)
    private final String question;
    @NotEmpty
    @Length(max = 1000)
    private final String answer;
    @NotNull
    private final Boolean isHidden;
    private UUID id;
    private UUID deckId;
    private Integer tipsCount;
    private String questionImageURL;
    private String answerImageURL;
    private Date createdAt;
    private Date modifiedAt;
    private UUID createdBy;
    private UUID modifiedBy;

    public FlashcardRepresentation(@JsonProperty("question") String question,
                                   @JsonProperty("answer") String answer,
                                   @JsonProperty("isHidden") Boolean isHidden) {
        this.question = question;
        this.answer = answer;
        this.isHidden = isHidden;
    }

    public FlashcardRepresentation(Flashcard flashcard) {
        this.id = flashcard.getId();
        this.question = flashcard.getQuestion();
        this.answer = flashcard.getAnswer();
        this.deckId = flashcard.getDeckId();
        this.isHidden = flashcard.getIsHidden();
        this.questionImageURL = flashcard.getQuestionImageURL();
        this.answerImageURL = flashcard.getAnswerImageURL();
    }

    @Override
    public Flashcard map() {
        return new Flashcard(id, question, answer, deckId, isHidden, questionImageURL, answerImageURL);
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public UUID getId() {
        return id;
    }

    public FlashcardRepresentation setId(UUID id) {
        this.id = id;
        return this;
    }

    public Integer getTipsCount() {
        return tipsCount;
    }

    public FlashcardRepresentation setTipsCount(int tipsCount) {
        this.tipsCount = tipsCount;
        return this;
    }

    public UUID getDeckId() {
        return deckId;
    }

    public FlashcardRepresentation setDeckId(UUID deckId) {
        this.deckId = deckId;
        return this;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public String getQuestionImageURL() {
        return questionImageURL;
    }

    public FlashcardRepresentation setQuestionImageURL(String questionImageURL) {
        this.questionImageURL = questionImageURL;
        return this;
    }

    public String getAnswerImageURL() {
        return answerImageURL;
    }

    public FlashcardRepresentation setAnswerImageURL(String answerImageURL) {
        this.answerImageURL = answerImageURL;
        return this;
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

    public FlashcardRepresentation setAuditFields(AuditableEntity auditableEntity) {
        this.createdAt=auditableEntity.getCreatedAt();
        this.modifiedAt=auditableEntity.getModifiedAt();
        this.createdBy=auditableEntity.getCreatedBy();
        this.modifiedBy=auditableEntity.getModifiedBy();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashcardRepresentation that = (FlashcardRepresentation) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getQuestion(), that.getQuestion()) &&
                Objects.equals(getAnswer(), that.getAnswer()) &&
                Objects.equals(getIsHidden(), that.getIsHidden()) &&
                Objects.equals(getDeckId(), that.getDeckId()) &&
                Objects.equals(getTipsCount(), that.getTipsCount()) &&
                Objects.equals(getQuestionImageURL(), that.getQuestionImageURL()) &&
                Objects.equals(getAnswerImageURL(), that.getAnswerImageURL()) &&
                Objects.equals(getCreatedAt(), that.getCreatedAt()) &&
                Objects.equals(getModifiedAt(), that.getModifiedAt()) &&
                Objects.equals(getCreatedBy(), that.getCreatedBy()) &&
                Objects.equals(getModifiedBy(), that.getModifiedBy());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getQuestion(), getAnswer(), getIsHidden(), getDeckId(), getTipsCount(), getQuestionImageURL(), getAnswerImageURL(), getCreatedAt(), getModifiedAt(), getCreatedBy(), getModifiedBy());
    }
}
