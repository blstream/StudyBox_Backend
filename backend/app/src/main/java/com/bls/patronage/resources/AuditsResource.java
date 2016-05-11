package com.bls.patronage.resources;

import com.bls.patronage.api.AuditRepresentation;
import com.bls.patronage.db.dao.AuditDAO;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Collection;
import java.util.stream.Collectors;

@Path("/audits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuditsResource {

    private final AuditDAO auditDAO;


    public AuditsResource(AuditDAO auditDAO) {
        this.auditDAO = auditDAO;
    }

    @Path("/{table}")
    @GET
    public Collection<AuditRepresentation> listAudits(){
        return auditDAO.getAllAudits("decks")
                .stream()
                .map(audit -> new AuditRepresentation(audit))
                .collect(Collectors.toList());
    }

    @Path("/{table}/{auditId}")
    @GET
    public AuditRepresentation getAudit(@Valid @PathParam("auditId") UUIDParam auditId){
        return new AuditRepresentation(auditDAO.getAudit(auditId.get(), "decks"));
    }


}
