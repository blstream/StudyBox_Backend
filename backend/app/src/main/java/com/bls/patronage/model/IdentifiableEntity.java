package com.bls.patronage.model;

import java.util.UUID;

class IdentifiableEntity {
    private final UUID id;

    IdentifiableEntity(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
}
