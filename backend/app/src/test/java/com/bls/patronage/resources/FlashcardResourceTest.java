package com.bls.patronage.resources;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Flashcard;
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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FlashcardResourceTest {
    private static final FlashcardDAO dao = mock(FlashcardDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new FlashcardResource(dao))
            .build();
    @Captor
    private ArgumentCaptor<Flashcard> flashcardCaptor;
    private Flashcard flashcard;
    private FlashcardRepresentation flashcardRepresentation;
    private String flashcardURI;

    @Before
    public void setUp() {
        flashcard = new Flashcard("12345678-9012-3456-7890-123456789012", "Are you ok?", "Yes", "8ad4b503-5bfc-4d8a-a761-0908374892b1");
        flashcardRepresentation = new FlashcardRepresentation("Im testing", "ok");
        flashcardURI = UriBuilder.fromResource(FlashcardResource.class).build(flashcard.getDeckID(), flashcard.getId()).toString();
    }

    @After
    public void tearDown() {
        reset(dao);
    }

    @Test
    public void updateFlashcard() {
        final Response response = resources.client().target(flashcardURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(flashcardRepresentation, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void deleteFlashcard() {
        when(dao.getFlashcardById(flashcard.getId())).thenReturn(flashcard);
        final Response response = resources.client().target(flashcardURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void getFlashcard() {
        when(dao.getFlashcardById(any(UUID.class))).thenReturn(flashcard);
        final Flashcard recievedFlashcard = resources.client().target(flashcardURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(Flashcard.class);

        verify(dao).getFlashcardById(flashcard.getId());
        assertThat(recievedFlashcard).isEqualTo(flashcard);
    }
}