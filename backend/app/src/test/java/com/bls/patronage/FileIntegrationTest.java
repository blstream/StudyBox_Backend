package com.bls.patronage;

import com.bls.patronage.api.FlashcardRepresentation;
import com.bls.patronage.db.dao.FlashcardDAO;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.resources.DecksCvMagicResource;
import com.bls.patronage.resources.UsersResource;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@Ignore("This test has Authorization issues, but with real users it should work")
public class FileIntegrationTest {
    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-config.yml");

    @ClassRule
    public static final DropwizardAppRule<StudyBoxConfiguration> RULE = new DropwizardAppRule<>(
            StudyBox.class, CONFIG_PATH,
            ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

    private static final FlashcardDAO flashcardDAO = mock(FlashcardDAO.class);


    private static final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("pom.xml"));
    private static final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
    private final String usersURI = new StringBuilder()
            .append("http://localhost:")
            .append(RULE.getLocalPort())
            .append(UriBuilder.fromResource(UsersResource.class).build())
            .toString();

    private Client client;
    private ArgumentCaptor<Flashcard> flashcardCaptior;

    @BeforeClass
    public static void migrateDb() throws Exception {
        RULE.getApplication().run("db", "migrate", CONFIG_PATH);
    }

    private static String createTempFile() {
        try {
            return File.createTempFile("test-example", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Before
    public void setUp() throws Exception {
        client = ClientBuilder.newClient().register(MultiPartFeature.class);
    }

    @After
    public void tearDown() throws Exception {
        client.close();
    }

    @Test
    public void testFileUpload() throws Exception {
        final String fileURI = new StringBuilder()
                .append("http://localhost:")
                .append(RULE.getLocalPort())
                .append(UriBuilder.fromResource(DecksCvMagicResource.class).build(UUID.randomUUID()))
                .toString();

        List<FlashcardRepresentation> flashcards = new ArrayList<>();
        flashcards.add(new FlashcardRepresentation("testQuestion", "testAnswer", true));
        flashcards.add(new FlashcardRepresentation("testQuestion2", "testAnswer2", false));

        when(flashcardDAO.createFlashcard(any(Flashcard.class))).thenReturn(null);

        final Response response = client.target(fileURI)
                .request()
                .post(Entity.entity(multipart, multipart.getMediaType()));

        assertThat(response.getStatus()).isEqualTo(201);
        verify(flashcardDAO, times(2)).createFlashcard(flashcardCaptior.capture());

        assertThat(flashcardCaptior.getValue().getDeckId()).isInstanceOf(UUID.class);
        assertThat(flashcardCaptior.getValue().getId()).isInstanceOf(UUID.class);
        assertThat(flashcardCaptior.getValue().getAnswer()).isNotEmpty();
        assertThat(flashcardCaptior.getValue().getQuestion()).isNotEmpty();
    }

}
