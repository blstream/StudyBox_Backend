package com.bls.patronage.cv;

import com.bls.patronage.api.AcceptableFileType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.net.URL;

public class CVRequest {

    public final URL url;
    public final String action;

    public CVRequest(URL url, String action) {
        this.url = url;
        this.action = action;
    }

    public static CVRequest createRecognizeRequest(final URL publicURLToUploadedFile, String fileType) {
        System.out.println(fileType);
        return new CVRequest(
                publicURLToUploadedFile,
                fileType.equals(AcceptableFileType.IMAGE.toString()) ? "ImageToFlashcard" : "TextToFlashcard"
        );
    }

    @JsonProperty
    public URL getUrl() {
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
