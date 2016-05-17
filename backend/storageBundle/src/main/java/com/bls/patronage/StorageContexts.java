package com.bls.patronage;

import io.dropwizard.validation.ValidationMethod;

public enum StorageContexts {
    CV("cv"), FLASHCARDS("flashcards"), TIPS("tips");

    private final String context;

    StorageContexts(String context) {
        this.context = context;
    }

    @ValidationMethod
    public boolean isValidContext() {
        return context.equals(CV) || context.equals(FLASHCARDS) || context.equals(TIPS);
    }

    public String getContext() {
        return context;
    }
}
