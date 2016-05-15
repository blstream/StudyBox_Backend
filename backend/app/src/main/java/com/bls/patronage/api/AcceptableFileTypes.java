package com.bls.patronage.api;

import io.dropwizard.validation.ValidationMethod;

public enum AcceptableFileTypes {
    IMAGE("image"), TEXT("text");

    private final String fileType;

    AcceptableFileTypes(String fileType) {
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
