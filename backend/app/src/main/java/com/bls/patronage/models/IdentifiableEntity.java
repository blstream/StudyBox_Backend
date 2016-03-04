package com.bls.patronage.models;

import java.util.UUID;

abstract public class IdentifiableEntity {
    private final UUID id;

    public IdentifiableEntity() {
        this.id = null;
    }

    public IdentifiableEntity(String id) {
        this.id = UUID.fromString(id);
    }

    public IdentifiableEntity(UUID id) {
        this.id = id;
    }


    public UUID getId() {
        return id;
    }
}
