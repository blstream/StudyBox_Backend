package com.bls.patronage.resources;

import com.bls.patronage.StorageService;
import com.bls.patronage.api.TipRepresentation;
import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.model.Tip;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TipResourceTest {
    private static final TipDAO dao = mock(TipDAO.class);
    private static final StorageService storageService = mock(StorageService.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TipResource(dao, storageService))
            .build();

    @Captor
    private ArgumentCaptor<Tip> tipCaptor;
    private Tip tip;
    private TipRepresentation tipRepresentation;
    private String tipURI;

    @Before
    public void setUp(){
        tip = new Tip(UUID.fromString("12345678-9012-3456-7890-123456789012"), "Like sky", 9, UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"), UUID.fromString("68d7fd99-4bd9-45f6-85bb-86331f5c274d"));
        tipRepresentation = new TipRepresentation("Testing", 2);
        tipURI= UriBuilder.fromResource(TipResource.class).build(tip.getDeckId(), tip.getFlashcardId(), tip.getId()).toString();
    }

    @After
    public void tearDown(){
        reset(dao);
    }

    @Test
    public void updateTip() {
        final Response response = resources.client().target(tipURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.entity(tipRepresentation, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
    }

    @Test
    public void deleteTip() {
        when(dao.getTipById(tip.getId())).thenReturn(tip);
        final Response response = resources.client().target(tipURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.NO_CONTENT);
    }

    @Test
    public void getTip() {
        when(dao.getTipById(any(UUID.class))).thenReturn(tip);
        final TipRepresentation recievedTip = resources.client().target(tipURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get(TipRepresentation.class);

        verify(dao).getTipById(tip.getId());
        assertThat(recievedTip).isEqualTo(new TipRepresentation(tip));
    }

}
