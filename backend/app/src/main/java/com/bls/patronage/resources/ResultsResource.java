package com.bls.patronage.resources;

import com.bls.patronage.api.ResultRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.dao.ResultDAO;
import com.bls.patronage.db.model.Result;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public Response createResult(@Auth User user, @Valid List<ResultRepresentation> results) {
        Optional resultFromDb;

        for(ResultRepresentation result : results) {
            if((resultFromDb = resultDAO.getResult(result.getFlashcardId(), user.getId())).isPresent()) {
                result.readFromDbModel((Result) resultFromDb.get())
                        .setCorrectAnswers(result.getCorrectAnswers() + (result.getCorrectAnswer() ? 1 : 0));
                resultDAO.updateResult(result.map(), user.getId());
            } else {
                result.setCorrectAnswers(result.getCorrectAnswer() ? 1 : 0)
                        .setUserId(user.getId());
                resultDAO.createResult(result.map(), user.getId());
            }
        }

        return Response.ok(results).status(Response.Status.CREATED).build();
    }

    @GET
    public List<ResultRepresentation> listResults(@Auth User user, @Valid @PathParam("deckId") UUIDParam deckId) {
        List<String> ids = flashcardDAO.getFlashcardsIdFromSelectedDeck(deckId.get());
        List<UUID> flashcardsUUIDs = ids.stream().map(UUID::fromString).collect(Collectors.toList());
        List<ResultRepresentation> results = new ArrayList<>();
        Optional result;

        for(UUID uuid : flashcardsUUIDs) {
            if((result = resultDAO.getResult(uuid, user.getId())).isPresent()) {
                results.add(new ResultRepresentation().readFromDbModel((Result) result.get()).setAuditFields(resultDAO.getResultAuditFields(uuid)));
            }
        }

        return results;
    }
}
