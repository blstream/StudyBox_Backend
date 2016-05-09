package com.bls.patronage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;

public interface StorageService {
    Path persistStream(InputStream stream, Path location) throws IOException, URISyntaxException;

    void deleteFile(Path location) throws IOException;

    File getFile(Path filePath);
}
