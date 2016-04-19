package com.bls.patronage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class UserRepresentationTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final String FIXTURE_ID = "12345678-1234-1234-1234-123456789012";
    private final String FIXTURE_EMAIL = "test@bar.baz";
    private final String FIXTURE_NAME = "foo";
    private final String FIXTURE_PASSWORD = "secretPass";
    private UserRepresentation userRequest;
    private UserRepresentation userResponse;


    @Before
    public void setup() {
        userRequest = new UserRepresentation(FIXTURE_EMAIL, FIXTURE_NAME, FIXTURE_PASSWORD);
        userResponse = new UserRepresentation(FIXTURE_EMAIL, FIXTURE_NAME).setId(UUID.fromString(FIXTURE_ID));
    }

    @Test
    public void resultToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/userRepresentationResponse.json"), UserRepresentation.class));

        assertThat(MAPPER.writeValueAsString(userResponse)).isEqualTo(expected);
    }

    @Test
    public void resultFromJSON() throws Exception {

        final UserRepresentation newResult = MAPPER.readValue(fixture("fixtures/userRepresentationRequest.json"), UserRepresentation.class);

        assertThat(newResult).isEqualToComparingFieldByField(userRequest);
    }
}
