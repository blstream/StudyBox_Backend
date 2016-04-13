package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

public class FlashcardRepresentation {
    @NotEmpty
    @Length(max = 1000)
    private final String question;
    @NotEmpty
    @Length(max = 1000)
    private final String answer;
    @NotNull
    private final Boolean isHidden;

    public FlashcardRepresentation(@JsonProperty("question") String question,
                                   @JsonProperty("answer") String answer,
                                   @JsonProperty("isHidden") Boolean isHidden) {
        this.question = question;
        this.answer = answer;
        this.isHidden = isHidden;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public Boolean getIsHidden() {
        return isHidden;
    }
}
