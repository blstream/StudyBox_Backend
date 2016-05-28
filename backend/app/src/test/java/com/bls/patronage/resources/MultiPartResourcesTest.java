package com.bls.patronage.resources;

import com.bls.patronage.StorageContexts;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.Tip;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MultiPartResourcesTest extends BasicAuthenticationTest {
    //IDs
    private static final UUID deckId = UUID.randomUUID();
    private static final UUID flashcardId = UUID.randomUUID();
    private static final UUID tipId = UUID.randomUUID();
    private static final UUID dataId = UUID.randomUUID();

    //Multipart
    private static final FormDataMultiPart multiPart = new FormDataMultiPart()
            .field("file", new ByteArrayInputStream("foo".getBytes()), MediaType.MULTIPART_FORM_DATA_TYPE);

    //URIs
    private static final String tipEssenceImageURI = UriBuilder
            .fromResource(TipResource.class)
            .build(deckId, flashcardId, tipId)
            .toString() + UriBuilder.fromMethod(TipResource.class, "postEssenceImage").toString();
    private static final String flashcardQuestionImageURI = UriBuilder
            .fromResource(FlashcardResource.class)
            .build(deckId, flashcardId)
            .toString() + UriBuilder.fromMethod(FlashcardResource.class, "postQuestionImage").toString();
    private static final String flashcardAnswerImageURI = UriBuilder
            .fromResource(FlashcardResource.class)
            .build(deckId, flashcardId)
            .toString() + UriBuilder.fromMethod(FlashcardResource.class, "postAnswerImage").toString();

    //URL
    private static URL dataURL;

    //Captors
    @Captor
    private ArgumentCaptor<Tip> tipCaptor;
    @Captor
    private ArgumentCaptor<Flashcard> flashcardCaptor;

    @Before
    public void setUpClass() throws Exception {
        dataURL = new URL("http://localhost:2000/storage/" + UUID.randomUUID() + "/test/" + dataId);

        when(storageService.create(any(InputStream.class), any(StorageContexts.class), any(UUID.class))).thenReturn(dataId);
    }


    private Response postMultipart(String uri) {
        return authResources.client()
                .register(MultiPartFeature.class)
//                .register(new AbstractBinder() {
//                    @Override
//                    protected void configure() {
//                        bind(request).to(HttpServletRequest.class);
//                    }
//                });
                .target(uri)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", "Basic " + encodedCredentials)
                .post(Entity.entity(multiPart, multiPart.getMediaType()));
    }

    @Test
    public void testTipEssenceMultipart() throws MalformedURLException {
        Tip tip = new Tip(tipId, "foo", 1, flashcardId, deckId);
        when(tipDAO.getTipById(tipId)).thenReturn(tip);
        when(storageService.createPublicURL(any(HttpServletRequest.class), eq(StorageResource.class), eq(dataId), eq(StorageContexts.TIPS), any(UUID.class)))
                .thenReturn(dataURL);

        Response response = postMultipart(tipEssenceImageURI);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        verify(tipDAO).updateTip(tipCaptor.capture());
        assertThat(tipCaptor.getValue().getEssenceImageURL()).isEqualTo(dataURL.toString());
    }

    @Test
    public void testFlashcardQuestionMultipart() throws MalformedURLException {
        Flashcard flashcard = new Flashcard(flashcardId, "foo", "baz", deckId, false);
        when(flashcardDAO.getFlashcardById(flashcardId)).thenReturn(flashcard);
        when(storageService.createPublicURL(any(HttpServletRequest.class), eq(StorageResource.class), any(UUID.class), eq(StorageContexts.FLASHCARDS), eq(dataId)))
                .thenReturn(dataURL);

        Response response = postMultipart(flashcardQuestionImageURI);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        verify(flashcardDAO).updateFlashcard(flashcardCaptor.capture());
        assertThat(flashcardCaptor.getValue().getQuestionImageURL()).isEqualTo(dataURL.toString());
    }

    @Test
    public void testFlashcardAnswerMultipart() throws MalformedURLException {
        Flashcard flashcard = new Flashcard(flashcardId, "foo", "baz", deckId, false);
        when(flashcardDAO.getFlashcardById(flashcardId)).thenReturn(flashcard);
        when(storageService.createPublicURL(any(HttpServletRequest.class), eq(StorageResource.class), any(UUID.class), eq(StorageContexts.FLASHCARDS), eq(dataId)))
                .thenReturn(dataURL);

        Response response = postMultipart(flashcardAnswerImageURI);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        verify(flashcardDAO).updateFlashcard(flashcardCaptor.capture());
        assertThat(flashcardCaptor.getValue().getAnswerImageURL()).isEqualTo(dataURL.toString());
    }
}
