package com.bls.patronage.resources;

import com.bls.patronage.api.TipRepresentation;
import com.bls.patronage.db.dao.TipDAO;
import com.bls.patronage.db.model.Tip;
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
public class TipsResourceTest {
    private static final TipDAO dao = mock(TipDAO.class);

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new TipsResource(dao))
            .build();
    @Captor
    private ArgumentCaptor<Tip> tipCaptor;
    private Tip tip;
    private TipRepresentation tipRepresentation;
    private String tipsURI;

    @Before
    public void setUp() {
        tip = new Tip("12345678-9012-3456-7890-123456789012", "Like a blue", 4, "8ad4b503-5bfc-4d8a-a761-0908374892b1", "68d7fd99-4bd9-45f6-85bb-86331f5c274d");
        tipRepresentation = new TipRepresentation("Im testing", 2);
        tipsURI = UriBuilder.fromResource(TipsResource.class).build(tip.getDeckId(), tip.getFlashcardId()).toString();
    }

    @After
    public void tearDown() {
        reset(dao);
    }

    @Test
    public void createTip() {
        final Response response = resources.client().target(tipsURI)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(tipRepresentation, MediaType.APPLICATION_JSON_TYPE));

        assertThat(response.getStatusInfo()).isEqualTo(Response.Status.OK);
        verify(dao).createTip(tipCaptor.capture());
        assertThat(tipCaptor.getValue().getId()).isNotNull();
        assertThat(tipCaptor.getValue().getEssence()).isEqualTo(tipRepresentation.getEssence());
        assertThat(tipCaptor.getValue().getDifficult()).isEqualTo(tipRepresentation.getDifficult());
    }

    @Test
    public void listTips() {
        final ImmutableList<Tip> tips = ImmutableList.of(tip);
        when(dao.getAllTips(tip.getFlashcardId())).thenReturn(tips);

        final List<Tip> response = resources.client().target(tipsURI)
                .request().get(new GenericType<List<Tip>>() {
                });

        verify(dao).getAllTips(tip.getFlashcardId());
        assertThat(response).containsAll(tips);
    }
}
