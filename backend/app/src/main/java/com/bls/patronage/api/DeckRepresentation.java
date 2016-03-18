package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class DeckRepresentation {

    @NotEmpty
    @Length(min = 1, max = 50)
    final private String name;

    final private Boolean isPublic;

    public DeckRepresentation(@JsonProperty("name") String name,
                              @JsonProperty("isPublic") Boolean isPublic) {
        this.name = name;
        this.isPublic = isPublic;
    }

    public Boolean getIsPublic() {
        return isPublic;
    }

    public String getName() {
        return name;
    }

}
