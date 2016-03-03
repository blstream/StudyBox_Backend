package com.bls.patronage.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeckRepresentation {

    private String name;

    public DeckRepresentation() {
    }

    public DeckRepresentation(String name) {
        this.name = name;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

}
