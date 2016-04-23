package com.bls.patronage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public interface StreamPersistenceService {
    Path persistStream(InputStream stream, Path location) throws IOException;
}
