package com.bls.patronage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class FlashcardRepresentationTests {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private String testQuestion;
    private String testAnswer;

    @Before
    public void setup() {
        testQuestion = "Are you ok?";
        testAnswer = "Yes, thank you.";
    }

    @Test
    public void FlashcardToJSON() throws Exception {
        final FlashcardRepresentation flashcardRepresentation = new FlashcardRepresentation(testQuestion,
                testAnswer, false);

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/flashcard.json"), FlashcardRepresentation.class));

        assertThat(MAPPER.writeValueAsString(flashcardRepresentation)).isEqualTo(expected);
    }

    @Test
    public void FlashcardFromJSON() throws Exception {
        final FlashcardRepresentation flashcardRepresentation = new FlashcardRepresentation(testQuestion,
                testAnswer, false);

        assertThat(MAPPER.readValue(fixture("fixtures/flashcard.json"), FlashcardRepresentation.class))
                .isEqualToComparingFieldByField(flashcardRepresentation);
    }
}
