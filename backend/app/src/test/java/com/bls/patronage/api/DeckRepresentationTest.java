package com.bls.patronage.api;

import com.bls.patronage.db.model.Deck;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Before;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

public class DeckRepresentationTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private final String FIXTURE_DECK_NAME = "math";
    private final Boolean FIXTURE_DECK_PUBLIC = true;
    private final int FIXTURE_FLASHCARD_NUMBER = 30;
    private final String FIXTURE_CREATOR_EMAIL = "test@foo.baz";
    private final String FIXTURE_DECK_ID = "11111111-0000-2222-4444-333333333333";
    private DeckRepresentation deck;
    private DeckRepresentation deckWithFlashcardNumber;
    private DeckRepresentation deckWithCreatorEmail;
    private Deck dbModel;
    private DeckRepresentation deckFromDbModel;
    private DeckRepresentation deckWithFlashcardNumberAndCreatorEmail;

    @Before
    public void setup() {
        deck = new DeckRepresentation(FIXTURE_DECK_NAME, FIXTURE_DECK_PUBLIC);
        deckWithFlashcardNumber = new DeckRepresentation(FIXTURE_DECK_NAME, FIXTURE_DECK_PUBLIC).setFlashcardsNumber(FIXTURE_FLASHCARD_NUMBER);
        deckWithCreatorEmail = new DeckRepresentation(FIXTURE_DECK_NAME, FIXTURE_DECK_PUBLIC).setCreatorEmail(FIXTURE_CREATOR_EMAIL);
        deckWithFlashcardNumberAndCreatorEmail = new DeckRepresentation(FIXTURE_DECK_NAME, FIXTURE_DECK_PUBLIC)
                .setCreatorEmail(FIXTURE_CREATOR_EMAIL).setFlashcardsNumber(FIXTURE_FLASHCARD_NUMBER);
        dbModel = new Deck(FIXTURE_DECK_ID, FIXTURE_DECK_NAME, FIXTURE_DECK_PUBLIC);
        deckFromDbModel = new DeckRepresentation(dbModel);
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


        assertThat(newDeck).isEqualToComparingFieldByField(deck);
    }

    @Test
    public void serializesToJSONWithFlashcardNumber() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/deckRepresentationWithFlashcardsNumber.json"), DeckRepresentation.class));


        assertThat(MAPPER.writeValueAsString(deckWithFlashcardNumber)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSONWithFlashcardNumber() throws Exception {

        DeckRepresentation newDeck
                = MAPPER.readValue(fixture("fixtures/deckRepresentationWithFlashcardsNumber.json"), DeckRepresentation.class);


        assertThat(newDeck).isEqualToComparingFieldByField(deckWithFlashcardNumber);
    }

    @Test
    public void serializesToJSONWithCreatorEmail() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/deckRepresentationWithCreatorEmail.json"), DeckRepresentation.class));


        assertThat(MAPPER.writeValueAsString(deckWithCreatorEmail)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSONWithCreatorEmail() throws Exception {

        DeckRepresentation newDeck
                = MAPPER.readValue(fixture("fixtures/deckRepresentationWithCreatorEmail.json"), DeckRepresentation.class);


        assertThat(newDeck).isEqualToComparingFieldByField(deckWithCreatorEmail);
    }

    @Test
    public void serializesToJSONWithFlashcardNumberAndCreatorEmail() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/deckRepresentationWithFlashcardsNumberAndCreatorEmail.json"), DeckRepresentation.class));


        assertThat(MAPPER.writeValueAsString(deckWithFlashcardNumberAndCreatorEmail)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSONWithFlashcardNumberAndCreatorEmail() throws Exception {

        DeckRepresentation newDeck
                = MAPPER.readValue(fixture("fixtures/deckRepresentationWithFlashcardsNumberAndCreatorEmail.json"), DeckRepresentation.class);


        assertThat(newDeck).isEqualToComparingFieldByField(deckWithFlashcardNumberAndCreatorEmail);
    }

    @Test
    public void serializesToJSONWithId() throws Exception {

        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(fixture("fixtures/deckRepresentationWithId.json"), DeckRepresentation.class));


        assertThat(MAPPER.writeValueAsString(deckFromDbModel)).isEqualTo(expected);
    }

    @Test
    public void deserializesFromJSONWithId() throws Exception {

        DeckRepresentation newDeck
                = MAPPER.readValue(fixture("fixtures/deckRepresentationWithId.json"), DeckRepresentation.class);


        assertThat(newDeck).isEqualToComparingFieldByField(deckFromDbModel);
    }
}
