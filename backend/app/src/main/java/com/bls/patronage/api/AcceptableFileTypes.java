package com.bls.patronage.api;

public class AcceptableFileTypes {
    public static final String IMAGE = "image";
    public static final String TEXT = "text";

    private final boolean validType;
    private final String givenType;

    public AcceptableFileTypes(String givenString) {
        givenType = givenString;
        validType = givenString.equals(IMAGE) || givenString.equals(TEXT);
    }

    public boolean isValidType() {
        return validType;
    }

    public String getGivenType() {
        return givenType;
    }
}
