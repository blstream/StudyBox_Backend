package com.bls.patronage.cv;

import com.bls.patronage.api.FlashcardRepresentation;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class CVResponseTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final int FIXTURE_STATUS_OK = 1;
    private final int FIXTURE_STATUS_BAD_REQUEST = 2;
    private final int FIXTURE_STATUS_SERVER_ERROR = 0;
    private final String FIXTURE_ERROR_MESSAGE = "Opis bdu";
    private final FlashcardRepresentation flashcard1 =
            new FlashcardRepresentation("Sample question?", "Sample answer.", false)
                    .setId(UUID.fromString("12345678-9012-3456-7890-123456789012"));
    private final FlashcardRepresentation flashcard2 =
            new FlashcardRepresentation("Sample question2?", "Sample answer2.", true)
                    .setId(UUID.fromString("92345678-1019-3556-7850-129456789019"));
    private final List<FlashcardRepresentation> FIXTURE_FLASHCARDS = Arrays.asList(flashcard1, flashcard2);

    private CVResponse cvresponseOk;
    private CVResponse cvresponseBadRequest;
    private CVResponse cvresponseServerError;


    @Before
    public void setup() {
        cvresponseOk = new CVResponse(FIXTURE_FLASHCARDS, FIXTURE_STATUS_OK, null);
        cvresponseBadRequest = new CVResponse(null, FIXTURE_STATUS_BAD_REQUEST, FIXTURE_ERROR_MESSAGE);
        cvresponseServerError = new CVResponse(null, FIXTURE_STATUS_SERVER_ERROR, FIXTURE_ERROR_MESSAGE);
    }

    @Test
    public void resultOkFromJSON() throws Exception {

        final CVResponse result = MAPPER.readValue(fixture("fixtures/CVResponseOk.json"), CVResponse.class);

        assertThat(result).isEqualToComparingFieldByField(cvresponseOk);
    }

    @Test
    public void resultBadRequestFromJSON() throws Exception {

        final CVResponse result = MAPPER.readValue(fixture("fixtures/CVResponseBadRequest.json"), CVResponse.class);

        assertThat(result).isEqualToComparingFieldByField(cvresponseBadRequest);
    }

    @Test
    public void resultServerErrorFromJSON() throws Exception {

        final CVResponse result = MAPPER.readValue(fixture("fixtures/CVResponseServerError.json"), CVResponse.class);

        assertThat(result).isEqualToComparingFieldByField(cvresponseServerError);
    }
}

