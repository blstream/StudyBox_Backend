package com.bls.patronage.resources;

import com.bls.patronage.api.TipRepresentation;
import com.bls.patronage.db.dao.TipDAO;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/decks/{deckId}/flashcards/{flashcardId}/tips/{tipId}")
@Produces(MediaType.APPLICATION_JSON)
public class TipResource {
    private final TipDAO tipDAO;


    public TipResource(TipDAO tipDAO) {
        this.tipDAO = tipDAO;
    }

    @GET
    public TipRepresentation getTip(@Valid @PathParam("tipId") UUIDParam tipId) {
        return new TipRepresentation(tipDAO.getTipById(tipId.get()));
    }

    @DELETE
    public void deleteTip(@Valid @PathParam("tipId") UUIDParam tipId){
        tipDAO.getTipById(tipId.get());
        tipDAO.deleteTip(tipId.get());
    }

    @PUT
    public TipRepresentation updateTip(@Valid @PathParam("tipId") UUIDParam tipId,
                                       @Valid TipRepresentation tip,
                                       @Valid @PathParam("flashcardId") UUIDParam flashcardId,
                                       @Valid @PathParam("deckId") UUIDParam deckId){
        tipDAO.getTipById(tipId.get());
        tipDAO.updateTip(
                tip.setId(tipId.get())
                        .setFlashcardId(flashcardId.get())
                        .setDeckId(deckId.get())
                        .buildDbModel()
        );

        return tip;
    }
}
