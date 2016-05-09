package com.bls.patronage.helpers;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FilePathsCoderTest {

    private String baseLocation = "foo";
    private UUID userId = UUID.randomUUID();
    private UUID fileId = UUID.randomUUID();

    private String encoded = "/users/" + userId + "/files/" + fileId;
    private Path decoded = Paths.get(baseLocation, userId.toString(), fileId.toString());

    @Test
    public void testEncode() throws MalformedURLException {
        URI uri = FilePathsCoder.encodeFilePath(decoded);

        assertThat(uri.toString()).isEqualTo(encoded);
    }

    @Test
    public void testDecode() {
        Path path = FilePathsCoder.decodeFilePath(Paths.get(baseLocation), userId, fileId);

        assertThat(path).isEqualTo(decoded);
    }
}
