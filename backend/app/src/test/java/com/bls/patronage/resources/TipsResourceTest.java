package com.bls.patronage.resources;

import com.bls.patronage.api.TipRepresentation;
import com.bls.patronage.db.model.AuditableEntity;
import com.bls.patronage.db.model.Tip;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TipsResourceTest extends BasicAuthenticationTest{

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TipsResource(tipDAO))
            .build();
    @Captor
    private ArgumentCaptor<Tip> tipCaptor;
    @Captor
    private ArgumentCaptor<UUID> uuidCaptor;
    private Tip tip;
    private TipRepresentation tipRepresentation;
    private String tipsURI;
    private AuditableEntity auditEntity;
    private UUID userId;

    static private Response postTip(String uri, String essence, int difficult, String encodedUserInfo) {
        return authResources.client().target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedUserInfo)
                .post(Entity.entity(new TipRepresentation(essence, difficult),
                        MediaType.APPLICATION_JSON_TYPE));
    }

    @Before
    public void setUp() {
        super.setUp();
        userId = UUID.fromString("a01db907-60ce-474a-8248-129e2f7f8f36");
        tip = new Tip(UUID.fromString("12345678-9012-3456-7890-123456789012"), "Like a blue", 4, UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"), UUID.fromString("68d7fd99-4bd9-45f6-85bb-86331f5c274d"));
        tipRepresentation = new TipRepresentation("Im testing", 2);
        auditEntity = new AuditableEntity(UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"),
                new Timestamp(new Long("1461219791000")),
                new Timestamp(new Long("1463234622000")),
                userId,
                userId);
        tipRepresentation.setAuditFields(auditEntity);
        tipsURI = UriBuilder.fromResource(TipsResource.class).build(tip.getDeckId(), tip.getFlashcardId()).toString();
        when(tipDAO.getTipAuditFields(tip.getId())).thenReturn(auditEntity);
    }

    @Test
    public void createTip() {
        final Response response = postTip(tipsURI, tipRepresentation.getEssence(), tipRepresentation.getDifficult(), encodedCredentials);

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.CREATED);
        verify(tipDAO).createTip(tipCaptor.capture(), uuidCaptor.capture());
        assertThat(tipCaptor.getValue().getId()).isNotNull();
        assertThat(tipCaptor.getValue().getEssence()).isEqualTo(tipRepresentation.getEssence());
        assertThat(tipCaptor.getValue().getDifficult()).isEqualTo(tipRepresentation.getDifficult());
    }

    @Test
    public void listTips() {
        final ImmutableList<Tip> tips = ImmutableList.of(tip);
        when(tipDAO.getAllTips(tip.getFlashcardId())).thenReturn(tips);

        final List<TipRepresentation> response = resources.client().target(tipsURI)
                .request().get(new GenericType<List<TipRepresentation>>() {
                });

        verify(tipDAO).getAllTips(tip.getFlashcardId());
        assertThat(response).containsOnly(new TipRepresentation(tip).setAuditFields(auditEntity));
    }
}
