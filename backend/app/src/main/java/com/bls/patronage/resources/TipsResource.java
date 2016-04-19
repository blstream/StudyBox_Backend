package com.bls.patronage.resources;

import com.bls.patronage.api.TipRepresentation;
import com.bls.patronage.db.dao.TipDAO;
import io.dropwizard.jersey.params.UUIDParam;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
    public Response createTip(@Valid TipRepresentation tip,
                         @Valid @PathParam("flashcardId") UUIDParam flashcardId,
                         @Valid @PathParam("deckId") UUIDParam deckId){

        tipDAO.createTip(
                tip
                        .setId(UUID.randomUUID())
                        .setFlashcardId(flashcardId.get())
                        .setDeckId(deckId.get())
                        .map()
        );


        return Response.ok(tip).status(Response.Status.CREATED).build();
    }

    @GET
    public List<TipRepresentation> listTips(@Valid @PathParam("flashcardId") UUIDParam id) {

        return tipDAO.getAllTips(id.get())
                .stream()
                .map(tip -> new TipRepresentation(tip))
                .collect(Collectors.toList());
    }
}
