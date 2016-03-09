package com.bls.patronage.model;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import com.bls.patronage.db.model.Deck;

public class DeckTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private Deck deck;
    private Deck deckWithDifferentId;
    private Deck deckWithDifferentName;

    @Before
    public void setup() {
        deck = new Deck(UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"), "biology");
        deckWithDifferentId = new Deck(UUID.fromString("27671bed-5441-4eb7-98a7-6fcdf3f939bb"), "biology");
        deckWithDifferentName = new Deck(UUID.fromString("8ad4b503-5bfc-4d8a-a761-0908374892b1"), "math");
    }

    @Test
    public void serializesToJSON() throws Exception {
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/deck.json"), Deck.class));

        assertThat(MAPPER.writeValueAsString(deck)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSON() throws Exception {
        assertThat(MAPPER.readValue(fixture("fixtures/deck.json"), Deck.class))
                .isEqualTo(deck);
    }

    @Test
    public void serializesToJSONwithDifferentDeckId() throws Exception {
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/deck.json"), Deck.class));

        assertThat(MAPPER.writeValueAsString(deckWithDifferentId)).isNotEqualTo(expected);
    }

    @Test
    public void serializesToJSONwithDifferentDeckName() throws Exception {
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/deck.json"), Deck.class));

        assertThat(MAPPER.writeValueAsString(deckWithDifferentName)).isNotEqualTo(expected);
    }

    @Test
    public void deserializesFromJSONwithDifferentDeckId() throws Exception {
        assertThat(MAPPER.readValue(fixture("fixtures/deck.json"), Deck.class))
                .isNotEqualTo(deckWithDifferentId);
    }

    @Test
    public void deserializesFromJSONwithDifferentDeckName() throws Exception {
        assertThat(MAPPER.readValue(fixture("fixtures/deck.json"), Deck.class))
                .isNotEqualTo(deckWithDifferentName);
    }
}
