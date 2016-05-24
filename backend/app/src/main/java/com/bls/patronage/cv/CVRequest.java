package com.bls.patronage.cv;

import com.bls.patronage.api.AcceptableFileType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class CVRequest {

    public String url;
    public String action;

    public CVRequest() {
        //jackson deserialization
    }

    public CVRequest(String url, String action) {
        this.url = url;
        this.action = action;
    }

    public static CVRequest createRecognizeRequest(final URL publicURLToUploadedFile, String fileType) {
        return new CVRequest(
                publicURLToUploadedFile.toString(),
                fileType.equals(AcceptableFileType.IMAGE.toString()) ? "ImageToFlashcard" : "TextToFlashcard"
        );
    }

    @JsonProperty
    public String getUrl() {
        return url;
    }

    @JsonProperty
    public String getAction() {
        return action;
    }

    @Override
    public String toString() {
        return "CVRequest{" +
                "url=" + url +
                ", action='" + action + '\'' +
                '}';
    }
}
