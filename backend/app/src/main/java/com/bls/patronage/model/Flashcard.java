package com.bls.patronage.model;

public class Flashcard {
    private long UUID;
    private String question;
    private String answer;

    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public long getUUID() {
        return UUID;
    }

    public void setUUID(long UUID) {
        this.UUID = UUID;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Flashcard flashcard = (Flashcard) o;

        if (getUUID() != flashcard.getUUID()) return false;
        if (!getQuestion().equals(flashcard.getQuestion())) return false;
        return getAnswer().equals(flashcard.getAnswer());

    }

    @Override
    public int hashCode() {
        int result = (int) (getUUID() ^ (getUUID() >>> 32));
        result = 31 * result + getQuestion().hashCode();
        result = 31 * result + getAnswer().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "UUID=" + UUID +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
