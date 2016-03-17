package com.bls.patronage.resources;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Flashcard;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FlashcardsResourceTest {
    private static final FlashcardDAO dao = mock(FlashcardDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new FlashcardsResource(dao))
            .build();
    @Captor
    private ArgumentCaptor<Flashcard> flashcardCaptor;
    private Flashcard flashcard;
    private FlashcardRepresentation flashcardRepresentation;
    private String flashcardsURI;

    @Before
    public void setUp() {
        flashcard = new Flashcard("12345678-9012-3456-7890-123456789012", "Are you ok?", "Yes", "8ad4b503-5bfc-4d8a-a761-0908374892b1");
        flashcardRepresentation = new FlashcardRepresentation("Im testing", "ok");
        flashcardsURI = UriBuilder.fromResource(FlashcardsResource.class).build(flashcard.getDeckID()).toString();
    }

    @After
    public void tearDown() {
        reset(dao);
    }

    @Test
    public void createFlashcard() {
        final Response response = resources.client().target(flashcardsURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(flashcardRepresentation, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(dao).createFlashcard(flashcardCaptor.capture());
        assertThat(flashcardCaptor.getValue().getId()).isNotNull();
        assertThat(flashcardCaptor.getValue().getQuestion()).isEqualTo(flashcardRepresentation.getQuestion());
        assertThat(flashcardCaptor.getValue().getAnswer()).isEqualTo(flashcardRepresentation.getAnswer());
    }

    @Test
    public void createFlashcardWithoutQuestionAndAnswer() {
        final Response response = resources.client().target(flashcardsURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(new FlashcardRepresentation(), MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void listFlashcards() {
        final ImmutableList<Flashcard> flashcards = ImmutableList.of(flashcard);
        when(dao.getAllFlashcards(flashcard.getDeckID())).thenReturn(flashcards);

        final List<Flashcard> response = resources.client().target(flashcardsURI)
                .request().get(new GenericType<List<Flashcard>>() {
                });

        verify(dao).getAllFlashcards(flashcard.getDeckID());
        assertThat(response).containsAll(flashcards);
    }
}