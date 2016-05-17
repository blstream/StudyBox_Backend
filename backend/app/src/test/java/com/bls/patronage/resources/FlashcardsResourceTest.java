package com.bls.patronage.resources;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.model.Amount;
import com.bls.patronage.db.model.AuditableEntity;
import com.bls.patronage.db.model.Flashcard;
import com.google.common.collect.ImmutableList;
import io.dropwizard.testing.junit.ResourceTestRule;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlashcardsResourceTest extends BasicAuthenticationTest{

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new FlashcardsResource(flashcardDAO))
            .build();
    @Captor
    private ArgumentCaptor<Flashcard> flashcardCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;
    private Flashcard flashcard;
    private FlashcardRepresentation flashcardRepresentation;
    private String flashcardsURI;
    private String flashcardsWithTipsURI;
    private List<String> randomFlashcardsURIs;
    private AuditableEntity auditEntity;
    private UUID userId;

    static private Response postFlashcard(String uri, String question, String answer, Boolean isHidden, String encodedUserInfo) {
        return authResources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .post(Entity.entity(new FlashcardRepresentation(question, answer, isHidden),
                        MediaType.APPLICATION_JSON_TYPE));
    }

    @Before
    public void setUp() {
        super.setUp();
        userId = UUID.fromString("a01db907-60ce-474a-8248-129e2f7f8f36");
        flashcard = new Flashcard(UUID.fromString("12345678-9012-3456-7890-123456789012"), "Are you ok?", "Yes",
                UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"), false);
        flashcardRepresentation = new FlashcardRepresentation("Im testing", "ok", false);
        auditEntity = new AuditableEntity(flashcard.getId(),
                new Timestamp(new Long("1461219791000")),
                new Timestamp(new Long("1463234622000")),
                userId,
                userId);
        flashcardRepresentation.setAuditFields(auditEntity);
        flashcardsURI = UriBuilder.fromResource(FlashcardsResource.class).build(flashcard.getDeckId()).toString();
        flashcardsWithTipsURI = UriBuilder.fromResource(FlashcardsResource.class)
                .queryParam("tipsCount", true).build(flashcard.getDeckId()).toString();
        randomFlashcardsURIs = new ArrayList<>();
        for (Amount amount : Amount.values()) {
            randomFlashcardsURIs.add(UriBuilder.fromResource(FlashcardsResource.class)
                    .queryParam("random", amount.toString().toLowerCase()).build(flashcard.getDeckId()).toString());
        }
        when(flashcardDAO.getFlashcardAuditFields(flashcard.getId())).thenReturn(auditEntity);
    }

    @Test
    public void createFlashcard() {
        Response response = postFlashcard(flashcardsURI, flashcardRepresentation.getQuestion(), flashcardRepresentation.getAnswer(), flashcardRepresentation.getIsHidden(), encodedCredentials);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        verify(flashcardDAO).createFlashcard(flashcardCaptor.capture(), uuidCaptor.capture());
        assertThat(flashcardCaptor.getValue().getId()).isNotNull();
        assertThat(flashcardCaptor.getValue().getQuestion()).isEqualTo(flashcardRepresentation.getQuestion());
        assertThat(flashcardCaptor.getValue().getAnswer()).isEqualTo(flashcardRepresentation.getAnswer());
        assertThat(flashcardCaptor.getValue().getIsHidden()).isEqualTo(flashcardRepresentation.getIsHidden());
        assertThat(uuidCaptor.getValue()).isEqualTo(user.getId());

    }

    @Test
    public void createFlashcardWithoutQuestionAndAnswer() {
        Response response = postFlashcard(flashcardsURI, "", "", false, encodedCredentials);

        assertThat(response.getStatus()).isEqualTo(422);
    }

    @Test
    public void listFlashcards() {
        final ImmutableList<Flashcard> flashcards = ImmutableList.of(flashcard);
        when(flashcardDAO.getAllFlashcards(flashcard.getDeckId())).thenReturn(flashcards);

        final List<FlashcardRepresentation> response = resources.client().target(flashcardsURI)
                .request().get(new GenericType<List<FlashcardRepresentation>>() {
                });

        verify(flashcardDAO).getAllFlashcards(flashcard.getDeckId());
        assertThat(response).contains(new FlashcardRepresentation(flashcard));
    }

    @Test
    public void listFlashcardsWithTipsNumber() {
        final ImmutableList<Flashcard> flashcards = ImmutableList.of(flashcard);
        when(flashcardDAO.getAllFlashcards(flashcard.getDeckId())).thenReturn(flashcards);
        when(flashcardDAO.getTipsCount(flashcard.getId())).thenReturn(5);

        final List<FlashcardRepresentation> response = resources.client().target(flashcardsWithTipsURI)
                .request().get(new GenericType<List<FlashcardRepresentation>>() {
                });

        verify(flashcardDAO).getAllFlashcards(flashcard.getDeckId());
        verify(flashcardDAO).getTipsCount(flashcard.getId());

        assertThat(response).contains(new FlashcardRepresentation(flashcard).setTipsCount(5).setAuditFields(auditEntity));
    }

    @Test
    public void getRandomFlashcards() {
        for (Amount amount : Amount.values()) {
            final List<Flashcard> flashcards = Collections.nCopies(amount.getValue(), flashcard);
            when(flashcardDAO.getRandomFlashcards(amount.getValue(), flashcard.getDeckId())).thenReturn(flashcards);
            final List<FlashcardRepresentation> response = resources.client()
                    .target(randomFlashcardsURIs.get(amount.ordinal()))
                    .request().get(new GenericType<List<FlashcardRepresentation>>() {
                    });
            verify(flashcardDAO).getRandomFlashcards(amount.getValue(), flashcard.getDeckId());
            assertThat(response).hasSize(amount.getValue());
        }
    }
}

