package com.bls.patronage.resources;

import com.bls.patronage.FilePathsCoder;
import com.bls.patronage.StorageException;
import com.bls.patronage.StreamPersistenceBundle;
import com.bls.patronage.cv.CVRequest;
import com.bls.patronage.cv.CVResponse;
import com.bls.patronage.db.dao.DeckDAO;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.User;
import io.dropwizard.auth.Auth;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.util.Objects;
import java.util.UUID;

@Path("/users/{userId}/storage")
@Produces(MediaType.APPLICATION_JSON)
public class StorageResource {

    private final FlashcardDAO flashcardDAO;
    private final DeckDAO deckDAO;
    private final StreamPersistenceBundle bundle;

    public StorageResource(StreamPersistenceBundle bundle, DeckDAO deckDAO, FlashcardDAO flashcardDAO) {
        this.bundle = bundle;
        this.deckDAO = deckDAO;
        this.flashcardDAO = flashcardDAO;
    }

    @GET
    @Path("/{fileId}")
    public StreamingOutput getFile(@Auth User user,
                                   @PathParam("fileId") UUID fileId) throws StorageException {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException,
                    WebApplicationException {
                Writer writer = new BufferedWriter(new OutputStreamWriter(bundle.getFile(fileId, user.getId())));
                writer.flush();
            }
        };
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @Auth User user,
            @FormDataParam("file") InputStream uploadedInputStream) throws StorageException, MalformedURLException {

        final Deck deck = new Deck(UUID.randomUUID());

        UUID dataId = bundle.persistStream(uploadedInputStream, user.getId());

        CVRequest message = new CVRequest(
                FilePathsCoder.resolveURIToFile(StorageResource.class, dataId, user.getId()),
                "ImageToFlashcard"
        );

        Response response = bundle.informService(message);

        saveFlashcardsFromResponse(response, deck, user.getId());

        bundle.deleteFile(dataId, user.getId());

        return Response.ok().status(Response.Status.CREATED).build();
    }

    private void saveFlashcardsFromResponse(Response response, Deck deck, UUID userId) {
        deckDAO.createDeck(deck, userId);
        if (response.getEntity() != null) {
            response
                    .readEntity(CVResponse.class)
                    .getFlashcards()
                    .stream()
                    .filter(Objects::nonNull)
                    .forEach(flashcardRepresentation -> flashcardDAO.createFlashcard(
                            flashcardRepresentation.setDeckId(deck.getId()).map()
                            )
                    );
        }
    }
}
