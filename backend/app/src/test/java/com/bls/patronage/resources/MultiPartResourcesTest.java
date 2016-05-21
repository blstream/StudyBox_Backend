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
    private static FormDataMultiPart multiPart;
    private static UUID deckId;
    private static UUID flashcardId;
    private static UUID tipId;
    private static UUID dataId;
    private static String tipEssenceImageURI;
    private static String flashcardQuestionImageURI;
    private static String flashcardAnswerImageURI;
    private static URL dataURL;
    @Captor
    private ArgumentCaptor<Tip> tipCaptor;
    @Captor
    private ArgumentCaptor<Flashcard> flashcardCaptor;

    @Before
    public void setUpClass() throws Exception {
        deckId = UUID.randomUUID();
        flashcardId = UUID.randomUUID();
        tipId = UUID.randomUUID();
        dataId = UUID.randomUUID();
        dataURL = new URL("http://localhost:2000/storage/" + UUID.randomUUID() + "/test/" + dataId);

        multiPart = new FormDataMultiPart()
                .field("file", new ByteArrayInputStream("foo".getBytes()), MediaType.MULTIPART_FORM_DATA_TYPE);

        tipEssenceImageURI = UriBuilder
                .fromResource(TipResource.class)
                .build(deckId, flashcardId, tipId)
                .toString() + UriBuilder.fromMethod(TipResource.class, "postEssenceImage").toString();
        flashcardQuestionImageURI = UriBuilder
                .fromResource(FlashcardResource.class)
                .build(deckId, flashcardId)
                .toString() + UriBuilder.fromMethod(FlashcardResource.class, "postQuestionImage").toString();
        flashcardAnswerImageURI = UriBuilder
                .fromResource(FlashcardResource.class)
                .build(deckId, flashcardId)
                .toString() + UriBuilder.fromMethod(FlashcardResource.class, "postAnswerImage").toString();

        when(storageService.create(any(InputStream.class), any(StorageContexts.class), any(UUID.class))).thenReturn(dataId);

    }

    private Response postMultipart(String uri) {
        return authResources.client()
                .register(MultiPartFeature.class)
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
        when(storageService.createPublicURL(any(HttpServletRequest.class), eq(StorageResource.class), eq(dataId), eq(StorageContexts.FLASHCARDS), any(UUID.class)))
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
        when(storageService.createPublicURL(any(HttpServletRequest.class), eq(StorageResource.class), eq(dataId), eq(StorageContexts.FLASHCARDS), any(UUID.class)))
                .thenReturn(dataURL);

        Response response = postMultipart(flashcardAnswerImageURI);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        verify(flashcardDAO).updateFlashcard(flashcardCaptor.capture());
        assertThat(flashcardCaptor.getValue().getAnswerImageURL()).isEqualTo(dataURL.toString());
    }
}
