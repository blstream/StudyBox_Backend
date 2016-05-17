package com.bls.patronage.resources;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.model.AuditableEntity;
import com.bls.patronage.db.model.Flashcard;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
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
import java.sql.Timestamp;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FlashcardResourceTest extends BasicAuthenticationTest{
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new FlashcardResource(flashcardDAO, storageService))
            .addProvider(MultiPartFeature.class)
            .build();
    @Captor
    private ArgumentCaptor<Flashcard> flashcardCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;
    private Flashcard flashcard;
    private FlashcardRepresentation flashcardRepresentation;
    private String flashcardURI;
    private AuditableEntity auditEntity;
    private UUID userId;

    static private Response getPutResponse(String uri,
                                           FlashcardRepresentation flashcard,
                                           String encodedUserInfo) {
        return authResources.client()
                .target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .put(Entity.entity(flashcard, MediaType.APPLICATION_JSON));
    }

    @Before
    public void setUp() {
        super.setUp();
        userId = UUID.fromString("a01db907-60ce-474a-8248-129e2f7f8f36");
        flashcard = new Flashcard(UUID.fromString("12345678-9012-3456-7890-123456789012"), "Are you ok?", "Yes",
                UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"), false);
        flashcardRepresentation = new FlashcardRepresentation("Im testing", "ok", true);
        auditEntity = new AuditableEntity(UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"),
                new Timestamp(new Long("1461219791000")),
                new Timestamp(new Long("1463234622000")),
                userId,
                userId);
        flashcardRepresentation.setAuditFields(auditEntity);

        flashcardURI = UriBuilder.fromResource(FlashcardResource.class).build(flashcard.getDeckId(),
                flashcard.getId()).toString();
    }

    @Test
    public void updateFlashcard() {
        final Response response = getPutResponse(flashcardURI, flashcardRepresentation, encodedCredentials);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(flashcardDAO).updateFlashcard(flashcardCaptor.capture(), uuidCaptor.capture());
        assertThat(flashcardCaptor.getValue().getId()).isEqualTo(flashcard.getId());
        assertThat(flashcardCaptor.getValue().getQuestion()).isEqualTo(flashcardRepresentation.getQuestion());
        assertThat(flashcardCaptor.getValue().getAnswer()).isEqualTo(flashcardRepresentation.getAnswer());
        assertThat(flashcardCaptor.getValue().getDeckId()).isEqualTo(flashcard.getDeckId());
    }

    @Test
    public void deleteFlashcard() {
        when(flashcardDAO.getFlashcardById(flashcard.getId())).thenReturn(flashcard);
        final Response response = resources.client().target(flashcardURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void getFlashcard() {
        when(flashcardDAO.getFlashcardById(any(UUID.class))).thenReturn(flashcard);
        when(flashcardDAO.getFlashcardAuditFields(any(UUID.class))).thenReturn(auditEntity);

        final FlashcardRepresentation receivedFlashcard = resources.client().target(flashcardURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(FlashcardRepresentation.class);

        verify(flashcardDAO).getFlashcardById(flashcard.getId());
        assertThat(receivedFlashcard).isEqualTo(new FlashcardRepresentation(flashcard));
    }
}
