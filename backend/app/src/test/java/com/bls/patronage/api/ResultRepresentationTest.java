package com.bls.patronage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class ResultRepresentationTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final String FIXTURE_ID = "12345678-1234-1234-1234-123456789012";
    private final Boolean FIXTURE_IS_CORRECT_ANSWER = true;
    private final Integer FIXTURE_CORRECT_ANSWERS = 30;
    private ResultRepresentation resultRequest;
    private ResultRepresentation resultResponse;


    @Before
    public void setup() {
        resultRequest = new ResultRepresentation(UUID.fromString(FIXTURE_ID), FIXTURE_IS_CORRECT_ANSWER);
        resultResponse = new ResultRepresentation(UUID.fromString(FIXTURE_ID), FIXTURE_CORRECT_ANSWERS);
    }

    @Test
    public void resultToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/resultRepresentationResponse.json"), ResultRepresentation.class));

        assertThat(MAPPER.writeValueAsString(resultResponse)).isEqualTo(expected);
    }

    @Test
    public void resultFromJSON() throws Exception {

        final ResultRepresentation newResult = MAPPER.readValue(fixture("fixtures/resultRepresentationRequest.json"), ResultRepresentation.class);

        assertThat(newResult).isEqualToComparingFieldByField(resultRequest);
    }
}
