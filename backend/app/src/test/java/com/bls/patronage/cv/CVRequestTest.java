package com.bls.patronage.cv;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class CVRequestTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final String FIXTURE_URL = "http://patronage2016.blstream.com:2000/storage/91337b08-45ee-4257-9ddc-148a69b972cb/CV/46001cf8-055d-4780-8d2c-c496fc51c2af";
    private static final String FIXTURE_ACTION = "ImageToFlashcard";
    private static final CVRequest REQUEST = new CVRequest(FIXTURE_URL, FIXTURE_ACTION);

    @Test
    public void resultToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/CVRequest.json"), CVRequest.class));

        assertThat(MAPPER.writeValueAsString(REQUEST)).isEqualTo(expected);
    }
}
