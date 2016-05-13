package com.bls.patronage;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FilePathsCoderTest {

    private String baseLocation = "./storage";
    private UUID userId = UUID.randomUUID();
    private UUID fileId = UUID.randomUUID();

    private String encoded = new StringBuilder()
            .append("/users")
            .append("/")
            .append(userId)
            .append("/files")
            .append("/")
            .append(fileId)
            .toString();
    private Path decoded = Paths.get(baseLocation, userId.toString(), fileId.toString());

    @Test
    public void testURIToFile() throws MalformedURLException {
        URI uri = FilePathsCoder.resolveURIToFile(ResourceImitation.class, decoded);

        assertThat(uri.toString()).isEqualTo(encoded);
    }

    @Test
    public void testPathToFile() {
        Path path = FilePathsCoder.resolvePathToFile(Paths.get(baseLocation), fileId, userId);

        assertThat(path).isEqualTo(decoded);
    }

    @javax.ws.rs.Path("/users/{userId}/files")
    private class ResourceImitation {
    }
}
