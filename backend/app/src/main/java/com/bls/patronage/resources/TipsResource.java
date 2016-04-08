package com.bls.patronage.resources;

import com.bls.patronage.api.TipRepresentation;
import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.model.Tip;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.UUID;

@Path("/decks/{deckId}/flashcards/{flashcardId}/tips")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TipsResource {
    private final TipDAO tipDAO;

    public TipsResource(TipDAO tipDAO){
        this.tipDAO=tipDAO;
    }

    @POST
    public Response createTip(@Valid TipRepresentation tip,
                         @Valid @PathParam("flashcardId") UUIDParam flashcardId,
                         @Valid @PathParam("deckId") UUIDParam deckId){
        Tip createdTip = new Tip(UUID.randomUUID(), tip.getEssence(), tip.getDifficult(), flashcardId.get(), deckId.get());
        tipDAO.createTip(createdTip);
        return Response.ok(createdTip).status(Response.Status.CREATED).build();
    }

    @GET
    public List<Tip> listTips(@Valid @PathParam("flashcardId") UUIDParam id){
        return tipDAO.getAllTips(id.get());
    }
}
