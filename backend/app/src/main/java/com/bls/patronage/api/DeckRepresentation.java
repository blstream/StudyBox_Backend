package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class DeckRepresentation {

    @NotEmpty
    @JsonProperty
    final private String name;

    @JsonProperty
    final private boolean isPublic;

    public DeckRepresentation() {
        this.name = null;
        this.isPublic = false;
    }

    public DeckRepresentation(String name, boolean publicAccessible) {
        this.name = name;
        this.isPublic = publicAccessible;
    }

    public boolean getIsPublic() {
        return isPublic;
    }

    public String getName() {
        return name;
    }

}
