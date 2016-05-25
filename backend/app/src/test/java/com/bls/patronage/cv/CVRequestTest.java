package com.bls.patronage.cv;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class CVRequestTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private static final String FIXTURE_URL = "someURLAsSting";
    private static final String FIXTURE_ACTION = "ImageToFlashcard";
    private static final CVRequest REQUEST = new CVRequest(FIXTURE_URL, FIXTURE_ACTION);

    @Test
    public void resultToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/CVRequest.json"), CVRequest.class));

        assertThat(MAPPER.writeValueAsString(REQUEST)).isEqualTo(expected);
    }
}
