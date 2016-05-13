package com.bls.patronage.cv;

import com.bls.patronage.api.AcceptableFileTypes;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URI;

public class CVRequest {

    public final URI location;
    public final String action;

    public CVRequest(URI location, String action) {
        this.location = location;
        this.action = action;
    }

    public static CVRequest createRecognizeRequest(final URI publicURLToUploadedFile, String givenType) {
        return new CVRequest(
                publicURLToUploadedFile,
                givenType.equals(AcceptableFileTypes.IMAGE) ? "ImageToFlashcard" : "TextToFlashcard"
        );
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
