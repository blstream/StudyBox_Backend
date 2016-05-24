package com.bls.patronage.cv;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class CVRequestTest {

    private String FIXTURE_URL;
    private String FIXTURE_ACTION;
    private ObjectMapper MAPPER;
    private CVRequest request;

    @Before
    public void setUp() throws MalformedURLException {
        MAPPER = Jackson.newObjectMapper();
        FIXTURE_URL = "http://localhost:2000/storage/91337b08-45ee-4257-9ddc-148a69b972cb/CV/46001cf8-055d-4780-8d2c-c496fc51c2af";
        FIXTURE_ACTION = "ImageToFlashcard";
        request = new CVRequest(FIXTURE_URL, FIXTURE_ACTION);
    }

    @Test
    public void resultToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/CVRequest.json"), CVRequest.class));

        assertThat(MAPPER.writeValueAsString(request)).isEqualTo(expected);
    }
}
