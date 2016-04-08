package com.bls.patronage.resources;

import com.bls.patronage.api.ResultRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.dao.ResultDAO;
import com.bls.patronage.db.model.Result;
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
import java.util.Set;
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
    public Response createResult(@Valid ResultRepresentation result,
                             @Valid @PathParam("deckId") UUIDParam id) {
        Set<UUID> uuids = resultDAO.getAllResults(id.get()).stream().map(Result::getId).collect(Collectors.toSet());
        Result createdResult;
        if (!uuids.contains(result.getFlashcardId())) {
            createdResult = new Result(result.getFlashcardId(), 0);
            resultDAO.createResult(createdResult);
        } else {
            int correctAnswers = resultDAO.getResult(result.getFlashcardId()).getCorrectAnswers() + (result.isCorrectAnswer() ? 1 : 0);
            createdResult = new Result(result.getFlashcardId(), correctAnswers);
            resultDAO.createResult(createdResult);
        }
        return Response.ok(createdResult).status(Response.Status.CREATED).build();
    }

    @GET
    public List<Result> listResults(@Valid @PathParam("deckId") UUIDParam deckId) {
        List<UUID> ids = flashcardDAO.getFlashcardsIdFromSelectedDeck(deckId.get());
        return ids.stream().map(resultDAO::getResult).collect(Collectors.toList());
    }
}
