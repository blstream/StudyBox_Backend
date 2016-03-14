package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeckRepresentation {

    @JsonProperty
    final private String name;
    @JsonProperty("public")
    final private boolean publicAccessible;

    public DeckRepresentation() {
        this.name = null;
        this.publicAccessible = false;
    }

    public DeckRepresentation(String name) {
        this.name = name;
        this.publicAccessible = false;
    }

    public DeckRepresentation(String name, boolean publicAccessible) {
        this.name = name;
        this.publicAccessible = publicAccessible;
    }


    public boolean isPublic() { return publicAccessible; }

    public String getName() {
        return name;
    }

}
