package com.bls.patronage.api;

import com.bls.patronage.db.model.Flashcard;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FlashcardRepresentation implements DbMappable<Flashcard> {
    private UUID id;
    @NotEmpty
    @Length(max = 1000)
    private final String question;
    @NotEmpty
    @Length(max = 1000)
    private final String answer;
    @NotNull
    private final Boolean isHidden;

    private UUID deckId;
    private Integer tipsCount;

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
    }

    @Override
    public Flashcard map() {
        return new Flashcard(id, question, answer, deckId, isHidden);
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

    public Integer getTipsCount() {
        return tipsCount;
    }

    public FlashcardRepresentation setTipsCount(int tipsCount) {
        this.tipsCount = tipsCount;
        return this;
    }

    public FlashcardRepresentation setId(UUID id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlashcardRepresentation that = (FlashcardRepresentation) o;
        return Objects.equals(question, that.question) &&
                Objects.equals(answer, that.answer) &&
                Objects.equals(id, that.id) &&
                Objects.equals(deckId, that.deckId) &&
                Objects.equals(isHidden, that.isHidden) &&
                Objects.equals(tipsCount, that.tipsCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, answer, id, deckId, isHidden, tipsCount);
    }
}
