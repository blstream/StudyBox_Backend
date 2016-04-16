package com.bls.patronage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class FlashcardRepresentationTests {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final String FIXTURE_QUESTION = "Are you ok?";
    private final String FIXTURE_ANSWER = "Yes, thank you.";
    private final String FIXTURE_ID = "12345678-1234-1234-1234-123456789012";
    private final String FIXTURE_DECKID = "12345678-0000-1111-2222-123456789012";
    private FlashcardRepresentation flashcard;
    private FlashcardRepresentation flashcardWithId;
    private FlashcardRepresentation flashcardWithIdAndDeckId;

    @Before
    public void setup() {
        flashcard = new FlashcardRepresentation(FIXTURE_QUESTION, FIXTURE_ANSWER, false);
        flashcardWithId = new FlashcardRepresentation(FIXTURE_QUESTION, FIXTURE_ANSWER, false)
                .setId(UUID.fromString(FIXTURE_ID));
        flashcardWithIdAndDeckId = new FlashcardRepresentation(FIXTURE_QUESTION, FIXTURE_ANSWER, false)
                .setId(UUID.fromString(FIXTURE_ID)).setDeckId(UUID.fromString(FIXTURE_DECKID));
    }

    @Test
    public void FlashcardToJSON() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/flashcard.json"), FlashcardRepresentation.class));

        assertThat(MAPPER.writeValueAsString(flashcard)).isEqualTo(expected);
    }

    @Test
    public void FlashcardFromJSON() throws Exception {

        final FlashcardRepresentation newFlashcard = MAPPER.readValue(fixture("fixtures/flashcard.json"), FlashcardRepresentation.class);

        assertThat(newFlashcard).isEqualToComparingFieldByField(flashcard);
    }

    @Test
    public void FlashcardToJSONWithId() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/flashcardWithId.json"), FlashcardRepresentation.class));

        assertThat(MAPPER.writeValueAsString(flashcardWithId)).isEqualTo(expected);
    }

    @Test
    public void FlashcardFromJSONWithId() throws Exception {

        final FlashcardRepresentation newFlashcard = MAPPER.readValue(fixture("fixtures/flashcardWithId.json"), FlashcardRepresentation.class);

        assertThat(newFlashcard).isEqualToComparingFieldByField(flashcardWithId);
    }

    @Test
    public void FlashcardToJSONWithIdAndDeckId() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/flashcardWithIdAndDeckId.json"), FlashcardRepresentation.class));

        assertThat(MAPPER.writeValueAsString(flashcardWithIdAndDeckId)).isEqualTo(expected);
    }

    @Test
    public void FlashcardFromJSONWithIdAndDeckId() throws Exception {

        final FlashcardRepresentation newFlashcard = MAPPER.readValue(fixture("fixtures/flashcardWithIdAndDeckId.json"), FlashcardRepresentation.class);

        assertThat(newFlashcard).isEqualToComparingFieldByField(flashcardWithIdAndDeckId);
    }
}
