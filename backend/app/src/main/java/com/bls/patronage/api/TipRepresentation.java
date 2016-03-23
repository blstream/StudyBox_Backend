package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;


/**
 * Created by arek on 3/22/16.
 */
public class TipRepresentation {

    @NotEmpty
    @Length(max=1000)
    private final String essence;

    @NotNull
    @Range(min=0, max=10)
    private final int difficult;

    public TipRepresentation(@JsonProperty("essence") String essence,
                             @JsonProperty("difficult") int difficult) {
        this.essence = essence;
        this.difficult = difficult;
    }

    public String getEssence() {
        return essence;
    }

    public int getDifficult() {
        return difficult;
    }

}
