package com.bls.patronage.helpers;

import com.bls.patronage.Message;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public class CVMessage implements Message {

    public final URI location;
    public final String action;

    public CVMessage(URI location, String action) {
        this.location = location;
        this.action = action;
    }

    @JsonProperty
    public URI getLocation() {
        return location;
    }

    @JsonProperty
    public String getAction() {
        return action;
    }
}