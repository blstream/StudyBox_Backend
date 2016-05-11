package com.bls.patronage.api;

import com.bls.patronage.db.model.Audit;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.sql.Timestamp;
import java.util.UUID;

@JsonDeserialize(builder = DeckRepresentation.DeckRepresentationBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditRepresentation implements DbMappable<Audit>{

    private final UUID id;
    private final Timestamp createdAt;
    private final Timestamp modifiedAt;
    private final UUID createdBy;
    private final UUID modifiedBy;

    public AuditRepresentation(Audit audit) {
        this.id = audit.getId();
        this.createdAt = audit.getCreatedAt();
        this.modifiedAt = audit.getModifiedAt();
        this.createdBy = audit.getCreatedBy();
        this.modifiedBy = audit.getModifiedBy();
    }

    @Override
    public Audit map(){
        return new Audit(id, createdAt, modifiedAt, createdBy, modifiedBy);
    }

    public UUID getId(){
        return id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getModifiedAt() {
        return modifiedAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public UUID getModifiedBy() {
        return modifiedBy;
    }


}
