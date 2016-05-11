package com.bls.patronage.db.model;

import java.sql.Timestamp;
import java.util.UUID;

public class Audit extends IdentifiableEntity {

    private final Timestamp createdAt;
    private final Timestamp modifiedAt;
    private final UUID createdBy;
    private final UUID modifiedBy;

    public Audit(UUID id, Timestamp createdAt, Timestamp modifiedAt, UUID createdBy, UUID modifiedBy) {
        super(id);
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
    }
    public Audit(UUID id, Timestamp createdAt, Timestamp modifiedAt, UUID createdBy, String modifiedBy) {
        super(id);
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = createdBy;
        this.modifiedBy = UUID.fromString(modifiedBy);
    }
    public Audit(UUID id, Timestamp createdAt, Timestamp modifiedAt, String createdBy, UUID modifiedBy) {
        super(id);
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.createdBy = UUID.fromString(createdBy);
        this.modifiedBy = modifiedBy;
    }
    public Audit(UUID id, Timestamp createdAt, Timestamp modifiedAt, String createdBy, String modifiedBy) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Audit audit = (Audit) o;

        if (!getCreatedAt().equals(audit.getCreatedAt()))
            return false;
        if (!getModifiedAt().equals(audit.getModifiedAt()))
            return false;
        if (!getCreatedBy().equals(audit.getCreatedBy()))
            return false;
        return (getModifiedBy().equals(audit.getModifiedBy()));
    }

    @Override
    public int hashCode() {
        int result = getCreatedAt().hashCode();
        result = 31 * result + getModifiedAt().hashCode();
        result = 31 * result + getCreatedBy().hashCode();
        result = 31 * result + getModifiedBy().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Audit{" +
                "createdAt='" + createdAt + '\'' +
                ", modifiedAt='" + modifiedAt + '\'' +
                ", createdBy=" + createdBy +
                ", modifiedBy=" + modifiedBy +
                '}';
    }
}

