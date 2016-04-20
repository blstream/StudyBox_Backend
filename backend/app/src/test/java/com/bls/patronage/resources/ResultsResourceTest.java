package com.bls.patronage.resources;

import com.bls.patronage.api.ResultRepresentation;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.Result;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ResultsResourceTest extends BasicAuthenticationTest {

    @Captor
    private ArgumentCaptor<Result> resultCaptor;
    private Result result;
    private ResultRepresentation resultRepresentation;
    private String resultsURI;
    private UUID deckId;
    private Flashcard flashcard;

    @Before
    public void setUp() {
        super.setUp();
        deckId = UUID.randomUUID();
        flashcard = new Flashcard(UUID.randomUUID(),"foo","bar", deckId, false);
        result = new Result(flashcard.getId(), new Random().nextInt(), user.getId());
        resultRepresentation = new ResultRepresentation(result.getId(), true);
        resultsURI = UriBuilder.fromResource(ResultsResource.class).build(deckId).toString();
    }

    static private Response postResults(String uri, List<ResultRepresentation> results, String encodedUserInfo) {
        return authResources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .post(Entity.entity(results, MediaType.APPLICATION_JSON_TYPE));
    }

    @Test
    public void createExistingResult() {
        when(resultDAO.getResult(flashcard.getId(), user.getId())).thenReturn(Optional.ofNullable(result));

        final Response response = postResults(resultsURI, Collections.singletonList(resultRepresentation),
                encodedCredentials);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        verify(resultDAO).updateResult(resultCaptor.capture());
        verify(resultDAO, never()).createResult(resultCaptor.capture());
        assertThat(resultCaptor.getValue().getId()).isEqualTo(resultRepresentation.getFlashcardId());
        assertThat(resultCaptor.getValue().getCorrectAnswers()).isEqualTo(result.getCorrectAnswers() + 1);
    }

    @Test
    public void createNewResult() {
        when(resultDAO.getResult(flashcard.getId(), user.getId())).thenReturn(Optional.ofNullable(null));

        final Response response = postResults(resultsURI, Collections.singletonList(resultRepresentation),
                encodedCredentials);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        verify(resultDAO, never()).updateResult(resultCaptor.capture());
        verify(resultDAO).createResult(resultCaptor.capture());
        assertThat(resultCaptor.getValue().getId()).isEqualTo(resultRepresentation.getFlashcardId());
        assertThat(resultCaptor.getValue().getCorrectAnswers()).isEqualTo(1);
    }

    @Test
    public void createIncorrectResult() {
        ResultRepresentation requestEntity = new ResultRepresentation(null, false);

        final Response response = postResults(resultsURI, Collections.singletonList(requestEntity),
                encodedCredentials);

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void listResults() {
        when(flashcardDAO.getFlashcardsIdFromSelectedDeck(deckId))
                .thenReturn(ImmutableList.of(flashcard.getId().toString()));
        when(resultDAO.getResult(resultRepresentation.getFlashcardId(),
                user.getId())).thenReturn(Optional.ofNullable(result));

        final Response response = getResponseWithCredentials(resultsURI, encodedCredentials);
        final List<ResultRepresentation> results = response
                .readEntity((new GenericType<List<ResultRepresentation>>() {}));

        verify(resultDAO).getResult(resultRepresentation.getFlashcardId(), user.getId());
        verify(flashcardDAO).getFlashcardsIdFromSelectedDeck(deckId);
        assertThat(results.get(0)).isEqualToComparingFieldByField(new ResultRepresentation().readFromDbModel(result));
    }
}
