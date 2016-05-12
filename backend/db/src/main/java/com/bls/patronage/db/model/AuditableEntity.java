package com.bls.patronage.db.model;

import java.sql.Timestamp;
import java.util.UUID;

public class AuditableEntity extends IdentifiableEntity{
    private final Timestamp createdAt;
    private final Timestamp modifiedAt;
    private final UUID createdBy;
    private final UUID modifiedBy;

    public AuditableEntity(){
        super();
        this.createdAt=null;
        this.modifiedAt = null;
        this.createdBy = null;
        this.modifiedBy = null;
    }

    public AuditableEntity(UUID id){
        super(id);
        this.createdAt=null;
        this.modifiedAt = null;
        this.createdBy = null;
        this.modifiedBy = null;
    }

    public AuditableEntity(String id){
        super(id);
        this.createdAt=null;
        this.modifiedAt = null;
        this.createdBy = null;
        this.modifiedBy = null;
    }

    public AuditableEntity(Timestamp createdAt, Timestamp modifiedAt, UUID createdBy, UUID modifiedBy) {
        super();
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }

    public AuditableEntity(UUID id, Timestamp createdAt, Timestamp modifiedAt, UUID createdBy, UUID modifiedBy) {
        super(id);
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        }

    public AuditableEntity(UUID id, Timestamp createdAt, Timestamp modifiedAt, UUID createdBy, String modifiedBy) {
        super(id);
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = createdBy;
        this.modifiedBy = UUID.fromString(modifiedBy);
        }

    public AuditableEntity(UUID id, Timestamp createdAt, Timestamp modifiedAt, String createdBy, UUID modifiedBy) {
        super(id);
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = UUID.fromString(createdBy);
        this.modifiedBy = modifiedBy;
        }

    public AuditableEntity(UUID id, Timestamp createdAt, Timestamp modifiedAt, String createdBy, String modifiedBy) {
        super(id);
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = UUID.fromString(createdBy);
        this.modifiedBy = UUID.fromString(modifiedBy);
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
