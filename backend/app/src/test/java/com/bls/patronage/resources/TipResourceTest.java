package com.bls.patronage.resources;

import com.bls.patronage.api.TipRepresentation;
import com.bls.patronage.db.model.AuditableEntity;
import com.bls.patronage.db.model.Tip;
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
public class TipResourceTest extends BasicAuthenticationTest{

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TipResource(tipDAO, storageService))
            .addProvider(MultiPartFeature.class)
            .build();

    @Captor
    private ArgumentCaptor<Tip> tipCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;
    private Tip tip;
    private TipRepresentation tipRepresentation;
    private String tipURI;
    private UUID userId;
    private AuditableEntity auditEntity;

    static private Response getPutResponse(String uri,
                                           TipRepresentation tip,
                                           String encodedUserInfo) {
        return authResources.client()
                .target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .put(Entity.entity(tip, MediaType.APPLICATION_JSON));
    }

    @Before
    public void setUp(){
        super.setUp();
        userId= UUID.fromString("a01db907-60ce-474a-8248-129e2f7f8f36");
        tip = new Tip(UUID.fromString("12345678-9012-3456-7890-123456789012"), "Like sky", 9, UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"), UUID.fromString("68d7fd99-4bd9-45f6-85bb-86331f5c274d"));
        tipRepresentation = new TipRepresentation("Testing", 2);
        auditEntity = new AuditableEntity(UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"),
                new Timestamp(new Long("1461219791000")),
                new Timestamp(new Long("1463234622000")),
                userId,
                userId);
        tipRepresentation.setAuditFields(auditEntity);
        tipURI= UriBuilder.fromResource(TipResource.class).build(tip.getDeckId(), tip.getFlashcardId(), tip.getId()).toString();
    }

    @Test
    public void updateTip() {
        final Response response = getPutResponse(tipURI, tipRepresentation, encodedCredentials);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void deleteTip() {
        when(tipDAO.getTipById(tip.getId())).thenReturn(tip);
        final Response response = resources.client().target(tipURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void getTip() {
        when(tipDAO.getTipById(any(UUID.class))).thenReturn(tip);
        when(tipDAO.getTipAuditFields(any(UUID.class))).thenReturn(auditEntity);

        final TipRepresentation recievedTip = resources.client().target(tipURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(TipRepresentation.class);

        verify(tipDAO).getTipById(tip.getId());
        assertThat(recievedTip).isEqualTo(new TipRepresentation(tip));
    }
}
