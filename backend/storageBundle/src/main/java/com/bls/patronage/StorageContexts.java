package com.bls.patronage;

import javax.ws.rs.WebApplicationException;

public enum StorageContexts {
    CV, FLASHCARDS, TIPS;

    private static StorageContexts fromString(String param) {
        String toUpper = param.toUpperCase();
        try {
            return valueOf(toUpper);
        } catch (Exception e) {
            throw new WebApplicationException("Context is not valid", 400);
        }
    }

}
