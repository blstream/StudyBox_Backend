package com.bls.patronage.db.model;

import java.util.Objects;
import java.util.UUID;

public class Flashcard extends IdentifiableEntity {
    private final String question;
    private final String answer;
    private final UUID deckId;
    private final Boolean isHidden;
    private String questionImageURL;
    private Boolean answerImageURL;

    public Flashcard() {
        super();
        this.question = null;
        this.answer = null;
        this.deckId = null;
        this.isHidden = null;
    }

    public Flashcard(String id, String question, String answer, UUID deckId, Boolean isHidden) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.deckId = deckId;
        this.isHidden = isHidden;
    }

    public Flashcard(String id, String question, String answer, String deckId, Boolean isHidden) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.deckId = UUID.fromString(deckId);
        this.isHidden = isHidden;
    }

    public Flashcard(UUID id, String question, String answer, UUID deckId, Boolean isHidden) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.deckId = deckId;
        this.isHidden = isHidden;
    }

    public Flashcard(UUID id, String question, String answer, String deckId, Boolean isHidden) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.deckId = UUID.fromString(deckId);
        this.isHidden = isHidden;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public UUID getDeckId() {
        return deckId;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }

    public String getQuestionImageURL() {
        return questionImageURL;
    }

    public Flashcard setQuestionImageURL(String questionImageURL) {
        this.questionImageURL = questionImageURL;
        return this;
    }

    public Boolean getAnswerImageURL() {
        return answerImageURL;
    }

    public Flashcard setAnswerImageURL(Boolean answerImageURL) {
        this.answerImageURL = answerImageURL;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flashcard flashcard = (Flashcard) o;
        return Objects.equals(getQuestion(), flashcard.getQuestion()) &&
                Objects.equals(getAnswer(), flashcard.getAnswer()) &&
                Objects.equals(getDeckId(), flashcard.getDeckId()) &&
                Objects.equals(getIsHidden(), flashcard.getIsHidden()) &&
                Objects.equals(getQuestionImageURL(), flashcard.getQuestionImageURL()) &&
                Objects.equals(getAnswerImageURL(), flashcard.getAnswerImageURL());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getQuestion(), getAnswer(), getDeckId(), getIsHidden(), getQuestionImageURL(), getAnswerImageURL());
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", deckId=" + deckId +
                ", isHidden=" + isHidden +
                ", questionImageURL='" + questionImageURL + '\'' +
                ", answerImageURL=" + answerImageURL +
                '}';
    }
}
