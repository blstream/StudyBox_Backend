package com.bls.patronage.model;

public class Flashcard extends StudyBoxEntity {
    private String question;
    private String answer;

    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
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

        if (!getQuestion().equals(flashcard.getQuestion())) return false;
        return getAnswer().equals(flashcard.getAnswer());

    }

    @Override
    public int hashCode() {
        int result = getQuestion().hashCode();
        result = 31 * result + getAnswer().hashCode();
        result = 31 * result + getId().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "id='" + getId() + "\'" +
                ", question='" + getQuestion() + '\'' +
                ", answer='" + getAnswer() + '\'' +
                '}';
    }
}
