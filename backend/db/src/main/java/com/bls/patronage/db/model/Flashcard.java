package com.bls.patronage.db.model;

import java.util.UUID;

public class Flashcard extends IdentifiableEntity {
    private final String question;
    private final String answer;
    private final UUID deckID;

    public Flashcard(String question, String answer, UUID deckID) {
        this.question = question;
        this.answer = answer;
        this.deckID = deckID;
    }

    public Flashcard(String id, String question, String answer, UUID deckID) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.deckID = deckID;
    }

    public Flashcard(UUID id, String question, String answer, UUID deckID) {
        super(id);
        this.question = question;
        this.answer = answer;
        this.deckID = deckID;
    }

    public Flashcard(String id, UUID deckID) {
        super(id);
        this.deckID = deckID;
        question = null;
        answer = null;
    }

    public Flashcard(UUID id, UUID deckID) {
        super(id);
        this.deckID = deckID;
        question = null;
        answer = null;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public UUID getDeckID() {
        return deckID;
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
        return getDeckID().equals(flashcard.getDeckID());

    }

    @Override
    public int hashCode() {
        int result = getQuestion() != null ? getQuestion().hashCode() : 0;
        result = 31 * result + (getAnswer() != null ? getAnswer().hashCode() : 0);
        result = 31 * result + getDeckID().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", deckID=" + deckID +
                '}';
    }
}
