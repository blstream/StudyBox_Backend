package com.bls.patronage.resources;

import com.bls.patronage.api.TipRepresentation;
import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Path("/decks/{deckId}/flashcards/{flashcardId}/tips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipsResource {
    private final TipDAO tipDAO;

    public TipsResource(TipDAO tipDAO){
        this.tipDAO=tipDAO;
    }

    @POST
    public Response createTip(@Auth @Valid TipRepresentation tip,
                         @Valid @PathParam("flashcardId") UUIDParam flashcardId,
                         @Valid @PathParam("deckId") UUIDParam deckId,
                              @Context SecurityContext context){

        final User user = (User) context.getUserPrincipal();

        tipDAO.createTip(
                tip
                        .setId(UUID.randomUUID())
                        .setFlashcardId(flashcardId.get())
                        .setDeckId(deckId.get())
                        .map(),
                user.getId()
        );


        return Response.ok(tip).status(Response.Status.CREATED).build();
    }

    @GET
    public List<TipRepresentation> listTips(@Valid @PathParam("flashcardId") UUIDParam id) {

        return tipDAO.getAllTips(id.get())
                .stream()
                .map(tip -> new TipRepresentation(tip).setAuditFields(tipDAO.getTipAuditFields(tip.getId())))
                .collect(Collectors.toList());
    }
}
