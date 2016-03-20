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

    public ResultsResource(ResultDAO resultDAO, FlashcardDAO flashcardDAO) {
        this.resultDAO = resultDAO;
        this.flashcardDAO = flashcardDAO;
    }

    @POST
    public void createResult(@Valid ResultRepresentation result,
                             @Valid @PathParam("deckId") UUIDParam id) {
        Set<UUID> uuids = resultDAO.getAllResults(id.get()).stream().map(Result::getId).collect(Collectors.toSet());

        if (!uuids.contains(result.getFlashcardId())) {
            resultDAO.createResult(new Result(result.getFlashcardId(), 0));
        } else {
            int correctAnswers = resultDAO.getResult(result.getFlashcardId()).getCorrectAnswers() + (result.isCorrectAnswer() ? 1 : 0);
            Result createdResult = new Result(result.getFlashcardId(), correctAnswers);
            resultDAO.createResult(createdResult);
        }
    }

    @GET
    public List<Result> listResults(@Valid @PathParam("deckId") UUIDParam deckId) {
        List<UUID> ids = flashcardDAO.getFlashcardsIdFromSelectedDeck(deckId.get());
        List<Result> results = ids.stream().map(resultDAO::getResult).collect(Collectors.toList());
        return results;
    }
}
