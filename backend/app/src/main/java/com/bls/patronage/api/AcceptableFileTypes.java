package com.bls.patronage.api;

import io.dropwizard.validation.ValidationMethod;

public class AcceptableFileTypes {
    public static final String IMAGE = "image";
    public static final String TEXT = "text";

    private final String fileType;

    public AcceptableFileTypes(String fileType) {
        this.fileType = fileType;
    }

    @ValidationMethod
    public boolean isValidType() {
        return fileType.equals(IMAGE) || fileType.equals(TEXT);
    }

    public String getFileType() {
        return fileType;
    }
}
