package com.bls.patronage.db.model;

import java.util.UUID;

public class Flashcard extends IdentifiableEntity {
    private final String question;
    private final String answer;
    private final UUID deckId;

    public Flashcard() {
        super();
        this.question = null;
        this.answer = null;
        this.deckId = null;
    }

    public Flashcard(String question, String answer, UUID deckId) {
        this.question = question;
        this.answer = answer;
        this.deckId = deckId;
    }

    public Flashcard(String question, String answer, String deckId) {
        this.question = question;
        this.answer = answer;
        this.deckId = UUID.fromString(deckId);
    }

    public Flashcard(String id, String question, String answer, UUID deckId) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.deckId = deckId;
    }

    public Flashcard(String id, String question, String answer, String deckId) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.deckId = UUID.fromString(deckId);
    }

    public Flashcard(UUID id, String question, String answer, UUID deckId) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.deckId = deckId;
    }

    public Flashcard(UUID id, String question, String answer, String deckId) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.deckId = UUID.fromString(deckId);
    }

    public Flashcard(UUID id, UUID deckId) {
        super(id);
        this.question = null;
        this.answer = null;
        this.deckId = deckId;
    }

    public Flashcard(UUID id, String deckId) {
        super(id);
        this.question = null;
        this.answer = null;
        this.deckId = UUID.fromString(deckId);
    }

    public Flashcard(String id, UUID deckId) {
        super(id);
        this.question = null;
        this.answer = null;
        this.deckId = deckId;
    }

    public Flashcard(String id, String deckId) {
        super(id);
        this.question = null;
        this.answer = null;
        this.deckId = UUID.fromString(deckId);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flashcard flashcard = (Flashcard) o;

        if (getQuestion() != null ? !getQuestion().equals(flashcard.getQuestion()) : flashcard.getQuestion() != null)
            return false;
        if (getAnswer() != null ? !getAnswer().equals(flashcard.getAnswer()) : flashcard.getAnswer() != null)
            return false;
        return getDeckId().equals(flashcard.getDeckId());

    }

    @Override
    public int hashCode() {
        int result = getQuestion() != null ? getQuestion().hashCode() : 0;
        result = 31 * result + (getAnswer() != null ? getAnswer().hashCode() : 0);
        result = 31 * result + getDeckId().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", deckId=" + deckId +
                '}';
    }
}
