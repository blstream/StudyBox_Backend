package com.bls.patronage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class TipRepresentationTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final String FIXTURE_ID = "12345678-1234-1234-1234-123456789012";
    private final String FIXTURE_DECK_ID = "12345678-0000-1111-2222-123456789012";
    private final String FIXTURE_FLASHCARD_ID = "12345678-2222-1111-0000-123456789012";
    private final String FIXTURE_ESSENCE = "TestEssence";
    private final Integer FIXTURE_DIFFICULTY = 5;
    private TipRepresentation tipRepresentation;
    private TipRepresentation tipRepresentationWithId;
    private TipRepresentation tipRepresentationWithDeckId;
    private TipRepresentation tipRepresentationWithFlashcardId;
    private TipRepresentation tipRepresentationWithDeckIdAndFlashcardId;


    @Before
    public void setup() {
        tipRepresentation = new TipRepresentation(FIXTURE_ESSENCE, FIXTURE_DIFFICULTY);
        tipRepresentationWithId = new TipRepresentation(FIXTURE_ESSENCE, FIXTURE_DIFFICULTY)
                .setId(UUID.fromString(FIXTURE_ID));
        tipRepresentationWithDeckId = new TipRepresentation(FIXTURE_ESSENCE, FIXTURE_DIFFICULTY)
                .setDeckId(UUID.fromString(FIXTURE_DECK_ID));
        tipRepresentationWithFlashcardId = new TipRepresentation(FIXTURE_ESSENCE, FIXTURE_DIFFICULTY)
                .setFlashcardId(UUID.fromString(FIXTURE_FLASHCARD_ID));
        tipRepresentationWithDeckIdAndFlashcardId = new TipRepresentation(FIXTURE_ESSENCE, FIXTURE_DIFFICULTY)
                .setDeckId(UUID.fromString(FIXTURE_DECK_ID)).setFlashcardId(UUID.fromString(FIXTURE_FLASHCARD_ID));
    }

    @Test
    public void resultToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/tipRepresentation.json"), TipRepresentation.class));

        assertThat(MAPPER.writeValueAsString(tipRepresentation)).isEqualTo(expected);
    }

    @Test
    public void resultFromJSON() throws Exception {

        final TipRepresentation newTip = MAPPER.readValue(fixture("fixtures/tipRepresentation.json"), TipRepresentation.class);

        assertThat(newTip).isEqualToComparingFieldByField(tipRepresentation);
    }

    @Test
    public void resultWithIdToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/tipRepresentationWithId.json"), TipRepresentation.class));

        assertThat(MAPPER.writeValueAsString(tipRepresentationWithId)).isEqualTo(expected);
    }

    @Test
    public void resultWithIdFromJSON() throws Exception {

        final TipRepresentation newTip = MAPPER.readValue(fixture("fixtures/tipRepresentationWithId.json"), TipRepresentation.class);

        assertThat(newTip).isEqualToComparingFieldByField(tipRepresentationWithId);
    }

    @Test
    public void resultWithDeckIdToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/tipRepresentationWithDeckId.json"), TipRepresentation.class));

        assertThat(MAPPER.writeValueAsString(tipRepresentationWithDeckId)).isEqualTo(expected);
    }

    @Test
    public void resultWithDeckIdFromJSON() throws Exception {

        final TipRepresentation newTip = MAPPER.readValue(fixture("fixtures/tipRepresentationWithDeckId.json"), TipRepresentation.class);

        assertThat(newTip).isEqualToComparingFieldByField(tipRepresentationWithDeckId);
    }

    @Test
    public void resultWithFlashcardIdToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/tipRepresentationWithFlashcardId.json"), TipRepresentation.class));

        assertThat(MAPPER.writeValueAsString(tipRepresentationWithFlashcardId)).isEqualTo(expected);
    }

    @Test
    public void resultWithFlashcardIdFromJSON() throws Exception {

        final TipRepresentation newTip = MAPPER.readValue(fixture("fixtures/tipRepresentationWithFlashcardId.json"), TipRepresentation.class);

        assertThat(newTip).isEqualToComparingFieldByField(tipRepresentationWithFlashcardId);
    }

    @Test
    public void resultWithDeckIdAndFlashcardIdToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/tipRepresentationWithDeckIdAndFlashcardId.json"), TipRepresentation.class));

        assertThat(MAPPER.writeValueAsString(tipRepresentationWithDeckIdAndFlashcardId)).isEqualTo(expected);
    }

    @Test
    public void resultWithDeckIdAndFlashcardIdFromJSON() throws Exception {

        final TipRepresentation newTip = MAPPER.readValue(fixture("fixtures/tipRepresentationWithDeckIdAndFlashcardId.json"), TipRepresentation.class);

        assertThat(newTip).isEqualToComparingFieldByField(tipRepresentationWithDeckIdAndFlashcardId);
    }
}
