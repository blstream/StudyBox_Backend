package com.bls.patronage.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class DeckRepresentationTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private DeckRepresentation deck;
    private DeckRepresentation deckWithDifferentName;

    @Before
    public void setup() {
        deck = new DeckRepresentation("math", true);
        deckWithDifferentName = new DeckRepresentation("physics", false);
    }

    @Test
    public void serializesToJSON() throws Exception {
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/deckRepresentation.json"), DeckRepresentation.class));

        assertThat(MAPPER.writeValueAsString(deck)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        DeckRepresentation newDeck
                = MAPPER.readValue(fixture("fixtures/deckRepresentation.json"), DeckRepresentation.class);
        assertThat(newDeck.getName()).isEqualTo(deck.getName());
    }

    @Test
    public void serializesToJSONwithDifferentDeckName() throws Exception {
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/deckRepresentation.json"), DeckRepresentation.class));

        assertThat(MAPPER.writeValueAsString(deckWithDifferentName)).isNotEqualTo(expected);
    }

    @Test
    public void deserializesFromJSONwithDifferentDeckName() throws Exception {
        DeckRepresentation newDeck
                = MAPPER.readValue(fixture("fixtures/deckRepresentation.json"), DeckRepresentation.class);
        assertThat(newDeck.getName())
                .isNotEqualTo(deckWithDifferentName.getName());
    }
}
