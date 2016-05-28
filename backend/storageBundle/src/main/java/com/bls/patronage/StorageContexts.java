package com.bls.patronage;

import javax.ws.rs.WebApplicationException;

public enum StorageContexts {
    CV, FLASHCARDS, TIPS;

    public static StorageContexts fromString(final String param) {
        final String toUpper = param.toUpperCase();
        try {
            return valueOf(toUpper);
        } catch (Exception e) {
            throw new WebApplicationException("Context is not valid", 400);
        }
    }

}
