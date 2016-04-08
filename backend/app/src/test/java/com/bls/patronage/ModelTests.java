package com.bls.patronage;

import com.bls.patronage.db.model.Deck;
import com.bls.patronage.db.model.Flashcard;
import com.bls.patronage.db.model.IdentifiableEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ModelTests {

    private String testUUID;
    private UUID testDeckUUID;
    private String testQuestion;
    private String testAnswer;
    private String testName;

    @Before
    public void setup() {
        testDeckUUID = UUID.fromString("12345678-9012-3456-7890-123456789012");
        testUUID = "12345678-9012-3456-7890-123456789012";
        testQuestion = "Are you ok?";
        testAnswer = "Yes, thank you.";
        testName = "Math";
    }

    @Test
    public void creatingIdentifiableEntity() {
        IdentifiableEntity identifiableEntity = new IdentifiableEntity(
                UUID.fromString(testUUID)
        ) {
        };

        assertThat(
                identifiableEntity.getId()
        ).isEqualToComparingFieldByField(
                UUID.fromString(testUUID)
        );
    }

    @Test
    public void creatingIdentifiableEntityWithoutParameters() {
        IdentifiableEntity identifiableEntity = new IdentifiableEntity() {
        };

        assertThat(identifiableEntity.getId()).isNull();
    }

    @Test
    public void creatingDeckWithoutArguments() {
        Deck deck = new Deck();

        assertThat(deck.getName()).isNull();
        assertThat(deck.getId()).isNull();
    }

    @Test
    public void creatingDeckWithOneNull() {
        Deck deck = new Deck(null);

        assertThat(deck.getName()).isNull();
        assertThat(deck.getId()).isNull();
    }

    @Test
    public void creatingDeckWithName() {
        Deck deck = new Deck(testName);

        assertThat(deck.getName()).isEqualTo(testName);
        assertThat(deck.getId()).isNull();
    }

    @Test
    public void creatingDeckWithBothArguments() {
        Deck deck = new Deck(UUID.fromString(testUUID), testName);

        assertThat(deck.getName()).isEqualTo(testName);
        assertThat(deck.getId()).isEqualTo(UUID.fromString(testUUID));
    }

    @Test
    public void creatingDeckWithBothArgumentsString() {
        Deck deck = new Deck(testUUID, testName);

        assertThat(deck.getName()).isEqualTo(testName);
        assertThat(deck.getId()).isEqualTo(UUID.fromString(testUUID));
    }

    @Test
    public void creatingFlashcardWithTwoNulls() {
        Flashcard flashcard = new Flashcard(null, null, testDeckUUID);

        assertThat(flashcard.getAnswer()).isNull();
        assertThat(flashcard.getQuestion()).isNull();
        assertThat(flashcard.getId()).isNull();
        assertThat(flashcard.getDeckId()).isEqualTo(testDeckUUID);
    }

    @Test
    public void creatingFlashcardWithTwoStrings() {
        Flashcard flashcard = new Flashcard(testQuestion, testAnswer, testDeckUUID);

        assertThat(flashcard.getAnswer()).isEqualTo(testAnswer);
        assertThat(flashcard.getQuestion()).isEqualTo(testQuestion);
        assertThat(flashcard.getId()).isNull();
        assertThat(flashcard.getDeckId()).isEqualTo(testDeckUUID);
    }

    @Test
    public void creatingFlashcardWithStringId() {
        Flashcard flashcard = new Flashcard(testUUID, testDeckUUID);

        assertThat(flashcard.getAnswer()).isNull();
        assertThat(flashcard.getQuestion()).isNull();
        assertThat(flashcard.getId()).isEqualTo(UUID.fromString(testUUID));
        assertThat(flashcard.getDeckId()).isEqualTo(testDeckUUID);
    }

    @Test
    public void creatingFlashcardWithId() {
        Flashcard flashcard = new Flashcard(UUID.fromString(testUUID), testDeckUUID);

        assertThat(flashcard.getAnswer()).isNull();
        assertThat(flashcard.getQuestion()).isNull();
        assertThat(flashcard.getId()).isEqualTo(UUID.fromString(testUUID));
        assertThat(flashcard.getDeckId()).isEqualTo(testDeckUUID);
    }

    @Test
    public void creatingFlashcardWithAllArgumentsString() {
        Flashcard flashcard = new Flashcard(testUUID, testQuestion, testAnswer, testDeckUUID);

        assertThat(flashcard.getAnswer()).isEqualTo(testAnswer);
        assertThat(flashcard.getQuestion()).isEqualTo(testQuestion);
        assertThat(flashcard.getId()).isEqualTo(UUID.fromString(testUUID));
        assertThat(flashcard.getDeckId()).isEqualTo(testDeckUUID);
    }

    @Test
    public void creatingFlashcardWithAllArguments() {
        Flashcard flashcard = new Flashcard(UUID.fromString(testUUID), testQuestion, testAnswer, testDeckUUID);

        assertThat(flashcard.getAnswer()).isEqualTo(testAnswer);
        assertThat(flashcard.getQuestion()).isEqualTo(testQuestion);
        assertThat(flashcard.getId()).isEqualTo(UUID.fromString(testUUID));
        assertThat(flashcard.getDeckId()).isEqualTo(testDeckUUID);
    }
}
