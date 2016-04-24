package com.bls.patronage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public interface StreamPersistenceService {
    URL persistStream(InputStream stream, URL location) throws IOException, URISyntaxException;

    void deleteStream(URL location) throws IOException;
}
