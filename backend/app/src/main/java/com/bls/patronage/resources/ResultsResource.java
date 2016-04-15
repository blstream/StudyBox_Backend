package com.bls.patronage.resources;

import com.bls.patronage.api.ResultRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.dao.ResultDAO;
import com.bls.patronage.db.exception.DataAccessException;
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

@Path("/decks/{deckId}/results")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ResultsResource {
    private final FlashcardDAO flashcardDAO;
    private final ResultDAO resultDAO;

    public ResultsResource(FlashcardDAO flashcardDAO, ResultDAO resultDAO) {
        this.flashcardDAO = flashcardDAO;
        this.resultDAO = resultDAO;
    }

    @POST
    public Response createResult(@Valid ResultRepresentation result) {
        try {
            result
                    .readFromDbModel(resultDAO.getResult(result.getId()))
                    .setCorrectAnswers(result.getCorrectAnswers() + 1);
        } catch (DataAccessException e) {
            result
                    .setId(UUID.randomUUID())
                    .setCorrectAnswers(result.isCorrectAnswer() ? 1 : 0);
        }

        resultDAO.updateResult(result.buildDbModel());


        return Response.ok(result).status(Response.Status.CREATED).build();
    }

    @GET
    public List<ResultRepresentation> listResults(@Valid @PathParam("deckId") UUIDParam deckId) {
        List<UUID> ids = flashcardDAO.getFlashcardsIdFromSelectedDeck(deckId.get());
        return ids
                .stream()
                .map(uuid -> new ResultRepresentation()
                        .readFromDbModel(
                                resultDAO.getResult(uuid)
                        ))
                .collect(Collectors.toList());
    }
}
