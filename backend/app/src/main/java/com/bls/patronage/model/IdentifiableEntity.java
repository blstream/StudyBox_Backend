package com.bls.patronage.model;

import java.util.UUID;

public class IdentifiableEntity {
    private final UUID id;

    public IdentifiableEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
