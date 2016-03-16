package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class DeckRepresentation {

    @NotEmpty
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
