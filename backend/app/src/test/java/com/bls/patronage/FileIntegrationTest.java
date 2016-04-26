package com.bls.patronage;

import com.bls.patronage.resources.FilesResource;
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
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FileIntegrationTest {
    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-config.yml");
    @ClassRule
    public static final DropwizardAppRule<StudyBoxConfiguration> RULE = new DropwizardAppRule<>(
            StudyBox.class, CONFIG_PATH,
            ConfigOverride.config("database.url", "jdbc:h2:" + TMP_FILE));

    private static final FileDataBodyPart filePart = new FileDataBodyPart("file", new File("pom.xml"));
    private static final MultiPart multipart = new FormDataMultiPart().bodyPart(filePart);
    private static final UUID userId = UUID.fromString("b3f3882b-b138-4bc0-a96b-cd25e087ff4e");
    private final String fileURI = new StringBuilder()
            .append("http://localhost:")
            .append(RULE.getLocalPort())
            .append(UriBuilder.fromResource(FilesResource.class).build(userId))
            .toString();
    private Client client;

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
        final Response response = client.target(fileURI)
                .request()
                .header("Authorization", "Basic " + Base64.getEncoder().encodeToString("test1@mail.com:secretPass1".getBytes()))
                .post(Entity.entity(multipart, multipart.getMediaType()));

        assertThat(response.getStatus()).isEqualTo(201);
    }

}
