package com.bls.patronage.resources;

import com.bls.patronage.api.TipRepresentation;
import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.model.Tip;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/decks/{deckId}/flashcards/{flashcardId}/tips/{tipId}")
@Produces(MediaType.APPLICATION_JSON)
public class TipResource {
    private final TipDAO tipDAO;


    public TipResource(TipDAO tipDAO) {
        this.tipDAO = tipDAO;
    }

    @GET
    public Tip getTip(@Valid @PathParam("tipId")UUIDParam tipId){
        return tipDAO.getTipById(tipId.get());
    }

    @DELETE
    public void deleteTip(@Valid @PathParam("tipId") UUIDParam tipId){
        tipDAO.deleteTip(tipDAO.getTipById(tipId.get()).getId());
    }

    @PUT
    public void updateTip(@Valid @PathParam("tipId") UUIDParam tipId,
                          @Valid TipRepresentation tip,
                          @Valid @PathParam("flashcardId") UUIDParam flashcardId,
                          @Valid @PathParam("deckId") UUIDParam deckId){
        Tip updatedTip = new Tip(tipId.get(), tip.getEssence(), tip.getDifficult(), flashcardId.get(), deckId.get());
        tipDAO.updateTip(updatedTip);
    }
}
