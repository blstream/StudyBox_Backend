package com.bls.patronage.resources;

import com.bls.patronage.api.ResultRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.dao.ResultDAO;
import com.bls.patronage.db.model.Result;
import com.google.common.collect.ImmutableList;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
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
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResultsResourceTest {
    private static final ResultDAO resultDAO = mock(ResultDAO.class);
    private static final FlashcardDAO flashcardDAO = mock(FlashcardDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new ResultsResource(flashcardDAO, resultDAO))
            .build();
    @Captor
    private ArgumentCaptor<Result> resultCaptor;
    private Result result;
    private ResultRepresentation resultRepresentation;
    private String resultsURI;
    private UUID deckId;

    @Before
    public void setUp() {
        result = new Result(UUID.randomUUID(), new Random().nextInt());
        resultRepresentation = new ResultRepresentation(result.getId(), true);
        deckId = UUID.randomUUID();
        resultsURI = UriBuilder.fromResource(ResultsResource.class).build(deckId).toString();
    }

    @After
    public void tearDown() {
        reset(resultDAO);
    }

    @Test
    public void createExistingResult() {
        when(resultDAO.getResult(resultRepresentation.getFlashcardId())).thenReturn(result);
        when(resultDAO.getAllResults(deckId)).thenReturn(ImmutableList.of(result));

        final Response response = resources.client().target(resultsURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(resultRepresentation, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
        verify(resultDAO).createResult(resultCaptor.capture());
        assertThat(resultCaptor.getValue().getId()).isEqualTo(resultRepresentation.getFlashcardId());
        assertThat(resultCaptor.getValue().getCorrectAnswers()).isEqualTo(result.getCorrectAnswers() + 1);
    }

    @Test
    public void createNewResult() {
        when(resultDAO.getResult(resultRepresentation.getFlashcardId())).thenReturn(result);
        when(resultDAO.getAllResults(deckId)).thenReturn(anyListOf(Result.class));

        final Response response = resources.client().target(resultsURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(resultRepresentation, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
        verify(resultDAO).createResult(resultCaptor.capture());
        assertThat(resultCaptor.getValue().getId()).isEqualTo(resultRepresentation.getFlashcardId());
        assertThat(resultCaptor.getValue().getCorrectAnswers()).isEqualTo(0);
    }

    @Test
    public void createIncorrectResult() {
        ResultRepresentation requestEntity = new ResultRepresentation(null, false);

        final Response response = resources.client().target(resultsURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void listResults() {
        when(flashcardDAO.getFlashcardsIdFromSelectedDeck(deckId)).thenReturn(ImmutableList.of(resultRepresentation.getFlashcardId()));
        when(resultDAO.getResult(resultRepresentation.getFlashcardId())).thenReturn(result);

        final List<Result> response = resources.client().target(resultsURI)
                .request().get(new GenericType<List<Result>>() {
                });

        verify(resultDAO).getResult(resultRepresentation.getFlashcardId());
        verify(flashcardDAO).getFlashcardsIdFromSelectedDeck(deckId);
        assertThat(response).contains(result);
    }
}