package com.bls.patronage;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FilePathsCoderTest {

    private String baseLocation = "./storage";
    private UUID userId = UUID.randomUUID();
    private UUID fileId = UUID.randomUUID();
    private StorageContexts context = StorageContexts.FLASHCARDS;

    private String encoded = new StringBuilder()
            .append("http://localhost:2000")
            .append("/users")
            .append("/")
            .append(userId)
            .append("/")
            .append(context.toString())
            .append("/")
            .append(fileId)
            .toString();
    private Path decoded = Paths.get(baseLocation, userId.toString(), context.toString(), fileId.toString());


    @Test
    public void testURIToFile() throws MalformedURLException {
        URL baseURL = new URL("http://localhost:2000");
        URL url = FilePathsCoder.resolveURIToFile(baseURL, ResourceImitation.class, userId, context, fileId);

        assertThat(url.toString()).isEqualToIgnoringCase(encoded);
    }

    @Test
    public void testPathToFile() {
        Path path = FilePathsCoder.resolvePathToFile(Paths.get(baseLocation), userId, context, fileId);

        assertThat(path).isEqualTo(decoded);
    }

    @javax.ws.rs.Path("/users/{userId}/{context}/{dataId}")
    private class ResourceImitation {
    }
}
