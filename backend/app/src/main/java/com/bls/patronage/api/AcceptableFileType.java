package com.bls.patronage.api;

import javax.ws.rs.WebApplicationException;

public enum AcceptableFileType {
    IMAGE, TEXT;

    public static AcceptableFileType fromString(String param) {
        String toUpper = param.toUpperCase();
        try {
            return valueOf(toUpper);
        } catch (Exception e) {
            throw new WebApplicationException("File type is not valid ", 400);
        }
    }
}
